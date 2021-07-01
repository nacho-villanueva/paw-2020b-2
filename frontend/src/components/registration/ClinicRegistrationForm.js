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

const ClinicRegistrationForm = () => {

    const [isLoading, setLoading] = useState(false);

    const [selectedHours, setSelectedHours] = useState([]);
    const [insurancePlans, setInsurancePlans] = useState([]);
    const [acceptedStudies, setAcceptedStudies] = useState([]);

    const history = useHistory();


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
            if(inputs.acceptedInsurance.length === 0)
                e.acceptedPlans = true;
            if(selectedHours.length === 0)
                e.hours = true

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
            <Form.Label className="bmd-label-static">Clinic's Name</Form.Label>
            <Form.Control
                type="text" className={"clinicName"}
                name="lastName" placeholder={"Clinic's Name"}
                required
            />
            <ErrorFeedback key={formErrors.clinicName} isInvalid={formErrors.clinicName}>Please insert clinic's name</ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col mt-1" controlId="clinicPhoneNumber">
            <Form.Label className="bmd-label-static">Phone Number</Form.Label>
            <Form.Control
                type="text" className={"registrationInput"}
                name="medicPhoneNumber" placeholder={"Phone Number"}
                required
            />
            <ErrorFeedback key={formErrors.phoneNumber} isInvalid={formErrors.phoneNumber}>Please insert clinic's phone number</ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="clinicStudyTypes">
            <Form.Label className="bmd-label-static">Study Types</Form.Label>
            <Typeahead
                multiple
                key={studyTypesOptions}
                selected={acceptedStudies}
                onChange={setAcceptedStudies}
                highlightOnlyResult
                options={Array.from(studyTypesOptions, item => item.name)}
                placeholder="Choose Accepted Study Types"
                id={"clinicStudyTypes"}
                required
            />
            <a href={"#/"} onClick={() => setShowStudyTypesModal(true)}>A study is not on the list? Click here to add it to our systems</a>
            <ErrorFeedback key={formErrors.acceptedStudies} isInvalid={formErrors.acceptedStudies}>Please insert clinic's accepted studies</ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="clinicInsurancePlans">
            <Form.Label className="bmd-label-static">Medical Insurance Plans</Form.Label>
            <Typeahead
                multiple
                key={insurancePlansOptions}
                selected={insurancePlans}
                onChange={setInsurancePlans}
                highlightOnlyResult
                options={Array.from(insurancePlansOptions, item => item.name)}
                placeholder="Choose Accepted Medical Insurance Plans"
                id={"clinicInsurancePlans"}
                required
            />
            <a href={"#/"} onClick={() => setShowInsurancePlansModal(true)}>Insurance Plan is not on the list? Click here to add it to our systems</a>
            <ErrorFeedback key={formErrors.acceptedPlans} isInvalid={formErrors.acceptedPlans}>Please insert clinic's accepted plans</ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="clinicOpenDays">
            <Form.Label className="bmd-label-static">Open Days & Hours</Form.Label>
            <ErrorFeedback key={formErrors.hours} isInvalid={formErrors.hours}>Please insert valid hours</ErrorFeedback>
            <SelectDaysHours onChange={setSelectedHours} selectStyle={{top:"-1000%!important;"}} selectPlaceholder={"Select Clinic's Open Days"}/>

        </Form.Group>

        {!isLoading &&
        <Button className= "create-btn registrationSubmitButton"
                type="submit" name="clinicSubmit"
                value={"Continue"}> Continue </Button>}

        {isLoading &&
        <Loader
            className= "create-btn registrationSubmitButton loading-button"
            type="ThreeDots"
            color="#FFFFFF"
            height={"25"}/>}

    </Form>
}

export default ClinicRegistrationForm;