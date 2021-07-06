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

const EditMedicTab = () => {

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
        medicalFields: []
    }

    const [accountValues, setAccountValues] = useState( {...defaultValues, verified: false})
    const [identification, setIdentification] = useState( "")
    const [medicalFields, setMedicalFields] = useState( "")

    const [modifiedValues, setModifiedValues] = useState(defaultValues)

    const resetModifiedValues = ()=>{
        setModifiedValues(defaultValues)
        setModified(false)
        setSuccess(false)
    }

    useEffect(() => {
        let loaded = false
        if(!loaded) {
            getAllMedicalFields(setMedicalFieldsOptions)
            GetMedicInfo(store.getState().auth.userId)
                .then((r) => {
                    setAccountValues(r)
                    GetIdentificationByURL(r.identification).then((i) => setIdentification(i))
                    GetMedicalFieldsByURL(r.medicalFields).then((mf) => setMedicalFields(mf))
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


                resetModifiedValues();
                setSuccess(true);
                GetMedicInfo(store.getState().auth.userId)
                    .then((info) => {
                        setAccountValues(info)
                        if(idMod) {
                            GetIdentificationByURL(info.identification).then((i) => setIdentification(i))
                        }
                        if(mfMod) {
                            GetMedicalFieldsByURL(info.medicalFields).then((mf) => setMedicalFields(mf))
                        }
                    })
            }
            setLoading(false)
        } )
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

            <EditFieldRow type="text" modified={modified} field={accountValues.licenseNumber} name={"License Number"}
                          onEdit={()=>{setModifiedValues({...modifiedValues, licenseNumber: accountValues.licenseNumber}); onEdit();}}>
                <Form.Group className="form-group col mt-1">
                    <FormControl
                        className={"fieldEditInput"}
                        placeholder={"Phone Number"}
                        value={modifiedValues.licenseNumber}
                        onChange={(val)=> {setModifiedValues({...modifiedValues, licenseNumber: val.target.value})}}
                        type={"text"} />
                    <ErrorFeedback isInvalid={errors.licenseNumber === ERROR_CODES.INVALID}>Invalid License Number</ErrorFeedback>
                </Form.Group>
            </EditFieldRow>

            <EditFieldRow type="list" modified={modified} field={medicalFields} name={"Medical Fields"}
                          onEdit={()=>{setModifiedValues({...modifiedValues, medicalFields: medicalFields}); onEdit();}}>
                <Form.Group className="form-group col mt-1">
                    <Typeahead
                        options={medicalFieldsOptions}
                        labelKey={"name"}
                        selected={modifiedValues.medicalFields}
                        highlightOnlyResult={true}
                        placeholder={"Medical Fields"}
                        onChange={(val)=> {setModifiedValues({...modifiedValues, medicalFields: val})}}
                        id={"medicalFieldsTypeahead"}
                        multiple
                        required
                    />
                    <ErrorFeedback isInvalid={errors.medicalFields === ERROR_CODES.INVALID}>Invalid Medical Fields</ErrorFeedback>
                </Form.Group>
            </EditFieldRow>

            <EditFieldRow modified={modified} type={"image"} field={identification} name={"Identification"}
                          onEdit={onEdit}>
                <Form.Group className="form-group col mt-1">
                    <Form.Control
                        type="file"
                        name="medicIdentification"
                        accept=".jpg,.jpeg.,.png"
                        onChange={(val)=> {setModifiedValues({...modifiedValues, identification: val.target.files[0]})}}
                        required
                    />
                    <ErrorFeedback isInvalid={errors.identification === ERROR_CODES.INVALID}>Invalid Identification</ErrorFeedback>
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

export default EditMedicTab;