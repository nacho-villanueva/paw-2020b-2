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
import {Trans, useTranslation} from "react-i18next";

const EditClinicTab = () => {

    const {t} = useTranslation();

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
                    setAccountValues(r)
                    GetStudyTypesByURL(r.studyTypes).then((st)=>{setStudyTypes(st)});
                    GetMedicalPlansByURL(r.insurancePlans).then((ip)=>{setInsurancePlans(ip)});
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
            <Alert show={success} variant={"success"}><Trans t={t} i18nKey={"edit-profile.save.alert"} /></Alert>
            <table className={"fieldsTable"}>
                <tbody>
                <EditFieldRow type="text" modified={modified} field={accountValues.name} name={t("edit-profile.tabs.clinic.name.label")}
                              onEdit={()=> {setModifiedValues({...modifiedValues, name:accountValues.name}); onEdit(); }}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={t("edit-profile.tabs.clinic.name.label")}
                            value={modifiedValues.name}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, name: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.name === ERROR_CODES.INVALID}><Trans t={t} i18nKey={"edit-profile.tabs.clinic.name.error"} /></ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="text" modified={modified} field={accountValues.telephone} name={t("edit-profile.tabs.clinic.telephone.label")}
                              onEdit={()=>{setModifiedValues({...modifiedValues, telephone: accountValues.telephone}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={t("edit-profile.tabs.clinic.telephone.label")}
                            value={modifiedValues.telephone}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, telephone: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.telephone === ERROR_CODES.INVALID}><Trans t={t} i18nKey={"edit-profile.tabs.clinic.telephone.error"} /></ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="list" modified={modified} field={insurancePlans} name={t("edit-profile.tabs.clinic.insurances.label")}
                              onEdit={()=>{setModifiedValues({...modifiedValues, insurancePlans: insurancePlans}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <Typeahead
                            options={medicalInsuranceOptions}
                            labelKey={"name"}
                            selected={modifiedValues.insurancePlans}
                            highlightOnlyResult={true}
                            placeholder={t("edit-profile.tabs.clinic.insurances.label")}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, insurancePlans: val})}}
                            id={"insuranceTypeahead"}
                            multiple
                            required
                        />
                        <ErrorFeedback isInvalid={errors.insurancePlans === ERROR_CODES.INVALID}><Trans t={t} i18nKey={"edit-profile.tabs.clinic.insurances.error"} /></ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="list" modified={modified} field={studyTypes} name={t("edit-profile.tabs.clinic.studies.label")}
                              onEdit={()=>{setModifiedValues({...modifiedValues, studyTypes: studyTypes}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <Typeahead
                            options={studyTypesOptions}
                            labelKey={"name"}
                            selected={modifiedValues.studyTypes}
                            highlightOnlyResult={true}
                            placeholder={t("edit-profile.tabs.clinic.studies.label")}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, studyTypes: val})}}
                            id={"insuranceTypeahead"}
                            multiple
                            required
                        />
                        <ErrorFeedback isInvalid={errors.studyTypes === ERROR_CODES.INVALID}><Trans t={t} i18nKey={"edit-profile.tabs.clinic.studies.error"} /></ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type="hours" modified={modified} field={accountValues.hours} name={t("edit-profile.tabs.clinic.hours.label")}
                              onEdit={()=>{setModifiedValues({...modifiedValues, hours: accountValues.hours}); onEdit();}}>
                    <Form.Group className="form-group col mt-1">
                        <SelectDaysHours key={accountValues.hours} defaultValue={accountValues.hours} onChange={(val) => {setModifiedValues({...modifiedValues, hours: val})}} selectStyle={{top:"-1000%!important;"}} selectPlaceholder={t("edit-profile.tabs.clinic.hours.select")}/>
                        <ErrorFeedback isInvalid={errors.hours === ERROR_CODES.INVALID || errors.hours === ERROR_CODES.MISSING_FIELD}><Trans t={t} i18nKey={"edit-profile.tabs.clinic.hours.error"} /></ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <tr>
                    <td className={"fieldName"}><Trans t={t} i18nKey={"edit-profile.tabs.clinic.verified"} /></td>
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