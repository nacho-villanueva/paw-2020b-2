import {Alert, Form, FormControl} from "react-bootstrap";
import EditFieldRow from "../inputs/EditField";
import ErrorFeedback from "../inputs/ErrorFeedback";
import {ERROR_CODES} from "../../constants/ErrorCodes";
import {Typeahead} from "react-bootstrap-typeahead";
import {SaveChanges} from "./EditProfilePage";
import {useEffect, useState} from "react";
import {
    getAllInsurancePlans,
    getAllStudyTypes
} from "../../api/CustomFields";
import {
    GetClinicInfo,
    GetMedicalPlansByURL,
    GetStudyTypesByURL
} from "../../api/UserInfo";
import {store} from "../../redux";
import {UpdateClinic, UpdateMedic} from "../../api/UpdateProfile";
import SelectDaysHours from "../inputs/SelectDaysHours";

const EditClinicTab = () => {

    const [medicalInsuranceOptions, setMedicalInsuranceOptions] = useState([]);
    const [studyTypesOptions, setStudyTypesOptions] = useState([]);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false)

    const defaultErrors = {
        name: false,
        telephone: false,
        hours: false,
        insurancePlans: false,
        studyTypes: false
    }
    const [errors, setErrors] = useState(defaultErrors)

    const [modified, setModified] = useState(false)

    const defaultValues = {
        name: "",
        telephone: "",
        hours: null,
        insurancePlans: null,
        studyTypes: null
    }

    const [accountValues, setAccountValues] = useState( {...defaultValues, verified: false})
    const [insurancePlans, setInsurancePlans] = useState( "")
    const [studyTypes, setStudyTypes] = useState( "")

    const [modifiedValues, setModifiedValues] = useState(defaultValues)

    const resetModifiedValues = ()=>{
        setModified(false)
        setModifiedValues(defaultValues)
        setSuccess(false)
    }

    useEffect(() => {
        let loaded = false
        if(!loaded) {
            getAllInsurancePlans(setMedicalInsuranceOptions)
            getAllStudyTypes(setStudyTypesOptions)
            GetClinicInfo(store.getState().auth.userId)
                .then((r) => {
                    GetStudyTypesByURL(r.studyTypes).then((st)=>{setStudyTypes(st)});
                    GetMedicalPlansByURL(r.insurancePlans).then((ip)=>{setInsurancePlans(ip)});
                    console.log(r)
                })

        }
        return () => {loaded = true}
    }, [])

    const onEdit = () => {
        setModified(true);
        setSuccess(false);
    }


    const handleSubmit = () => {
        setLoading(true)
        UpdateClinic(modifiedValues).then((r)=> {
            if(r !== true){
                setErrors(r)
            }
            else {
                let shouldIP = modifiedValues.insurancePlans !== [];
                let shouldST = modifiedValues.studyTypes !== [];

                resetModifiedValues();
                setSuccess(true);
                GetClinicInfo(store.getState().auth.userId)
                    .then((r) => {
                        setAccountValues(r);
                        if(shouldIP)
                            GetStudyTypesByURL(r.studyTypes).then((st)=>{setStudyTypes(st)});
                        if(shouldST)
                            GetMedicalPlansByURL(r.insurancePlans).then((ip)=>{setInsurancePlans(ip)});
                    })
            }
            setLoading(false)
        })
    }

    return (
        <div className={"tableContainer"}>
            <Alert show={success} variant={"success"}>Information has been updated successfully.</Alert>
            <table className={"fieldsTable"}>
                <tbody>
                <EditFieldRow type="text" modified={modified} field={accountValues.name} name={"Name"}
                              onEdit={()=> {setModifiedValues({...modifiedValues, name:accountValues.name}); onEdit(); }}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={"Name"}
                            value={modifiedValues.name}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, name: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.name === ERROR_CODES.INVALID}>Invalid Name</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="text" modified={modified} field={accountValues.telephone} name={"Phone Number"}
                              onEdit={()=>{setModifiedValues({...modifiedValues, telephone: accountValues.telephone}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={"Phone Number"}
                            value={modifiedValues.telephone}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, telephone: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.telephone === ERROR_CODES.INVALID}>Invalid Phone Number</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="list" modified={modified} field={insurancePlans} name={"Medical Insurances"}
                              onEdit={()=>{setModifiedValues({...modifiedValues, insurancePlans: insurancePlans}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <Typeahead
                            options={medicalInsuranceOptions}
                            labelKey={"name"}
                            selected={modifiedValues.insurancePlans}
                            highlightOnlyResult={true}
                            placeholder={"Medical Fields"}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, insurancePlans: val})}}
                            id={"insuranceTypeahead"}
                            multiple
                            required
                        />
                        <ErrorFeedback isInvalid={errors.insurancePlans === ERROR_CODES.INVALID}>Invalid Insurance Plans</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="list" modified={modified} field={studyTypes} name={"Medical Studies"}
                              onEdit={()=>{setModifiedValues({...modifiedValues, studyTypes: studyTypes}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <Typeahead
                            options={studyTypesOptions}
                            labelKey={"name"}
                            selected={modifiedValues.studyTypes}
                            highlightOnlyResult={true}
                            placeholder={"Medical Fields"}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, studyTypes: val})}}
                            id={"insuranceTypeahead"}
                            multiple
                            required
                        />
                        <ErrorFeedback isInvalid={errors.studyTypes === ERROR_CODES.INVALID}>Invalid Study Types</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="hours" modified={modified} field={accountValues.hours} name={"Hours"}
                              onEdit={()=>{setModifiedValues({...modifiedValues, hours: accountValues.hours}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <SelectDaysHours key={accountValues.hours} defaultValue={accountValues.hours} onChange={(val) => {setModifiedValues({...modifiedValues, hours: val})}} selectStyle={{top:"-1000%!important;"}} selectPlaceholder={""}/>
                        <ErrorFeedback isInvalid={errors.hours === ERROR_CODES.INVALID || errors.hours === ERROR_CODES.MISSING_FIELD}>Invalid Open Hours</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <tr>
                    <td className={"fieldName"}>Verified</td>
                    <td>
                        {accountValues.verified && <i className={"fas verified fa-check fa-lg verified"}/>}
                        {!accountValues.verified && <i className={"fas verified fa-times fa-lg notVerified"}/>}
                    </td>
                </tr>
                </tbody>
            </table>
            <hr className={"divider"}/>
            {modified && <SaveChanges isLoading={loading} askPassword={false} onSubmit={handleSubmit} onCancel={resetModifiedValues}/>}
        </div>)
}

export default EditClinicTab;