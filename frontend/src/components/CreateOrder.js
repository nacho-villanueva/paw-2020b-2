import {Card, Form, Button, Modal, Table, Collapse} from "react-bootstrap";
import {useState} from "react";
import {useHistory} from "react-router-dom";

import "./Style/CreateOrder.css";
import FormImpl from "react-bootstrap/esm/Form";

function CreateOrder(){
    const infoForm="tab-pane fade in show active";
    const medicName="dr. peppe";
    const infoSubmit="bubba";
    const clinicSubmit="newgrounds forever";
    const verifySubmit="this man... this is a wonderful man";

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

    //show selected clinic
    const [clinicItem, setClinicItem] = useState(-1);
    const selectClinic = (listItem) => { setClinicItem(listItem); console.log("ey que pasa aca", listItem);};


    //search clinics call
    const searchClinics = (event) => {
        event.preventDefault();
        event.stopPropagation();
        console.log(event);
    };
    const clinicsList = [
        {
            userId: 1,
            name:"Anisa Sanusi's Sanatory",
            email:"ass@medtransfer.com",
            telephone:"911212155",
            hours: {
                //sun, mon, tue, wed, thr, fri, sat
                openHours: ["9:00","9:00","9:00","9:00","9:00","9:00","10:00"],
                closeHours:["23:00","23:00","23:00","23:00","23:00","22:00","22:00"]
            },
            acceptedPlans: ["Galeno 300", "OSDE 4100", "Particular"],
            medicalStudies: ["Allergy test", "ECG", "Consulta"]
        },
        {
            userId: 17,
            name:"Tennesse Institute of Traumatology & Introvertion",
            email:"teinti@medtransfer.com",
            telephone:"911212155",
            hours: {
                //sun, mon, tue, wed, thr, fri, sat
                openHours: ["9:00","9:00","9:00","9:00","9:00","9:00","10:00"],
                closeHours:["23:00","23:00","23:00","23:00","23:00","22:00","22:00"]
            },
            acceptedPlans: ["Galeno 300", "OSDE 4100", "Particular"],
            medicalStudies: ["Allergy test", "ECG", "Consulta"]
        }
    ];

    const Item = (props) => {
        return(
            <li class="nav-item" key={props.clinic.name}>
                <a
                    id={props.clinic.userId} onClick={() =>{selectClinic(props.clinic.userId)}}
                    className="list-group-item list-group-item-action"
                    data-toggle="tab" role="tab"
                    aria-controls={"clinic_" + props.clinic.userId} aria-selected="false"
                >
                    <div className="justify-content-between">
                        <h5 className="mb-1">{props.clinic.name}</h5>
                    </div>
                </a>
            </li>
        );
    }

    const ClinicInfo = (props) => {
        console.log(props.clinic);
        return(
            <div
                key={"clinicInfo_" + props.clinic.userId}
            >
                pipo
                {props.clinic.userId}
                <h3>{props.clinic.name}</h3>
            </div>
        );
    }





    const daysOfTheWeek = [
        {name:"Sunday", id: 0},
        {name:"Monday", id: 1},
        {name:"Tuesday", id: 2},
        {name:"Wednesday", id: 3},
        {name:"Thursday", id: 4},
        {name:"Friday", id: 5},
        {name:"Saturday", id: 6}
    ];

    const Day = (props) => {
        return (
            <tr>
                <th>{props.item.name}</th>
                <th>
                    <Form.Group controlId={"isAvailable" + props.item.id}>
                        <Form.Control
                            type="checkbox" name={"isAvailable" + props.item.id}
                        />
                    </Form.Group>
                </th>
                <th>
                    <Form.Group controlId={"day-" + props.item.id + "-ot"}>
                        <Form.Control
                            type="text" className="form-control time-input"
                            placeholder="00:00" maxLength="5"
                            name={"day-" + props.item.id + "-ot"}
                        />
                    </Form.Group>
                </th>
                <th>
                    <Form.Group controlId={"day-" + props.item.id + "-ct"}>
                        <Form.Control
                            type="text" className="form-control time-input"
                            placeholder="00:00" maxLength="5"
                            name={"day-" + props.item.id + "-ct"}
                        />
                    </Form.Group>
                </th>
            </tr>
        );
    }


    //step 1 (order info) form validation
    const [infoValidated, setInfoValidated] = useState(false);
    const handleInfoSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        console.log("order info", event);

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

        console.log("clinic", event);
        for(let idx in event.target){
            if(idx <= 25){
                console.log(event.target[idx].name, event.target[idx].value);
            }
        }

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
                                    <Form.Group className="form-group col mt-1" controlId="clinicName">
                                        <Form.Label className="bmd-label-static">Search by clinic name</Form.Label>
                                        <Form.Control
                                            type="text" style={{paddingTop: "14px"}}
                                            name="clinicName"
                                        />
                                    </Form.Group>
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

                                </div>
                                <div className="row mx-1">
                                    <Button
                                        className="clinic-btn mx-auto"
                                        variant="primary"
                                        onClick={() => {setShow(!show);}}
                                    >
                                        {"Schedule availability "}
                                        {show===false? <i className="fas fa-chevron-down ml-2"/> : <i className="fas fa-chevron-up ml-2"/>}
                                    </Button>
                                    <Button
                                        className="clinic-btn search-btn mx-auto"
                                        onClick={searchClinics}
                                    >
                                        Search
                                    </Button>
                                </div>
                                <div className="row mx-1 justify-content-center">
                                    <Collapse in={show}>
                                        <div style={{width: "100%"}}>
                                            <Table
                                                striped bordered hover
                                                className="custom-table"
                                            >
                                                <thead>
                                                    <tr>
                                                        <th>Day</th>
                                                        <th>Available</th>
                                                        <th>From</th>
                                                        <th>To</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {daysOfTheWeek.map((item) => (
                                                        <Day key={item.id} item={item} />
                                                    ))}
                                                </tbody>
                                            </Table>
                                        </div>
                                    </Collapse>
                                </div>
                            </div>

                            <div className="card mt-5">
                                <div className="card-body">
                                    <p className="card-title h4">Results</p>
                                    <hr/>
                                    <div className="d-flex flex-row">
                                        <div id="results" className="list-group">
                                            {clinicsList.length === 0 ? <h3 className="text-center py-5 lead">No clinics found based on search filters</h3> : <></>}
                                            <ul className="nav flex-column" id="myTab" role="tablist">
                                            {clinicsList.map((item) => (
                                                <Item key={item.userId} clinic={item}/>
                                            ))}
                                            </ul>
                                        </div>
                                        <div id="data" className="data-section">
                                            <h5 class="text-muted">Selected Clinic</h5>
                                            <div className="tab-content">
                                                {clinicItem === -1 ? <h4>No clinic selected</h4> : <ClinicInfo key={"selected_" + clinicItem} clinic={clinicsList.filter(clinic => clinic.userId === clinicItem)}/>}
                                            </div>



                                        </div>
                                    </div>
                                </div>
                            </div>



                            <a onClick={changeToOrderInfoStep} className="btn btn-secondary mt-4 mb-4 float-left" role="button">Back</a>
                            <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="clinicSubmit"
                                    value={clinicSubmit}
                            >Next</Button>
                        </Form>
                    </div>
                    <div id="verify-form" className={"custom-form fade in " + verifyStep}>
                        <Form>
                            <a onClick={changeToClinicStep} className="btn btn-secondary mt-4 mb-4 float-left" role="button">Back</a>
                            <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="verifySubmit"
                                    value={verifySubmit}
                            >Next</Button>
                        </Form>
                    </div>
                </div>

            </div>
        </div>

    );

}

export default CreateOrder;