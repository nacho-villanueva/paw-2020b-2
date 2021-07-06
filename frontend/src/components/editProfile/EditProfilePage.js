import {Button, Card, FormControl, Tab, Tabs} from "react-bootstrap";
import "./Style/EditProfilePage.css"
import {useState} from "react";
import AccountTab from "./EditAccountTab";
import {useSelector} from "react-redux";
import {Roles} from "../../constants/Roles";
import EditPatientProfileTab from "./EditPatientProfileTab";
import ErrorFeedback from "../inputs/ErrorFeedback";
import Loader from "react-loader-spinner";
import EditMedicTab from "./EditMedicTab";
import EditClinicTab from "./EditClinicTab";
import {Trans, useTranslation} from "react-i18next";

export const SaveChanges = (props) => {
    const [password, setPassword] = useState("")

    const {t} = useTranslation();

    const handleSubmit = (event)=>{
        event.preventDefault();
        event.stopPropagation();
        props.onSubmit(password)
    }

    return (
        <div>
            <form noValidate onSubmit={handleSubmit}>
                {props.askPassword && <div>
                    <p className={"currentPasswordTitle"}><Trans t={t} i18nKey="edit-profile.save.password.title"/></p>
                    <div className="currentPasswordGroup">
                        <p><Trans t={t} i18nKey="edit-profile.save.password.label"/></p>
                        <FormControl
                            className={"fieldEditInput"}
                            value={password}
                            onChange={(i)=>{setPassword(i.target.value)}}
                            placeholder={"••••••••••••••"}
                            type={"password"} />
                    </div>
                    <ErrorFeedback isInvalid={props.isInvalid}><Trans t={t} i18nKey="edit-profile.save.password.error"/></ErrorFeedback>
                </div>}
                <div className={"saveButtonsGroup"}>
                    {!props.isLoading &&
                        <Button type={"submit"} className="saveButton"><Trans t={t} i18nKey="edit-profile.save.submit-button"/></Button>
                    }
                    {props.isLoading && <Loader className= "saveButton loading-button" type="ThreeDots" color="#FFFFFF" height={"25"}/>}
                    <Button variant={"secondary"} onClick={props.onCancel} className= "cancelButton"><Trans t={t} i18nKey="edit-profile.save.cancel-button"/></Button>
                </div>
            </form>
        </div>
    )
}

const EditProfilePage = () => {
    const {t} = useTranslation();

    const role = useSelector(state => state.auth.role);

    let tabName = "Profile"
    if(role === Roles.PATIENT)
        tabName = t("edit-profile.tabs.patient.title")
    if(role === Roles.MEDIC)
        tabName = t("edit-profile.tabs.medic.title")
    if(role === Roles.CLINIC)
        tabName = t("edit-profile.tabs.clinic.title")

    return (
        <div className={"profileContentContainer"}>
            <Card className={"profileCard"}>
                <h4 className={"profileTitle"}><Trans t={t} i18nKey="edit-profile.title"/></h4>
                <hr className={"divider"}/>
                <Tabs>
                    <Tab eventKey={"user"} title={t("edit-profile.tabs.user.title")}><hr className={"divider"}/><AccountTab /> </Tab>
                    <Tab eventKey={"profile"} title={tabName}><hr className={"divider"}/>
                        {role === Roles.PATIENT && <EditPatientProfileTab />}
                        {role === Roles.MEDIC && <EditMedicTab />}
                        {role === Roles.CLINIC && <EditClinicTab />}
                    </Tab>
                </Tabs>
            </Card>
        </div>
    )

}

export default EditProfilePage;