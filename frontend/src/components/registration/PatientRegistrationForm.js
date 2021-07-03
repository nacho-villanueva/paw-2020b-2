import {Button, Form} from "react-bootstrap";
import {Typeahead} from "react-bootstrap-typeahead";
import {getAllInsurancePlans} from "../../api/CustomFields";
import {useEffect, useState} from "react";
import {registerPatient} from "../../api/Auth";
import ErrorFeedback from "../inputs/ErrorFeedback";
import {useHistory} from "react-router-dom";
import Loader from "react-loader-spinner";
import {Trans, useTranslation} from "react-i18next";

const PatientRegistrationForm = () => {

    const { t } = useTranslation();

    const history = useHistory();
    const [isLoading, setLoading] = useState(false);

    const onRegisterSuccess = () => { history.push("/"); setLoading(false) };

    const onRegisterFail = () => {setValidated(true); setLoading(false) };
    const [validated, setValidated] = useState(false);

    const [formErrors, setFormErrors] = useState({firstName: false, lastName:false, insurancePlan: false, insuranceNumber: false});

    const handleRegisterSubmit = (event) => {

        event.preventDefault()

        let formInputs = event.target;
        const inputs = {
            firstName: formInputs[0].value,
            lastName: formInputs[1].value,
            insurancePlan: formInputs[2].value,
            insuranceNumber: formInputs[4].value,
        }

        let insurance = insurancePlans.filter(i => i.name === inputs.insurancePlan)
        const form = event.currentTarget;

        if (form.checkValidity() === false || insurance.length !== 1) {

            let e = {firstName: false, lastName:false, insurancePlan: false, insuranceNumber: false}


            if(inputs.firstName === "")
                e.firstName = true
            if(inputs.lastName === "")
                e.lastName = true
            if(inputs.insuranceNumber === "")
                e.insuranceNumber = true
            if(insurance.length !== 1)
                e.insurancePlan = true

            setFormErrors(e)
            setValidated(true)

            event.stopPropagation()
        }
        else {
            setLoading(true)
            registerPatient(inputs.firstName + " " + inputs.lastName, insurance[0], inputs.insuranceNumber, onRegisterSuccess, onRegisterFail)
        }
    }

    const [insurancePlans, setInsurancePlans] = useState([]);

    useEffect(() => {
        let loadedInsurancePlans = false
        if(!loadedInsurancePlans) {
            getAllInsurancePlans(setInsurancePlans)
        }
        return () => {loadedInsurancePlans = true}
    }, [])



    return <Form className={"registrationForm form-signin"} noValidate validated={validated} onSubmit={handleRegisterSubmit}>
        <div style={{display:"flex"}}>
            <Form.Group className="form-group col mt-1" controlId="firstName">
                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.patient.firstname.label"/></Form.Label>
                <Form.Control
                    type="text" className={"registrationInput"}
                    name="firstName" placeholder={t("registration.patient.firstname.label")}
                    isInvalid={!!formErrors.firstName}
                    required
                />
                <ErrorFeedback key={formErrors.firstName} isInvalid={formErrors.firstName}><Trans t={t} i18nKey="registration.patient.firstname.error"/></ErrorFeedback>
            </Form.Group>

            <Form.Group className="form-group col mt-1" controlId="lastName">
                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.patient.lastname.label"/></Form.Label>
                <Form.Control
                    type="text" className={"registrationInput"}
                    name="lastName" placeholder={t("registration.patient.lastname.label")}
                    isInvalid={!!formErrors.lastName}
                    required
                />
                <ErrorFeedback isInvalid={formErrors.lastName}><Trans t={t} i18nKey="registration.patient.lastname.error"/></ErrorFeedback>
            </Form.Group>
        </div>

        <Form.Group className="form-group col" controlId="patientInsurancePlan">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.patient.insurance-plan.label"/></Form.Label>
            <Typeahead
                key={formErrors.insurancePlan}
                options={Array.from(insurancePlans, item => item.name)}
                placeholder={t("registration.patient.insurance-plan.placeholder")}
                id={"patientInsurancePlan"}
                isInvalid={!!formErrors.insurancePlan}
                required
            />
            <ErrorFeedback isInvalid={formErrors.insurancePlan}><Trans t={t} i18nKey="registration.patient.insurance-plan.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col mt-1" controlId="patientInsuranceNumber">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.patient.insurance-number.label"/></Form.Label>
            <Form.Control
                type="text" className={"registrationInput"}
                name="patientInsuranceNumber" placeholder={t("registration.patient.insurance-number.placeholder")}
                required
            />
            <ErrorFeedback isInvalid={formErrors.insuranceNumber}><Trans t={t} i18nKey="registration.patient.insurance-number.error"/></ErrorFeedback>
        </Form.Group>


        {!isLoading &&
        <Button className= "create-btn registrationSubmitButton"
                type="submit" name="patientSubmit"
                value={t("registration.patient.continue")}> <Trans t={t} i18nKey="registration.patient.continue"/> </Button>}

        {isLoading &&
        <Loader
            className= "create-btn registrationSubmitButton loading-button"
            type="ThreeDots"
            color="#FFFFFF"
            height={"25"}/>}

    </Form>
}

export default PatientRegistrationForm;