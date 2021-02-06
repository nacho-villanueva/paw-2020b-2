import {Form, Button, Table, Collapse, Pagination} from "react-bootstrap";
import {useState} from "react";
import {useHistory} from "react-router-dom";

import "./Style/CreateOrder.css";

function CreateOrder(){
    const infoForm="tab-pane fade in show active";
    const medicName="dr. peppe";
    const infoSubmit="bubba";
    const clinicSubmit="newgrounds forever";
    const verifySubmit="this man... this is a wonderful man";
    //DUMMY DATA (in the real version these should be made with useState to update and render the site)
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
        },
        {
            userId: 4,
            name:"Badobeep",
            email:"fnf@medtransfer.com",
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
            userId: 18,
            name:"Prosigo a la meta",
            email:"adl@medtransfer.com",
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
            userId: 26,
            name:"yo mismo no pretendo",
            email:"ocl@medtransfer.com",
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
            userId: 29,
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
            userId: 31,
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
        },
        {
            userId: 34,
            name:"Badobeep",
            email:"fnf@medtransfer.com",
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
            userId: 48,
            name:"Prosigo a la meta",
            email:"adl@medtransfer.com",
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
            userId: 46,
            name:"yo mismo no pretendo",
            email:"ocl@medtransfer.com",
            telephone:"911212155",
            hours: {
                //sun, mon, tue, wed, thr, fri, sat
                openHours: ["9:00","9:00","9:00","9:00","9:00","9:00","10:00"],
                closeHours:["23:00","23:00","23:00","23:00","23:00","22:00","22:00"]
            },
            acceptedPlans: ["Galeno 300", "OSDE 4100", "Particular"],
            medicalStudies: ["Allergy test", "ECG", "Consulta"]
        },
    ];
    const daysOfTheWeek = [
        {name:"Sunday", id: 0},
        {name:"Monday", id: 1},
        {name:"Tuesday", id: 2},
        {name:"Wednesday", id: 3},
        {name:"Thursday", id: 4},
        {name:"Friday", id: 5},
        {name:"Saturday", id: 6}
    ];
    const insurancePlans = [
        {name:'None (insert SS number)'},
        {name:'Galeno 300'},
        {name:'OSDE 4200'},
        {name:'Brook 9100'}
    ];



    const history = useHistory();

    const [orderInfo, setOrderInfo] = useState({
        medicName: medicName,
        patientName: '',
        patientEmail: '',
        clinicId: '',
        patientInsurancePlan: '',
        patientInsuranceNumber: '',
        studyType: '',
        orderDescription:'',
        medicLicenceNumber: '',
        medicEmail: ''
    });

    const getCurrentDate = () => {
        let today = new Date();
        return today.toDateString();
    };


    //states used to know which step to show
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

    //show selected clinic
    const [selectedClinic, setSelectedClinic] = useState(null);
    const selectClinic = (listItem) => { setSelectedClinic(listItem);};

    //controls/values for pagination of clinics search
    const [totalClinicPages, setTotalClinicPages] = useState(7);
    const [currentPage, setCurrentPage] = useState(1);

    const [clinicSearchValidated, setClinicSearchValidated] = useState(false);
    //search clinics call
    const searchClinics = (event) => {
        event.preventDefault();
        event.stopPropagation();
        console.log(event);
        setClinicSearchValidated(true);
        /////
        //event.target[0] -> clinicName
        ///event.target[1] -> insurancePlan
        //---
        //for 0:N:6
        //event.target[4+N] -> isAvailableN
        //event.target[4+1+N] -> day-N-ot
        //event.target[4+2+N] -> day-N-ct
        ///////
        //CODE HERE THAT CALLS THE API AND FILLS UP clinicsList with the results
        //will always search/fetch for page 1
    };

    //MISSING: pagitanation for clinics list
    const changePageAndFetch = (pageNumber) => {
        //will fetch clinics based on the already picked filters
        //only will change the currentPage value (and might update totalClinicPages if the fetch comes up with a different value for that)
        setCurrentPage(pageNumber);
    };



    //components built based on API data
    const PaginationCustom = (props) => {
        let active = props.current;

        return(
            <div className="row justify-content-center">
                <Pagination>
                    {active -1 > 0 ? <Pagination.Prev onClick={() => changePageAndFetch(active - 1)}/> : <Pagination.Prev disabled/>}
                    <Pagination.Item active>{active}</Pagination.Item>
                    {active + 1 <= props.total ?  <Pagination.Next onClick={() => changePageAndFetch(active + 1)}/> : <Pagination.Next disabled/>}
                </Pagination>
            </div>
        );
    }

    const Item = (props) => {
        return(
            <li class="nav-item" key={props.clinic.name}>
                <a
                    id={props.clinic.userId} onClick={() =>{selectClinic(props.clinic)}}
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
        return(
            <div
                className="tab-pane tab-result"
                key={"clinicInfo_" + props.item.userId}
            >
                <h3>{props.item.name}</h3>
                <Table className="table table-borderless">
                    <tbody>
                        <tr>
                            <td>Email</td>
                            <td className="output">{props.item.email}</td>
                        </tr>
                        <tr>
                            <td>Telephone</td>
                            <td className="output">{props.item.email}</td>
                        </tr>
                        <tr>
                            <td>Open hours</td>
                            <td>
                                {daysOfTheWeek.map((item) => (
                                    <div key={"oh_"+props.item.userId+"_"+item.id}>
                                        <span>{item.name}</span>&nbsp;&nbsp;&nbsp;
                                        <span>{props.item.hours.openHours[item.id] + " - " + props.item.hours.closeHours[item.id]}</span>
                                    </div>
                                ))}
                            </td>
                        </tr>
                        <tr>
                            <td>Accepted insurance</td>
                            <td className="output">
                                {props.item.acceptedPlans.map((plan) => (
                                    <span
                                        key={"plan_"+props.item.userId+"_"+plan}
                                        className="badge-sm badge-pill badge-secondary mr-1 d-inline-block"
                                    >{plan}</span>
                                ))}
                            </td>
                        </tr>
                        <tr>
                            <td>Available studies</td>
                            <td className="output">
                                {props.item.medicalStudies.map((study) => (
                                    <p key={"study_"+props.item.userId+"_"+study}>{study}</p>
                                ))}
                            </td>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }

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

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            console.log('a');

            let aux = orderInfo;
            aux.medicName = {medicName};
            aux.patientEmail = event.target[1].value;
            aux.patientName = event.target[2].value;
            aux.patientInsurancePlan = event.target[3].value;
            aux.patientInsuranceNumber = event.target[4].value;
            aux.studyType = event.target[5].value;
            aux.orderDescription = event.target[6].value;
            setOrderInfo(aux);

            changeToClinicStep();
        }

        setInfoValidated(true);
    };

    //step 2 (clinic select) form validation
    const [clinicValidated, setClinicValidated] = useState(false);
    const handleClinicSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        if(form.checkValidity() === false || selectedClinic === null){
            event.stopPropagation();
        }else{
            console.log('b');

            let aux = orderInfo;
            aux.clinicId = selectedClinic.userId;
            setOrderInfo(aux);

            changeToVerifyStep();
        }

        setClinicValidated(true);
    };

    //step 3 (verify and submit) form validation
    const [verifyValidated, setVerifyValidated] = useState(false);
    const handleVerifySubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            //checking if it worked...
            console.log("orderInfo", orderInfo);
            //changeToVerifyStep();
            //now it should send the order to the API and redirect the user to /view-study
        }

        setVerifyValidated(true);
    };



    return(
        <div className="row justify-content-center">
            <div className={"card form-card " + (activeClinicStep === "active" ? "clinic" : "")}>

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
                                            {insurancePlans.map((item) => (
                                                <option>{item.name}</option>
                                            ))}
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
                        <Form noValidate validated={clinicSearchValidated} onSubmit={searchClinics}>
                            <div className="search-block">
                                <div className="row mx-1 pt-2">
                                    <Form.Group className="form-group col mt-1" controlId="clinicName">
                                        <Form.Label className="bmd-label-static">Search by clinic name</Form.Label>
                                        <Form.Control
                                            type="text" style={{paddingTop: "10px"}}
                                            name="clinicName"
                                        />
                                    </Form.Group>
                                    <Form.Group className="form-group col" controlId="insurancePlan">
                                        <Form.Label className="bmd-label-static">Search by insurance plan</Form.Label>
                                        <Form.Control
                                            as="select"
                                            name="insurancePlan"
                                        >
                                            {insurancePlans.map((item) => (
                                                <option selected={item.name === orderInfo.patientInsurancePlan}>{item.name}</option>
                                            ))}
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
                                        type="submit" name="clinicSearchSubmit"
                                        value="search this"
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
                        </Form>

                        <Form noValidate validated={clinicValidated} onSubmit={handleClinicSubmit}>
                            <div className="card results-card mt-5">
                                <div className="card-body">
                                    <p className="card-title h4">Results</p>
                                    <hr/>
                                    <div className="d-flex flex-row">
                                        <div id="results" className="list-group result-section">
                                            {clinicsList.length === 0 ? <h3 className="text-center py-5 lead">No clinics found based on search filters</h3>
                                            :
                                            <ul className="nav flex-column" id="myTab" role="tablist">
                                            {clinicsList.map((item) => (
                                                <Item key={item.userId} clinic={item}/>
                                            ))}
                                            </ul>
                                            }
                                            {totalClinicPages > 1 ? <PaginationCustom current={currentPage} total={totalClinicPages} /> : <></>}

                                        </div>
                                        <div id="data" className="data-section">
                                            <h5 class="text-muted">Selected Clinic</h5>
                                            <div className="">
                                                {selectedClinic === null ? <h4>No clinic selected</h4> : <ClinicInfo key={"selected_" + selectedClinic.userId} item={selectedClinic}/>}
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

                    <div id="verify-form" className={"custom-form tab-pane fade in " + verifyStep}>
                        <Form noValidate validation={verifyValidated} onSubmit={handleVerifySubmit}>
                            <div className="card">
                                <div className="card-body">
                                    <div className="row">
                                        <div className="col">
                                            <p className="card-title ml-3 h4">Medical order</p>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col">
                                            <p className="card-subtitle ml-3 text-muted lead">Date: {getCurrentDate()}</p>
                                        </div>
                                    </div>
                                    <hr className="mt-3 mb-4"/>
                                    <div className="row justify-content-start">
                                        <div className="col type">
                                            <p className="type-title">Patient</p>
                                            {orderInfo.patientName}
                                        </div>
                                        <div className="col type">
                                            <p className="type-title">Medical Clinic</p>
                                            {selectedClinic !== null ? selectedClinic.name : ""}
                                        </div>
                                        <div class="w-100"></div>
                                        <div className="col type">
                                            <p className="type-title">Patient insurance plan</p>
                                            {orderInfo.patientInsurancePlan}
                                        </div>
                                        <div className="col type">
                                            <p className="type-title">Patient insurance number</p>
                                            {orderInfo.patientInsuranceNumber}
                                        </div>
                                    </div>
                                    <hr className="mt-3 mb-5"/>
                                    <p className="card-text text-center h5">
                                        Study type: {orderInfo.studyType}
                                    </p>
                                    <p className="card-text text-center">{orderInfo.orderDescription}</p>
                                    <hr className="mt-5 mb-4"/>
                                    <div className="media">
                                        <div className="media-body">
                                            <h5 className="mt-0 mb-1 text-center">{medicName}</h5>
                                            <p className="text-center">M.N.: {orderInfo.medicLicenceNumber}</p>
                                        </div>
                                        <img
                                            className="align-self-end ml-3 signature"
                                            alt="medic's signature"
                                            style={{width: "5rem;", maxHeight: "5em;"}}
                                            src="https://picsum.photos/200"
                                        />
                                    </div>
                                </div>

                            </div>
                            <a onClick={changeToClinicStep} className="btn btn-secondary mt-4 mb-4 float-left" role="button">Back</a>
                            <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="verifySubmit"
                                    value={verifySubmit}
                            >Submit order</Button>
                        </Form>
                    </div>
                </div>

            </div>
        </div>

    );

}

export default CreateOrder;