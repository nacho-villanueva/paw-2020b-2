import {Card, Form, Button, Modal} from "react-bootstrap";
import {useState} from "react";
import {useHistory} from "react-router-dom";

import "./Style/CreateOrder.css";
import FormImpl from "react-bootstrap/esm/Form";

function CreateOrder(){
    const infoForm="tab-pane fade in show active";
    const medicName="dr. peppe";
    const infoSubmit="bubba";
    const clinicSubmit="newgrounds forever";

    const history = useHistory();

    //states to know which step to show
    const [activeOrderStep, setActiveOrderStep] = useState("active");
    const [activeClinicStep, setActiveClinicStep] = useState("");
    const [activeVerifyStep, setActiveVerifyStep] = useState("");

    const [orderStep, setOrderStep] = useState("show active");
    const [clinicStep, setClinicStep] = useState("");
    const [verifyStep, setVerifyStep] = useState("");

    const changeToOrderInfoStep = (event) => {
        setOrderStep("show active");
        setActiveOrderStep("active");

        setClinicStep("");
        setActiveClinicStep("");

        setVerifyStep("");
        setActiveVerifyStep("")
    }

    const changeToClinicStep = (event) => {
        setClinicStep("show active");
        setActiveClinicStep("active");

        setOrderStep("");
        setActiveOrderStep("");

        setVerifyStep("");
        setActiveVerifyStep("")
    }

    const changeToVerifyStep = (event) => {
        setVerifyStep("show active");
        setActiveVerifyStep("active");

        setOrderStep("");
        setActiveOrderStep("");

        setClinicStep("");
        setActiveClinicStep("")
    }

    //schedule availability modal
    const [show, setShow] = useState(false);
    const handleClose = () => {setShow(false);};
    const handleShow = () => {setShow(true);};


    //search clinics call
    const searchClinics = () => {console.log('O_o');};


    //step 1 (order info) form validation
    const [infoValidated, setInfoValidated] = useState(false);
    const handleInfoSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        console.log(event);

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            console.log('a');
            changeToClinicStep();
        }

        setInfoValidated(true);
    };

    //step 2 (clinic select) form validation
    const [clinicValidated, setClinicValidated] = useState(false);
    const handleClinicSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        console.log(event);

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            console.log('b');
            changeToVerifyStep();
        }

        setInfoValidated(true);
    };



    return(
        <div className="row justify-content-center">
            <div className={"card form-card " + activeClinicStep}>

                <div className="stepper-wrapper-horizontal">
                    <div className="step-wrapper">
                        <div className={"step-number " + activeOrderStep}>1</div>
                        <div className={"step-description text-center " + activeOrderStep}>Create a new medical order</div>
                        <div className="divider-line"></div>
                    </div>
                    <div className="step-wrapper">
                        <div className={"step-number " + activeClinicStep}>2</div>
                        <div className={"step-description text-center " + activeClinicStep}>Select clinic</div>
                        <div className="divider-line"></div>
                    </div>
                    <div className="step-wrapper">
                        <div className={"step-number " + activeVerifyStep}>3</div>
                        <div className={"step-description text-center " + activeVerifyStep}>Submit medical order</div>
                    </div>
                </div>
                <div className="row justify-content-center">
                    <hr className="divider spacer"/>
                </div>


                <div className="tab-content mt-9">
                    <div id="info-form" className={"custom-form tab-pane fade in " + orderStep}>
                            <Form noValidate validated={infoValidated} onSubmit={handleInfoSubmit}>
                                <Form.Group>
                                    <Form.Label className="text-muted">Medic</Form.Label>
                                    <p className="lead mb-0">{medicName}</p>
                                    <Form.Control
                                            required type="text"
                                            name="medicName"
                                            value={medicName}
                                            className="custom-hidden"
                                    />
                                </Form.Group>
                                <hr className="divider mt-0"/>
                                <div className="row mx-1">
                                    <Form.Group className="form-group col" controlId="patientEmail">
                                        <Form.Label className="bmd-label-floating">Patient's email</Form.Label>
                                        <Form.Control
                                            required type="email"
                                            name="patientEmail"
                                        />
                                        <Form.Control.Feedback type="invalid">Please input a valid email address</Form.Control.Feedback>
                                    </Form.Group>
                                </div>

                                <div className="row mx-1">
                                    <Form.Group className="form-group col" controlId="patientName">
                                        <Form.Label className="bmd-label-floating">Patient's name</Form.Label>
                                        <Form.Control
                                            required type="text"
                                            name="patientName"
                                        />
                                        <Form.Control.Feedback type="invalid">Please write the patient's name</Form.Control.Feedback>
                                    </Form.Group>
                                </div>
                                <div className="row mx-1">
                                    <Form.Group className="form-group col" controlId="patientInsurancePlan">
                                        <Form.Label className="bmd-label-floating">Patient's insurance plan</Form.Label>
                                        <Form.Control
                                            required as="select"
                                            name="patientInsurancePlan"
                                        >
                                            <option>None (insert SS number)</option>
                                            <option>IbaiLife 390</option>
                                            <option>Galeno Azul</option>
                                            <option>OSDE 4100</option>
                                        </Form.Control>
                                        <Form.Control.Feedback type="invalid">Please select an insurance plan</Form.Control.Feedback>
                                    </Form.Group>
                                    <Form.Group className="form-group col" controlId="patientInsuranceNumber">
                                        <Form.Label className="bmd-label-floating">Patient's insurance number</Form.Label>
                                        <Form.Control
                                            required type="text"
                                            name="patientInsuranceNumber"
                                            className="mt-2"
                                        />
                                        <Form.Control.Feedback type="invalid">Please enter the patient's insurance number</Form.Control.Feedback>
                                    </Form.Group>
                                </div>
                                <hr className="mt-3 mb-2"/>
                                <div className="row mx-1">
                                    <div className="col">
                                        <Form.Group className="form-group" controlId="studyType">
                                            <Form.Label className="bmd-label-static">Study type</Form.Label>
                                            <Form.Control
                                                required as="select"
                                                name="studyType"
                                                placeholder="Pick a study type"
                                            >
                                                <option>MRI</option>
                                                <option>Blood test</option>
                                                <option>Allergy test</option>
                                            </Form.Control>
                                            <Form.Control.Feedback type="invalid">Please pick a study type</Form.Control.Feedback>
                                        </Form.Group>
                                    </div>
                                </div>
                                <div className="col">
                                    <Form.Group controlId="orderDescription">
                                        <Form.Label className="bmd-label-static">Order description</Form.Label>
                                        <textarea
                                            type="text"
                                            name="orderDescription"
                                            rows="10" className="form-control area-custom"
                                        />
                                        <Form.Control.Feedback type="invalid">interesting...</Form.Control.Feedback>
                                    </Form.Group>
                                </div>
                                <hr className="mt-3 mb-2"/>

                                <a onClick={history.goBack} className="btn btn-secondary mt-4 mb-4 float-left" role="button">Cancel</a>

                                <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="infoSubmit"
                                    value={infoSubmit}
                                >Next</Button>
                            </Form>
                    </div>
                    <div id="clinic-form" className={"custom-form tab-pane fade in " + clinicStep}>
                        <Form noValidate validated={clinicValidated} onSubmit={handleClinicSubmit}>
                            <div className="search-block">
                                <div className="row mx-1 pt-2">
                                    <Form.Group className="form-group col" controlId="clinicName">
                                        <Form.Label className="bmd-label-static">Search by clinic name</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="clinicName"
                                        />
                                    </Form.Group>
                                    <Button
                                        className="btn-outline clinic-btn avail-btn mr-2"
                                        variant="secondary"
                                        onClick={handleShow}
                                    >
                                        Schedule availability
                                    </Button>
                                </div>
                                <div className="row mx-1">
                                    <Form.Group className="form-group col" controlId="insurancePlan">
                                        <Form.Label className="bmd-label-static">Search by insurance plan</Form.Label>
                                        <Form.Control
                                            as="select"
                                            name="insurancePlan"
                                        >
                                            <option>None (insert SS number)</option>
                                            <option>IbaiLife 390</option>
                                            <option>Galeno Azul</option>
                                            <option>OSDE 4100</option>
                                        </Form.Control>
                                    </Form.Group>
                                    <Button
                                        className="clinic-btn search-btn mr-2"
                                        onClick={searchClinics}
                                    >
                                        Search
                                    </Button>
                                </div>
                            </div>
                            <Modal show={show} onHide={handleClose}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Schedule availability</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>THIS IS THE MODAL BABYYYYYY</Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={handleClose}>Close</Button>
                                    <Button variant="primary" onClick={handleClose}>Save changes</Button>
                                </Modal.Footer>
                            </Modal>

                            <div>

                            </div>


                            <a onClick={changeToOrderInfoStep} className="btn btn-secondary mt-4 mb-4 float-left" role="button">Back</a>
                            <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="clinicSubmit"
                                    value={clinicSubmit}
                            >Next</Button>
                        </Form>
                    </div>
                </div>

            </div>
        </div>

    );

}

export default CreateOrder;