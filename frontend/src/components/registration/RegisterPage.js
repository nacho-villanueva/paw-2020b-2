import NavBar from "../NavBar";
import "./Style/RegisterPage.css"
import {Tab, Tabs} from "react-bootstrap";
import PatientRegistrationForm from "./PatientRegistrationForm";
import MedicRegistrationForm from "./MedicRegistrationForm";
import ClinicRegistrationForm from "./ClinicRegistrationForm";

const RegisterPage = () => {
    return <div className={"registerMainContainer"}>
        <NavBar />
        <div className={"registerContentContainer"}>
            <div className={"registerCard"}>
                <div className={"registerCardTitle"}>
                    <h2>Before you continue...</h2>
                    <h4>We need a couple more details.</h4>
                </div>
                <p style={{marginLeft:"2%"}} className={"text-muted"}>Choose to register as a: </p>
                <Tabs>
                    <Tab eventKey="patient" title="Patient">
                        <PatientRegistrationForm />
                    </Tab>
                    <Tab eventKey="medic" title="Medic">
                        <MedicRegistrationForm/>
                    </Tab>
                    <Tab eventKey="clinic" title="Clinic">
                        <ClinicRegistrationForm />
                    </Tab>
                </Tabs>
            </div>
        </div>
    </div>
}

export default RegisterPage;