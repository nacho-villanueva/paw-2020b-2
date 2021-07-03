import NavBar from "../NavBar";
import "./Style/RegisterPage.css"
import {Tab, Tabs} from "react-bootstrap";
import PatientRegistrationForm from "./PatientRegistrationForm";
import MedicRegistrationForm from "./MedicRegistrationForm";
import ClinicRegistrationForm from "./ClinicRegistrationForm";
import {Trans, useTranslation} from "react-i18next";

const RegisterPage = () => {

    const { t } = useTranslation();

    return <div className={"registerMainContainer"}>
        <NavBar />
        <div className={"registerContentContainer"}>
            <div className={"registerCard"}>
                <div className={"registerCardTitle"}>
                    <h2><Trans t={t} i18nKey="registration.title"/></h2>
                    <h4><Trans t={t} i18nKey="registration.subtitle"/></h4>
                </div>
                <p style={{marginLeft:"2%"}} className={"text-muted"}><Trans t={t} i18nKey="registration.tabs"/></p>
                <Tabs>
                    <Tab eventKey="patient" title={t("role.patient")}>
                        <PatientRegistrationForm />
                    </Tab>
                    <Tab eventKey="medic" title={t("role.medic")}>
                        <MedicRegistrationForm/>
                    </Tab>
                    <Tab eventKey="clinic" title={t("role.clinic")}>
                        <ClinicRegistrationForm />
                    </Tab>
                </Tabs>
            </div>
        </div>
    </div>
}

export default RegisterPage;