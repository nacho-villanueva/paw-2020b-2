import {Button, Form} from "react-bootstrap";
import {Typeahead} from "react-bootstrap-typeahead";
import MedicalFieldModal from "./MedicalFieldModal";
import {useEffect, useState} from "react";
import {getAllMedicalFields} from "../../api/CustomFields";
import {registerMedic} from "../../api/Auth";
import ErrorFeedback from "../inputs/ErrorFeedback";
import {useHistory} from "react-router-dom";
import Loader from "react-loader-spinner";
import {Trans, useTranslation} from "react-i18next";

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

    const { t } = useTranslation();

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
                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.medic.firstname.label"/></Form.Label>
                <Form.Control
                    type="text" className={"registrationInput"}
                    name="firstName" placeholder={t("registration.medic.firstname.label")}
                    required
                />
                <ErrorFeedback key={formErrors.firstName} isInvalid={formErrors.firstName}><Trans t={t} i18nKey="registration.medic.firstname.error"/></ErrorFeedback>
            </Form.Group>

            <Form.Group className="form-group col mt-1" controlId="lastName">
                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.medic.lastname.label"/></Form.Label>
                <Form.Control
                    type="text" className={"registrationInput"}
                    name="lastName" placeholder={t("registration.medic.lastname.label")}
                    required
                />
                <ErrorFeedback key={formErrors.lastName} isInvalid={formErrors.lastName}><Trans t={t} i18nKey="registration.medic.lastname.error"/></ErrorFeedback>
            </Form.Group>
        </div>

        <Form.Group className="form-group col mt-1" controlId="medicPhoneNumber">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.medic.phone-number.label"/></Form.Label>
            <Form.Control
                type="text" className={"registrationInput"}
                name="medicPhoneNumber" placeholder={t("registration.medic.phone-number.label")}
                required
            />
            <ErrorFeedback key={formErrors.phoneNumber} isInvalid={formErrors.phoneNumber}><Trans t={t} i18nKey="registration.medic.phone-number.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col mt-1" controlId="medicLicenseNumber">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.medic.license.label"/></Form.Label>
            <Form.Control
                type="text" className={"registrationInput"}
                name="medicLicenseNumber" placeholder={t("registration.medic.license.label")}
                required
            />
            <ErrorFeedback key={formErrors.licenseNumber} isInvalid={formErrors.licenseNumber}><Trans t={t} i18nKey="registration.medic.license.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col mt-1" controlId="medicLicenseNumber">
            <Form.Label className="bmd-label"><Trans t={t} i18nKey="registration.medic.identification.label"/></Form.Label>
            <Form.Control
                type="file" className={"registrationInput"}
                name="medicIdentification" placeholder={t("registration.medic.identification.label")}
                accept=".jpg,.jpeg.,.png"
                required
            />
            <small className="text-muted"><Trans t={t} i18nKey="registration.medic.identification.help"/></small>
            <ErrorFeedback key={formErrors.identification} isInvalid={formErrors.identification}><Trans t={t} i18nKey="registration.medic.identification.error"/></ErrorFeedback>
        </Form.Group>

        <Form.Group className="form-group col" controlId="medicInsurancePlan">
            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="registration.medic.medical-fields.label"/></Form.Label>
            <Typeahead
                key={medicalFieldsOptions}
                selected={medicalFields}
                onChange={setMedicalFields}
                multiple
                dropup
                highlightOnlyResult
                options={Array.from(medicalFieldsOptions, item => item.name)}
                placeholder={t("registration.medic.medical-fields.placeholder")}
                id={"medicInsurancePlan"}
                required
            />
            <a href={"#/"} onClick={() => setShowMedicalFieldModal(true)}><Trans t={t} i18nKey="registration.medic.medical-fields.modal"/></a>
            <ErrorFeedback key={formErrors.medicalFields} isInvalid={formErrors.medicalFields}><Trans t={t} i18nKey="registration.medic.medical-fields.error"/></ErrorFeedback>
        </Form.Group>


        {!isLoading &&
        <Button className= "create-btn registrationSubmitButton"
                type="submit" name="medicSubmit"> <Trans t={t} i18nKey="registration.medic.continue"/> </Button>}

        {isLoading &&
        <Loader
            className= "create-btn registrationSubmitButton loading-button"
            type="ThreeDots"
            color="#FFFFFF"
            height={"25"}/>}

    </Form>
}

export default MedicRegistrationForm;