import {Button, Form} from "react-bootstrap";
import {Typeahead} from "react-bootstrap-typeahead";
import MedicalFieldModal from "./MedicalFieldModal";
import {useEffect, useState} from "react";
import {getAllMedicalFields} from "../../api/CustomFields";
import {registerMedic} from "../../api/Auth";
import ErrorFeedback from "../inputs/ErrorFeedback";
import {useHistory} from "react-router-dom";
import Loader from "react-loader-spinner";

function getBase64(file) {
    return new Promise((resolve) => {
        let fileReader = new FileReader();
        fileReader.onload = (e) => resolve(fileReader.result.split(",")[1]);
        fileReader.readAsDataURL(file);
    });
}


const MedicRegistrationForm = () => {

    const [showMedicalFieldModal, setShowMedicalFieldModal] = useState(false);
    const [medicalFields, setMedicalFields] = useState([])
    const [medicalFieldsOptions, setMedicalFieldsOptions] = useState([])
    const [validated, setValidated] = useState(false)

    const history = useHistory();
    const [isLoading, setLoading] = useState(false);

    const defaultFormErrors = {
        firstName: false,
        lastName: false,
        phoneNumber: false,
        licenseNumber: false,
        identification: false,
        medicalFields: false
    }
    const [formErrors, setFormErrors] = useState(defaultFormErrors)

    /*Callbacks for creating Medical Fields*/
    const mfCallbackSuccess = (field) => {
        setMedicalFields([...medicalFields, field])
        getAllMedicalFields(setMedicalFieldsOptions)
    }
    const mfCallbackFail = (field) => {}

    /*Fetch Medical Fields*/
    useEffect(() => {
        let loadedMedicalFields = false
        if(!loadedMedicalFields) {
            getAllMedicalFields(setMedicalFieldsOptions)
        }
        return () => {loadedMedicalFields = true}
    }, [])


    /*Callbacks for Medic Registration*/
    const onSuccess = () => {history.push("/"); setLoading(false)};
    const onFail = (err) => {
        setLoading(false);
        let error = {...defaultFormErrors};
        for(let e of err){
            switch (e.property){
                case "telephone":
                    error.phoneNumber = true
                    break;
                case "identification":
                    error.identification = true
                    break;
                case "medicalFields":
                    error.medicalFields = true;
                    break;
                default:
                    break;
            }
        }
        setFormErrors(error)
        setValidated(true)
    };

    /*Front-End Form Validations*/
    const handleRegisterSubmit = (event) => {
        event.preventDefault()

        const form = event.currentTarget;

        let formInputs = event.target;
        const inputs = {
            firstName: formInputs[0].value,
            lastName: formInputs[1].value,
            phoneNumber: formInputs[2].value,
            licenceNumber: formInputs[3].value,
            identification: formInputs[4].files[0],
            medicalFields: []
        }

        let validImage = inputs.identification !== undefined;
        if(validImage)
            validImage = inputs.identification.type === "image/jpg" || inputs.identification.type === "image/jpeg" || inputs.identification.type === "image/png";

        let validMF = true;
        for(let mf of medicalFields){
            let currentMF = medicalFieldsOptions.filter(obj => {return obj.name === mf});
            if (currentMF.length !== 1) {
                validMF = false
                break;
            }

            inputs.medicalFields.push(currentMF[0])
        }

        if(form.checkValidity() === false || !validImage || !validMF){

            let e = defaultFormErrors;

            if(inputs.firstName === "")
                e.firstName = true
            if(inputs.lastName === "")
                e.lastName = true
            if(inputs.phoneNumber === "")
                e.phoneNumber = true
            if(inputs.licenceNumber === "")
                e.licenseNumber = true

            if(!validImage)
                e.identification = true
            if(!validated)
                e.medicalFields = true

            setFormErrors(e)

            setValidated(true)

            event.stopPropagation()
        } else {
            const medic = {
                "name": inputs.firstName + " " + inputs.lastName,
                "telephone": inputs.phoneNumber,
                "identification": {
                    "contentType": inputs.identification.type,
                    "image": ""
                },
                "licenceNumber": inputs.licenceNumber,
                "medicalFields": inputs.medicalFields
            }

            setLoading(true);

            getBase64(inputs.identification).then((data) => {
                medic.identification.image = data;
                registerMedic(medic, onSuccess, onFail)
            });
        }

    }

    return <Form className={"registrationForm form-signin"} noValidate validated={validated} onSubmit={handleRegisterSubmit}>
        <MedicalFieldModal show={showMedicalFieldModal} setShow={setShowMedicalFieldModal} callbackSuccess={mfCallbackSuccess} callbackFail={mfCallbackFail}/>
        <div style={{display:"flex"}}>
            <Form.Group className="form-group col mt-1" controlId="firstName">
                <Form.Label className="bmd-label-static">First Name</Form.Label>
                <Form.Control
                    type="text" className={"registrationInput"}
                    name="firstName" placeholder={"First Name"}
                    required
                />
                <ErrorFeedback key={formErrors.firstName} isInvalid={formErrors.firstName}>Please insert your first name</ErrorFeedback>
            </Form.Group>

            <Form.Group className="form-group col mt-1" controlId="lastName">
                <Form.Label className="bmd-label-static">Last Name</Form.Label>
                <Form.Control
                    type="text" className={"registrationInput"}
                    name="lastName" placeholder={"Last Name"}
                    required
                />
                <ErrorFeedback key={formErrors.lastName} isInvalid={formErrors.lastName}>Please insert your last name</ErrorFeedback>
            </Form.Group>
        </div>

        <Form.Group className="form-group col mt-1" controlId="medicPhoneNumber">
            <Form.Label className="bmd-label-static">Phone Number</Form.Label>
            <Form.Control
                type="text" className={"registrationInput"}
                name="medicPhoneNumber" placeholder={"Phone Number"}
                required
            />
            <ErrorFeedback key={formErrors.phoneNumber} isInvalid={formErrors.phoneNumber}>Please insert a valid phone number</ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col mt-1" controlId="medicLicenseNumber">
            <Form.Label className="bmd-label-static">License Number Number</Form.Label>
            <Form.Control
                type="text" className={"registrationInput"}
                name="medicLicenseNumber" placeholder={"Medical License Number"}
                required
            />
            <ErrorFeedback key={formErrors.licenseNumber} isInvalid={formErrors.licenseNumber}>Please insert your licence number</ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col mt-1" controlId="medicLicenseNumber">
            <Form.Label className="bmd-label">Seal and Signature</Form.Label>
            <Form.Control
                type="file" className={"registrationInput"}
                name="medicLicenseNumber" placeholder={"Medical License Number"}
                accept=".jpg,.jpeg.,.png"
                required
            />
            <small className="text-muted">Please upload a photo of your seal and signature</small>
            <ErrorFeedback key={formErrors.identification} isInvalid={formErrors.identification}>Please upload a valid image</ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="medicInsurancePlan">
            <Form.Label className="bmd-label-static">Medical Fields</Form.Label>
            <Typeahead
                key={medicalFieldsOptions}
                selected={medicalFields}
                onChange={setMedicalFields}
                multiple
                dropup
                highlightOnlyResult
                options={Array.from(medicalFieldsOptions, item => item.name)}
                placeholder="Choose Medical Fields"
                id={"medicInsurancePlan"}
                required
            />
            <a href={"#/"} onClick={() => setShowMedicalFieldModal(true)}>Your field is not on the list? Click here to add it our systems</a>
            <ErrorFeedback key={formErrors.medicalFields} isInvalid={formErrors.medicalFields}>Please insert a valid medical field</ErrorFeedback>
        </Form.Group>


        {!isLoading &&
        <Button className= "create-btn registrationSubmitButton"
                type="submit" name="medicSubmit"
                value={"Continue"}> Continue </Button>}

        {isLoading &&
        <Loader
            className= "create-btn registrationSubmitButton loading-button"
            type="ThreeDots"
            color="#FFFFFF"
            height={"25"}/>}

    </Form>
}

export default MedicRegistrationForm;