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

export const SaveChanges = (props) => {
    const [password, setPassword] = useState("")

    const handleSubmit = (event)=>{
        event.preventDefault();
        event.stopPropagation();
        props.onSubmit(password)
    }

    return (
        <div>
            <form noValidate onSubmit={handleSubmit}>
                {props.askPassword && <div>
                    <p className={"currentPasswordTitle"}>Insert current password before saving changes </p>
                    <div className="currentPasswordGroup">
                        <p>Current Password</p>
                        <FormControl
                            className={"fieldEditInput"}
                            value={password}
                            onChange={(i)=>{setPassword(i.target.value)}}
                            placeholder={"••••••••••••••"}
                            type={"password"} />
                    </div>
                    <ErrorFeedback isInvalid={props.isInvalid}>Invalid Email</ErrorFeedback>
                </div>}
                <div className={"saveButtonsGroup"}>
                    {!props.isLoading &&
                        <Button type={"submit"} className="saveButton">Save Changes</Button>
                    }
                    {props.isLoading && <Loader className= "saveButton loading-button" type="ThreeDots" color="#FFFFFF" height={"25"}/>}
                    <Button variant={"secondary"} onClick={props.onCancel} className= "cancelButton">Cancel</Button>
                </div>
            </form>
        </div>
    )
}

const EditProfilePage = () => {
    const role = useSelector(state => state.auth.role);

    return (
        <div className={"profileContentContainer"}>
            <Card className={"profileCard"}>
                <h4 className={"profileTitle"}>My Profile</h4>
                <hr className={"divider"}/>
                <Tabs>
                    <Tab eventKey={"user"} title={"Account"}><hr className={"divider"}/><AccountTab /> </Tab>
                    <Tab eventKey={"profile"} title={"Profile"}><hr className={"divider"}/>
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