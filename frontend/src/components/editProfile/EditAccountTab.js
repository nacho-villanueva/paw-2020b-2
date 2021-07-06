import {useState} from "react";
import EditFieldRow from "../inputs/EditField";
import {Alert, Form, FormControl} from "react-bootstrap";
import {SaveChanges} from "./EditProfilePage";
import {UpdateAccount} from "../../api/UpdateProfile";
import {store} from "../../redux";
import {ERROR_CODES} from "../../constants/ErrorCodes";
import ErrorFeedback from "../inputs/ErrorFeedback";
import {GetUserInfo} from "../../api/UserInfo";
import {Trans, useTranslation} from "react-i18next";

const AccountTab = () => {

    const {t} = useTranslation();

    const passwordMin= 8; const passwordMax = 100;

    const [modified, setModified] = useState(false)
    const [viewPassword, setViewPassword] = useState(false)
    const [success, setSuccess] = useState(false)
    const [loading, setLoading] = useState(false)

    const defaultErrors = {
        email: false,
        password: false,
        oldPassword: false
    }
    const [errors, setErrors] = useState(defaultErrors)

    const [email, setEmail] = useState(store.getState().auth.email)

    const [modifiedValues, setModifiedValues] = useState({
        email: "",
        password: "",
    })

    const resetModifiedValues = ()=>{
        setModifiedValues({email: "", password: ""});
        setErrors(defaultErrors)
        setModified(false)
    }

    const onEdit = () => {
        setModified(true);
        setSuccess(false);
    }




    const handleSubmit = (currentPassword) => {
        setLoading(true)
        UpdateAccount(modifiedValues, currentPassword).then(r => {
            if(r !== true){
                let currentErrors = {...defaultErrors}
                currentErrors.email = r.password;
                currentErrors.password = r.password;
                currentErrors.oldPassword = r.oldPassword;
                setErrors(currentErrors);
            } else {
                    resetModifiedValues();
                    GetUserInfo(store.getState().auth.userId).then((r) => {
                        setEmail(r.email);
                    });
                    setSuccess(true);
            }
            setLoading(false)
        })
    }

    return (
        <div className={"tableContainer"}>
            <Alert show={success} variant={"success"}>Information has been updated successfully.</Alert>
            <table className={"fieldsTable"}>
                <tbody>
                    <EditFieldRow type={"text"} modified={modified} field={email} name={t("edit-profile.tabs.user.email.label")}
                                  onEdit={()=> {setModifiedValues({...modifiedValues, email:email}); onEdit(); }}>
                        <Form.Group className="form-group col mt-1">
                            <FormControl
                                className={"fieldEditInput"}
                                placeholder={t("edit-profile.tabs.user.email.label")}
                                value={modifiedValues.email}
                                onChange={(val)=> {setModifiedValues({...modifiedValues, email: val.target.value})}}
                                type={"email"} />
                        </Form.Group>
                        <ErrorFeedback isInvalid={errors.email === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.user.email.error.invalid"/></ErrorFeedback>
                        <ErrorFeedback isInvalid={errors.email === ERROR_CODES.ALREADY_EXISTS}><Trans t={t} i18nKey="edit-profile.tabs.user.email.error.already-exists"/></ErrorFeedback>
                    </EditFieldRow>

                    <EditFieldRow type={"text"} modified={modified} field={"••••••••••••••"} name={t("edit-profile.tabs.user.password.label")}
                                  onEdit={onEdit}>
                        <Form.Group className="form-group changePass col mt-1">
                            <FormControl
                                className={"fieldEditInput"}
                                placeholder={t("edit-profile.tabs.user.password.label")}
                                value={modifiedValues.password}
                                onChange={(val)=> {setModifiedValues({...modifiedValues, password: val.target.value})}}
                                type={viewPassword? "text":"password"} />
                            <a href={"#/"} onClick={()=>setViewPassword(!viewPassword)}><i className={viewPassword? "far fa-lg icons fa-eye-slash": "far fa-lg icons fa-eye"}/></a>
                        </Form.Group>
                        <ErrorFeedback isInvalid={errors.password === ERROR_CODES.INVALID}><Trans t={t} i18nKey="edit-profile.tabs.user.password.error" values={{min: passwordMin, max: passwordMax}}/></ErrorFeedback>
                    </EditFieldRow>
                </tbody>
            </table>
            <hr className={"divider"}/>
            {modified && <SaveChanges isLoading={loading} askPassword={true} isInvalid={errors.oldPassword === ERROR_CODES.INVALID} onSubmit={handleSubmit} onCancel={resetModifiedValues}/>}
        </div>)
}

export default AccountTab;