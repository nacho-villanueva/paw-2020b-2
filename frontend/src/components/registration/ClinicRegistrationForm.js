import {Button, Form} from "react-bootstrap";
import {Typeahead} from "react-bootstrap-typeahead";
import {useHistory} from "react-router-dom";
import {getAllInsurancePlans, getAllStudyTypes} from "../../api/CustomFields";
import {useEffect, useState} from "react";
import SelectDaysHours from "../inputs/SelectDaysHours";
import "./Style/ClinicRegistrationForm.css"
import ErrorFeedback from "../inputs/ErrorFeedback";
import InsurancePlanModal from "./InsurancePlanModal";
import StudyTypesModal from "./StudyTypesModal";
import {registerClinic} from "../../api/Auth";
import Loader from "react-loader-spinner";
import {Trans, useTranslation} from "react-i18next";

const ClinicRegistrationForm = () => {

    const [isLoading, setLoading] = useState(false);

    const [selectedHours, setSelectedHours] = useState([]);
    const [insurancePlans, setInsurancePlans] = useState([]);
    const [acceptedStudies, setAcceptedStudies] = useState([]);

    const history = useHistory();

    const { t } = useTranslation();


    const defaultFormErrors = {
        clinicName: false,
        phoneNumber: false,
        acceptedStudies: false,
        acceptedPlans: false,
        hours: false
    }
    const [formErrors, setFormErrors] = useState(defaultFormErrors)

    const [insurancePlansOptions, setInsurancePlansOptions] = useState([]);
    useEffect(() => {
        let loadedInsurancePlans = false
        if(!loadedInsurancePlans) {
            getAllInsurancePlans(setInsurancePlansOptions)
        }
        return () => {loadedInsurancePlans = true}
    }, [])

    const [studyTypesOptions, setStudyTypesOptions] = useState([]);
    useEffect(() => {
        let loadedAcceptedStudies = false
        if(!loadedAcceptedStudies) {
            getAllStudyTypes(setStudyTypesOptions)
        }
        return () => {loadedAcceptedStudies = true}
    }, [])

    const [showInsurancePlansModal, setShowInsurancePlansModal] = useState(false)
    const [showStudyTypesModal, setShowStudyTypesModal] = useState(false)

    /*Callbacks for creating Insurance Plans*/
    const ipCallbackSuccess = (field) => {
        setInsurancePlans([...insurancePlans, field])
        getAllInsurancePlans(setInsurancePlansOptions)
    }
    const ipCallbackFail = (field) => {}

    /*Callbacks for creating Study Types*/
    const stCallbackSuccess = (field) => {
        setAcceptedStudies([...acceptedStudies, field])
        getAllStudyTypes(setStudyTypesOptions)
    }
    const stCallbackFail = (field) => {}

    const [validated, setValidated] = useState(false);

    const onSuccess = () => {history.push("/"); setLoading(false)};
    const onFail = (err) => {
        setLoading(false);
        let error = {...defaultFormErrors};
        for(let e of err){
            switch (e.property){
                case "telephone":
                    error.phoneNumber = true
                    break;
                case "availableStudies":
                    error.acceptedStudies = true
                    break;
                case "acceptedPlans":
                    error.acceptedPlans = true;
                    break;
                default:
                    break;
            }
            if(e.property.includes("hour"))
                error.hours = true
        }
        setFormErrors(error)
        setValidated(true)
    };

    const handleRegisterSubmit = (event) => {
        event.preventDefault()

        const form = event.currentTarget;

        let formInputs = event.target;
        const inputs = {
            name: formInputs[0].value,
            phoneNumber: formInputs[1].value,
            studyTypes: studyTypesOptions.filter((sto) => acceptedStudies.includes(sto.name)),
            acceptedInsurance: insurancePlansOptions.filter((ipo) => insurancePlans.includes(ipo.name)),
            hours: selectedHours
        }

        if(form.checkValidity() === false){
            let e = {...defaultFormErrors};
            if(inputs.name === "")
                e.clinicName = true;
            if(inputs.phoneNumber === "")
                e.phoneNumber = true
            if(inputs.studyTypes.length === 0)
                e.acceptedStudies = true

            setFormErrors(e)
        }
        else {
            setLoading(true);
            registerClinic(inputs, onSuccess, onFail);
        }
        event.stopPropagation()
    }


    return <Form className={"registrationForm"} noValidate validated={validated} onSubmit={handleRegisterSubmit}>
        <InsurancePlanModal show={showInsurancePlansModal} setShow={setShowInsurancePlansModal} callbackSuccess={ipCallbackSuccess} callbackFail={ipCallbackFail}/>
        <StudyTypesModal show={showStudyTypesModal} setShow={setShowStudyTypesModal} callbackSuccess={stCallbackSuccess} callbackFail={stCallbackFail}/>

        <Form.Group className="form-group col mt-1" controlId="clinicName">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.clinic.name.label"/></Form.Label>
            <Form.Control
                type="text" className={"clinicName"}
                name="lastName" placeholder={t("registration.clinic.name.label")}
                required
            />
            <ErrorFeedback key={formErrors.clinicName} isInvalid={formErrors.clinicName}><Trans t={t} i18nKey="registration.clinic.name.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col mt-1" controlId="clinicPhoneNumber">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.clinic.phone-number.label"/></Form.Label>
            <Form.Control
                type="text" className={"registrationInput"}
                name="medicPhoneNumber" placeholder={t("registration.clinic.phone-number.label")}
                required
            />
            <ErrorFeedback key={formErrors.phoneNumber} isInvalid={formErrors.phoneNumber}><Trans t={t} i18nKey="registration.clinic.phone-number.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="clinicStudyTypes">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.clinic.study-types.label"/></Form.Label>
            <Typeahead
                multiple
                key={studyTypesOptions}
                selected={acceptedStudies}
                onChange={setAcceptedStudies}
                highlightOnlyResult
                options={Array.from(studyTypesOptions, item => item.name)}
                placeholder={t("registration.clinic.study-types.placeholder")}
                id={"clinicStudyTypes"}
                required
            />
            <a href={"#/"} onClick={() => setShowStudyTypesModal(true)}><Trans t={t} i18nKey="registration.clinic.study-types.modal"/></a>
            <ErrorFeedback key={formErrors.acceptedStudies} isInvalid={formErrors.acceptedStudies}><Trans t={t} i18nKey="registration.clinic.study-types.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="clinicInsurancePlans">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.clinic.insurances.label"/></Form.Label>
            <Typeahead
                multiple
                key={insurancePlansOptions}
                selected={insurancePlans}
                onChange={setInsurancePlans}
                highlightOnlyResult
                options={Array.from(insurancePlansOptions, item => item.name)}
                placeholder={t("registration.clinic.insurances.placeholder")}
                id={"clinicInsurancePlans"}
                required
            />
            <a href={"#/"} onClick={() => setShowInsurancePlansModal(true)}><Trans t={t} i18nKey="registration.clinic.insurances.modal"/></a>
            <ErrorFeedback key={formErrors.acceptedPlans} isInvalid={formErrors.acceptedPlans}><Trans t={t} i18nKey="registration.clinic.insurances.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="clinicOpenDays">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.clinic.hours.label"/></Form.Label>
            <ErrorFeedback key={formErrors.hours} isInvalid={formErrors.hours}><Trans t={t} i18nKey="registration.clinic.hours.error"/></ErrorFeedback>
            <SelectDaysHours onChange={setSelectedHours} selectStyle={{top:"-1000%!important;"}} selectPlaceholder={t("registration.clinic.hours.placeholder")}/>

        </Form.Group>

        {!isLoading &&
        <Button className= "create-btn registrationSubmitButton"
                type="submit" name="clinicSubmit"> <Trans t={t} i18nKey="registration.clinic.continue"/> </Button>}

        {isLoading &&
        <Loader
            className= "create-btn registrationSubmitButton loading-button"
            type="ThreeDots"
            color="#FFFFFF"
            height={"25"}/>}

    </Form>
}

export default ClinicRegistrationForm;