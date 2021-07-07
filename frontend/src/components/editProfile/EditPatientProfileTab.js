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
import {Trans, useTranslation} from "react-i18next";

const EditPatientProfileTab = () => {

    const {t} = useTranslation()

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
                        insurancePlan: r.insurancePlan.name,
                        insuranceNumber: r.insuranceNumber
                    })
            })
        }
        return () => {loaded = true}
    }, [])

    const [modified, setModified] = useState(false)

    const defaultValues = {
        name: null,
        insurancePlan: null,
        insuranceNumber: null
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
        if(modifiedValues.insurancePlan !== null) {
            for (let ip of insurancePlansOptions) {
                if (ip.name === modifiedValues.insurancePlan) {
                    insurancePlan = ip;
                    break;
                }
            }

            if(insurancePlan === null)
                insurancePlan = ""
        }

        if(insurancePlan === null && modifiedValues.insuranceNumber !== null)
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
                            insurancePlan: r.insurancePlan.name,
                            insuranceNumber: r.insuranceNumber
                        })
                    })
            }
            setLoading(false)
        })

    }

    return (
        <div className={"tableContainer"}>
            <Alert show={success} variant={"success"}><Trans t={t} i18nKey="edit-profile.save.alert"/></Alert>
            <table className={"fieldsTable"}>
                <tbody>
                <EditFieldRow type={"text"} modified={modified} field={accountValues.name} name={t("edit-profile.tabs.patient.name.label")}
                              onEdit={()=> {setModifiedValues({...modifiedValues, name:accountValues.name}); onEdit(); }}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={t("edit-profile.tabs.patient.name.label")}
                            value={modifiedValues.name}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, name: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.name === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.patient.name.error"/></ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type={"text"} modified={modified} field={accountValues.insurancePlan} name={t("edit-profile.tabs.patient.insurancePlan.label")}
                              onEdit={()=>{setModifiedValues({...modifiedValues, insurancePlan: accountValues.insurancePlan}); onEdit()}}>
                    <Form.Group className="form-group col mt-1">
                        <Typeahead
                            options={Array.from(insurancePlansOptions,(x) => x.name)}
                            defaultSelected={[accountValues.insurancePlan]}
                            highlightOnlyResult={true}
                            placeholder={t("edit-profile.tabs.patient.insurancePlan.label")}
                            id={"patientInsurancePlan"}
                            onInputChange={(val)=> {setModifiedValues({...modifiedValues, insurancePlan: val})}}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, insurancePlan: val[0]})}}
                            required
                        />
                        <ErrorFeedback isInvalid={errors.insurancePlan === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.patient.insurancePlan.error"/></ErrorFeedback>
                    </Form.Group>
                </EditFieldRow>

                <EditFieldRow type={"text"} modified={modified} field={accountValues.insuranceNumber} name={t("edit-profile.tabs.patient.insuranceNumber.label")}
                              onEdit={()=>{onEdit(); setModifiedValues({...modifiedValues, insuranceNumber: accountValues.insuranceNumber})}}>
                    <Form.Group className="form-group col mt-1">
                        <FormControl
                            className={"fieldEditInput"}
                            placeholder={t("edit-profile.tabs.patient.insuranceNumber.label")}
                            value={modifiedValues.insuranceNumber}
                            onChange={(val)=> {setModifiedValues({...modifiedValues, insuranceNumber: val.target.value})}}
                            type={"text"} />
                        <ErrorFeedback isInvalid={errors.insuranceNumber === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.patient.insuranceNumber.error"/></ErrorFeedback>
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