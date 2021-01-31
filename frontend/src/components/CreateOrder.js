import {Card, Form, Button} from "react-bootstrap";
import {useState} from "react";
import {useHistory} from "react-router-dom";

import "./Style/CreateOrder.css";

function CreateOrder(){
    const infoForm="tab-pane fade in show active";
    const medicName="dr. peppe";

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

    //form validation
    const [infoValidated, setInfoValidated] = useState(false);
    const handleInfoSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        console.log(event.target);

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            changeToClinicStep();
        }

        setInfoValidated(true);
    };



    return(
        <div className="row justify-content-center">
            <div className="card form-card">

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
                            <Form className="form" noValidate validated={infoValidated} onSubmit={handleInfoSubmit}>
                                <Form.Group>
                                    <Form.Label className="text-muted">Medic</Form.Label>
                                    <p className="lead">{medicName}</p>
                                </Form.Group>
                                <hr className="divider"/>
                                <div className="row mx-1">
                                    <Form.Group className="form-group col">
                                        <Form.Label className="bmd-label-floating">Patient's email</Form.Label>
                                        <Form.Control
                                            required type="email"
                                            name="patientEmail"
                                        />
                                        <Form.Control.Feedback type="invalid">Please input a valid email address</Form.Control.Feedback>
                                    </Form.Group>
                                </div>

                                <div className="row mx-1">
                                    <Form.Group className="form-group col">
                                        <Form.Label className="bmd-label-floating">Patient's name</Form.Label>
                                        <Form.Control
                                            required type="text"
                                            name="patientName"
                                        />
                                        <Form.Control.Feedback type="invalid">Please write the patient's name</Form.Control.Feedback>
                                    </Form.Group>
                                </div>
                                <div className="row mx-1">
                                    <Form.Group className="form-group col">
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
                                    <Form.Group className="form-group col">
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
                                        <Form.Group className="form-group">
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
                                    <Form.Group>
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
                                    onClick={handleInfoSubmit}
                                >Next</Button>
                            </Form>
                    </div>
                </div>

            </div>
        </div>

    );

}

export default CreateOrder;