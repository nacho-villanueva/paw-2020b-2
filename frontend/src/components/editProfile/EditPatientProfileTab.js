import EditFieldRow from "../inputs/EditField";
import {Alert, Form, FormControl} from "react-bootstrap";
import {SaveChanges} from "./EditProfilePage";
import {useEffect, useState} from "react";
import {Typeahead} from "react-bootstrap-typeahead";
import {getAllInsurancePlans} from "../../api/CustomFields";
import {GetPatientInfo} from "../../api/UserInfo";
import {store} from "../../redux";
import {ERROR_CODES} from "../../constants/ErrorCodes";
import {UpdatePatient} from "../../api/UpdateProfile";
import ErrorFeedback from "../inputs/ErrorFeedback";

const EditPatientProfileTab = () => {

    const [insurancePlansOptions, setInsurancePlansOptions] = useState([]);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false)

    const defaultErrors = {
        name: false,
        insurancePlan: false,
        insuranceNumber: false,
    }
    const [errors, setErrors] = useState(defaultErrors)

    useEffect(() => {
        let loaded = false
        if(!loaded) {
            getAllInsurancePlans(setInsurancePlansOptions)
            GetPatientInfo(store.getState().auth.userId)
                .then((r) => {
                    setAccountValues({
                        name:r.name,
                        insurancePlan: r.insurancePlan,
                        insuranceNumber: r.insuranceNumber
                    })
            })
        }
        return () => {loaded = true}
    }, [])

    const [modified, setModified] = useState(false)

    const defaultValues = {
        name: "",
        insurancePlan: "",
        insuranceNumber: ""
    }

    const [accountValues, setAccountValues] = useState(defaultValues)

    const [modifiedValues, setModifiedValues] = useState(defaultValues)

    const resetModifiedValues = ()=>{
        setModifiedValues(defaultValues)
        setModified(false)
    }

    const onEdit = () => {
        setModified(true);
        setSuccess(false);
    }

    const handleSubmit = () => {
        let insurancePlan = null;
        if(modifiedValues.insurancePlan !== "") {
            for (let ip of insurancePlansOptions) {
                if (ip.name === modifiedValues.insurancePlan) {
                    insurancePlan = ip;
                    break;
                }
            }

            if(insurancePlan === null){
                setErrors({...defaultErrors, insurancePlan:ERROR_CODES.INVALID})
                return;
            }
        }

        if(insurancePlan == null && modifiedValues.insuranceNumber !== "")
            insurancePlan = accountValues.insurancePlan

        setLoading(true)
        UpdatePatient({name: modifiedValues.name, insurancePlan: insurancePlan, insuranceNumber: modifiedValues.insuranceNumber}).then((r)=> {
            if(r !== true){
                setErrors(r)
            }
            else{
                resetModifiedValues();
                setSuccess(true)
                GetPatientInfo(store.getState().auth.userId)
                    .then((r) => {
                        setAccountValues({
                            name:r.name,
                            insurancePlan: r.insurancePlan,
                            insuranceNumber: r.insuranceNumber
                        })
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
                <EditFieldRow type={"text"} modified={modified} field={accountValues.name} name={"Name"}
                              onEdit={()=> {setModifiedValues({...modifiedValues, name:accountValues.name}); onEdit(); }}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={"Name"}
                            value={modifiedValues.name}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, name: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.name === ERROR_CODES.INVALID}>Invalid Name</ErrorFeedback>
                        <ErrorFeedback isInvalid={errors.name === ERROR_CODES.MISSING_FIELD}>Insert Name</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type={"text"} modified={modified} field={accountValues.insurancePlan.name} name={"Medical Plan"}
                              onEdit={()=>{setModifiedValues({...modifiedValues, insurancePlan: accountValues.insurancePlan}); onEdit()}}>
                    <Form.Group className="form-group col mt-1">
                        <Typeahead
                            options={insurancePlansOptions}
                            labelKey={"name"}
                            defaultSelected={[accountValues.insurancePlan]}
                            highlightOnlyResult={true}
                            placeholder={"Insurance Plan"}
                            id={"patientInsurancePlan"}
                            onInputChange={(val)=> {setModifiedValues({...modifiedValues, insurancePlan: val})}}
                            required
                        />
                        <ErrorFeedback isInvalid={errors.insurancePlan === ERROR_CODES.INVALID}>Invalid Insurance Plan</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type={"text"} modified={modified} field={accountValues.insuranceNumber} name={"Medical Plan Number"}
                              onEdit={()=>{onEdit(); setModifiedValues({...modifiedValues, insuranceNumber: accountValues.insuranceNumber})}}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={"Medical Plan Number"}
                            value={modifiedValues.insuranceNumber}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, insuranceNumber: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.insuranceNumber === ERROR_CODES.INVALID}>Invalid Insurance Number</ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>
                </tbody>
            </table>
            <hr className={"divider"}/>
            {modified && <SaveChanges isLoading={loading} askPassword={false} onSubmit={handleSubmit} onCancel={resetModifiedValues}/>}
        </div>
    )
}

export default EditPatientProfileTab;