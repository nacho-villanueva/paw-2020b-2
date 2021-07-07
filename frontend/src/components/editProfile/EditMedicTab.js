import {Alert, Form, FormControl} from "react-bootstrap";
import EditFieldRow from "../inputs/EditField";
import ErrorFeedback from "../inputs/ErrorFeedback";
import {ERROR_CODES} from "../../constants/ErrorCodes";
import {Typeahead} from "react-bootstrap-typeahead";
import {SaveChanges} from "./EditProfilePage";
import {useEffect, useState} from "react";
import {getAllMedicalFields} from "../../api/CustomFields";
import {GetIdentificationByURL, GetMedicalFieldsByURL, GetMedicInfo} from "../../api/UserInfo";
import {store} from "../../redux";
import {UpdateMedic} from "../../api/UpdateProfile";
import {Trans, useTranslation} from "react-i18next";

const EditMedicTab = () => {

    const {t} = useTranslation()

    const [medicalFieldsOptions, setMedicalFieldsOptions] = useState([]);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false)

    const defaultErrors = {
        name: false,
        telephone: false,
        identification: false,
        licenseNumber: false,
        medicalFields: false
    }
    const [errors, setErrors] = useState(defaultErrors)

    const [modified, setModified] = useState(false)

    const defaultValues = {
        name: "",
        telephone: "",
        identification: null,
        licenseNumber: "",
        medicalFields: [],
        verified: false
    }

    const [modifiedValues, setModifiedValues] = useState(defaultValues)

    const resetModifiedStatus = ()=>{
        setModified(false)
        setSuccess(false)
    }

    const setMedicValues = (data) => {
        let newValues = {
            name: data.name,
            telephone: data.telephone,
            licenseNumber: data.licenseNumber,
            verified: data.verified
        }
        setModifiedValues((prevState) => ({...prevState, ...newValues}));
    }

    const updateIdentification = (i) => {
        setModifiedValues((prevState) => ({...prevState, identification: i}));
    }

    const updateMedicalFields = (mf) => {
        setModifiedValues((prevState) => ({...prevState, medicalFields: mf}));
    }

    useEffect(() => {
        let loaded = false
        if(!loaded) {
            getAllMedicalFields(setMedicalFieldsOptions)
            GetMedicInfo(store.getState().auth.userId)
                .then((r) => {
                    setMedicValues(r);
                    GetIdentificationByURL(r.identification).then((i) => {
                        updateIdentification(i)
                    })
                    GetMedicalFieldsByURL(r.medicalFields).then((mf) => {
                        updateMedicalFields(mf)
                    })
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
        UpdateMedic(modifiedValues).then((r) =>{
            if(r !== true) {
                setErrors(r)
            }
            else {

                let idMod = false;
                let mfMod = false;

                if(modifiedValues.identification !== defaultValues.identification)
                    idMod = true
                if(modifiedValues.medicalFields !== [])
                    mfMod = true


                resetModifiedStatus();
                setSuccess(true);
                GetMedicInfo(store.getState().auth.userId)
                    .then((info) => {
                        setMedicValues(info)
                        if(idMod) {
                            GetIdentificationByURL(info.identification).then((i) => updateIdentification(i))
                        }
                        if(mfMod) {
                            GetMedicalFieldsByURL(info.medicalFields).then((mf) => updateMedicalFields(mf))
                        }
                    })
            }
            setLoading(false)
        } )
    }

    return (
    <div className={"tableContainer"}>
        <Alert show={success} variant={"success"}><Trans t={t} i18nKey="edit-profile.save.alert" /></Alert>
        <table className={"fieldsTable"}>
            <tbody>
            <EditFieldRow type="text" modified={modified} field={modifiedValues.name} name={t("edit-profile.tabs.medic.name.label")}
                          onEdit={()=> { onEdit(); }}>
                <Form.Group className="form-group col mt-1">
                    <FormControl
                        className={"fieldEditInput"}
                        placeholder={t("edit-profile.tabs.medic.name.label")}
                        value={modifiedValues.name}
                        onChange={(val)=> {setModifiedValues({...modifiedValues, name: val.target.value})}}
                        type={"text"} />
                    <ErrorFeedback isInvalid={errors.name === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.clinic.name.error"/></ErrorFeedback>
                    <ErrorFeedback isInvalid={errors.name === ERROR_CODES.MISSING_FIELD}><Trans t={t} i18nKey="edit-profile.tabs.clinic.name.error"/></ErrorFeedback>
                </Form.Group>
            </EditFieldRow>

            <EditFieldRow type="text" modified={modified} field={modifiedValues.telephone} name={t("edit-profile.tabs.medic.telephone.label")}
                          onEdit={()=>{ onEdit();}}>
                <Form.Group className="form-group col mt-1">
                    <FormControl
                        className={"fieldEditInput"}
                        placeholder={t("edit-profile.tabs.medic.telephone.label")}
                        value={modifiedValues.telephone}
                        onChange={(val)=> {setModifiedValues({...modifiedValues, telephone: val.target.value})}}
                        type={"text"} />
                    <ErrorFeedback isInvalid={errors.telephone === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.medic.telephone.error"/></ErrorFeedback>
                    <ErrorFeedback isInvalid={errors.telephone === ERROR_CODES.MISSING_FIELD}><Trans t={t} i18nKey="edit-profile.tabs.medic.telephone.error"/></ErrorFeedback>
                </Form.Group>
            </EditFieldRow>

            <EditFieldRow type="text" modified={modified} field={modifiedValues.licenseNumber} name={t("edit-profile.tabs.medic.license.label")}
                          onEdit={()=>{ onEdit();}}>
                <Form.Group className="form-group col mt-1">
                    <FormControl
                        className={"fieldEditInput"}
                        placeholder={t("edit-profile.tabs.medic.license.label")}
                        value={modifiedValues.licenseNumber}
                        onChange={(val)=> {setModifiedValues({...modifiedValues, licenseNumber: val.target.value})}}
                        type={"text"} />
                    <ErrorFeedback isInvalid={errors.licenseNumber === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.medic.license.error"/></ErrorFeedback>
                    <ErrorFeedback isInvalid={errors.licenseNumber === ERROR_CODES.MISSING_FIELD}><Trans t={t} i18nKey="edit-profile.tabs.medic.license.error"/></ErrorFeedback>
                </Form.Group>
            </EditFieldRow>

            <EditFieldRow type="list" modified={modified} field={modifiedValues.medicalFields} name={t("edit-profile.tabs.medic.fields.label")}
                          onEdit={()=>{ onEdit();}}>
                <Form.Group className="form-group col mt-1">
                    <Typeahead
                        options={medicalFieldsOptions}
                        labelKey={"name"}
                        selected={modifiedValues.medicalFields}
                        highlightOnlyResult={true}
                        placeholder={t("edit-profile.tabs.medic.fields.label")}
                        onChange={(val)=> {setModifiedValues({...modifiedValues, medicalFields: val})}}
                        id={"medicalFieldsTypeahead"}
                        multiple
                        required
                    />
                    <ErrorFeedback isInvalid={errors.medicalFields === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.medic.fields.error"/></ErrorFeedback>
                    <ErrorFeedback isInvalid={errors.medicalFields === ERROR_CODES.MISSING_FIELD}><Trans t={t} i18nKey="edit-profile.tabs.medic.fields.error"/></ErrorFeedback>
                </Form.Group>
            </EditFieldRow>

            <EditFieldRow modified={modified} type={"image"} field={modifiedValues.identification} name={t("edit-profile.tabs.medic.identification.label")}
                          onEdit={onEdit}>
                <Form.Group className="form-group col mt-1">
                    <Form.Control
                        type="file"
                        name="medicIdentification"
                        accept=".jpg,.jpeg.,.png"
                        onChange={(val)=> {setModifiedValues({...modifiedValues, identification: val.target.files[0]})}}
                        required
                    />
                    <ErrorFeedback isInvalid={errors.identification === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.medic.identification.error"/></ErrorFeedback>
                    <ErrorFeedback isInvalid={errors.identification === ERROR_CODES.MISSING_FIELD}><Trans t={t} i18nKey="edit-profile.tabs.medic.identification.error"/></ErrorFeedback>
                </Form.Group>
            </EditFieldRow>

            <tr>
                <td className={"fieldName"}><Trans t={t} i18nKey="edit-profile.tabs.medic.verified"/></td>
                <td>
                    {modifiedValues.verified && <i className={"fas verified fa-check fa-lg verified"}/>}
                    {!modifiedValues.verified && <i className={"fas verified fa-times fa-lg notVerified"}/>}
                </td>
            </tr>
            </tbody>
        </table>
        <hr className={"divider"}/>
        {modified && <SaveChanges isLoading={loading} askPassword={false} onSubmit={handleSubmit} onCancel={resetModifiedStatus}/>}
    </div>)
}

export default EditMedicTab;