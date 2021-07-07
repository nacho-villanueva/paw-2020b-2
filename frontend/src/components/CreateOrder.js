import {Form, Button, Table, Collapse, Pagination, Spinner, Alert} from "react-bootstrap";
import {useState, useEffect, useCallback} from "react";
import {useHistory} from "react-router-dom";
import { Trans, useTranslation } from 'react-i18next'
import {GetLoggedMedic, GetStudyTypes, QueryClinics, CreateMedicalOrder, FindPatient} from "../api/Auth";

import "./Style/CreateOrder.css";
import { store } from "../redux";
import { ERROR_CODES } from "../constants/ErrorCodes"
import InvalidFeedback from "./InvalidFeedback.js";
import { getAuthorizedImage, getValueFromEvent } from "../api/utils";
import { getAllInsurancePlans } from "../api/CustomFields";
import { GetIdentificationByURL } from "../api/UserInfo";
import { InputsSearch } from "./search_clinic/utils";

function CreateOrder(){

    const { t } = useTranslation();

    const history = useHistory();

    const [errors, setErrors] = useState([]);
    const [statusCode, setStatusCode] = useState(0);
    const [orderStatus, setOrderStatus] = useState({
        code: '',
        id: ''
    });


    /*******************************
    **DATA
    *******************************/
    const infoSubmit="bubba";
    const clinicSubmit="newgrounds forever";
    const verifySubmit="this man... this is a wonderful man";
    //DUMMY DATA (in the real version these should be made with useState to update and render the site)
    /*
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
    */
    const daysOfTheWeek = [
        {name:"Sunday", id: 0},
        {name:"Monday", id: 1},
        {name:"Tuesday", id: 2},
        {name:"Wednesday", id: 3},
        {name:"Thursday", id: 4},
        {name:"Friday", id: 5},
        {name:"Saturday", id: 6}
    ];
    /*
    *! NEED THAT END POINT!!!!
    */
    const [insurancePlans, setInsurancePlans] = useState([
        {name:'None (insert SS number)'},
        {name:'Galeno Azul'},
        {name:'OSDE 4200'},
        {name:'Brook 9100'},
        {name:'OSDE'}
    ]);





    const [orderInfo, setOrderInfo] = useState({
        medicName: '',
        patientName: '',
        patientEmail: '',
        clinicId: '',
        patientInsurancePlan: '',
        patientInsuranceNumber: '',
        studyType: '',
        orderDescription:'',
        medicLicenceNumber: '',
        medicEmail: '',
        identificationSrc: '',
    });

    const [studyTypes, setStudyTypes] = useState([{name:'empty', id: -1}]);

    //used for parsing and sending queries to API
    const [searchFilters, setSearchFilters] = useState({
        clinicName: '',
        plan: '',
        studyType: '',
        days: new Array(7),
        fromTime: new Array(7),
        toTime: new Array(7)
        /*availability: [
            {day: 0, 'from-time': '', 'to-time': ''},
        ]*/
    });

    //used for saving the filters' states for the frontend
    const [searchInputs, setSearchInputs] = useState({
        clinicName: '',
        insurancePlan: '',
        studyType: '',
        schedule: [
            {day:0, checked: true, fromTime: '', toTime: ''},
            {day:1, checked: true, fromTime: '', toTime: ''},
            {day:2, checked: true, fromTime: '', toTime: ''},
            {day:3, checked: true, fromTime: '', toTime: ''},
            {day:4, checked: true, fromTime: '', toTime: ''},
            {day:5, checked: true, fromTime: '', toTime: ''},
            {day:6, checked: true, fromTime: '', toTime: ''}
        ]
    })

    const [clinicsList, setClinicsList] = useState([{userId: 0, name: ''}])

    const getCurrentDate = () => {
        let today = new Date();
        return today.toDateString();
    };


    /*******************************
    **STEPPER STATES
    *******************************/

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


    /*******************************
    **QUERIES
    *******************************/

    //used for calling the medic's info
    const [query, setQuery] = useState("redux");
    const [data, setData] = useState(null); //test if i'm not making a mistake
    const [count, setCount] = useState(1);
    const [image, setImage] = useState(undefined);

    useEffect(async () => {
        const fetchData = async () => {
            await GetLoggedMedic(orderInfo, setOrderInfo, count, setCount, setStatusCode, setErrors);
            await GetStudyTypes(setStudyTypes, count, setCount);
            await getAllInsurancePlans(setInsurancePlans);
        };

        fetchData();
    }, [, query]);

    const fetchIdentification = useCallback(async () => {
        if(orderInfo.identificationSrc !== undefined){
            GetIdentificationByURL(orderInfo.identificationSrc).then((r) => {setImage(r)});
        }
    })

    //getting identification....
    useEffect(() => {
        if(orderInfo !== undefined){
            fetchIdentification().then((r) => {setCount(count+1)});
        }
    }, [orderInfo]);


    const [patientInfo, setPatientInfo] = useState({
        name: '',
        email: '',
        insurance: {
            number: '',
            plan: ''
        },
        error: false,
        loading: false
    })
    const lookUpPatientByEmail = (event) => {
        let patientEmail = event.target.value;
        let aux = patientInfo;
        aux.loading = true;
        setPatientInfo(aux)
        setCount(count+1);
        FindPatient(patientEmail, count, setCount, aux, setPatientInfo);
        //uhhhh
    }




    const closePatientInfoAlert = () => {
        let pat = patientInfo;
        pat.error = false;
        setPatientInfo(pat);
        setCount(count +1);
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
    const searchClinics = () => {
        InputsSearch(searchFilters, setSearchFilters, searchInputs, setSearchInputs, setClinicsList, count, setCount, setTotalClinicPages, setStatusCode, setErrors);

        setClinicSearchValidated(true);
    };

    const changePageAndFetch = (pageNumber) => {
        //will fetch clinics based on the already picked filters
        //only will change the currentPage value (and might update totalClinicPages if the fetch comes up with a different value for that)
        QueryClinics(searchFilters, setClinicsList, count, setCount, pageNumber, setTotalClinicPages, setStatusCode, setErrors);
        setCurrentPage(pageNumber);
    };


    /*******************************
    **CUSTOM COMPONENTS
    *******************************/
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
            <li className="nav-item" key={props.clinic.name}>
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
        const id = props.item.userId

        const[showSchedule, setShowSchedule] = useState(false);
        const[showPlans, setShowPlans] = useState(false);
        const[showStudies, setShowStudies] = useState(false);

        return(
            <div
                className="tab-pane tab-result"
                key={"clinicInfo_" + id}
            >
                <h3>{props.item.name}</h3>
                <Table className="table table-borderless">
                    <tbody>
                        <tr>
                            <td><Trans t={t} i18nKey="create-order.clinic-info.email"/></td>
                            <td className="output">{props.item.email}</td>
                        </tr>
                        <tr>
                            <td><Trans t={t} i18nKey="create-order.clinic-info.telephone"/></td>
                            <td className="output">{props.item.telephone}</td>
                        </tr>
                        <tr>
                            <td>
                            <Button
                                variant="secondary"
                                onClick={() => {setShowSchedule(!showSchedule);}}
                            >
                                {t("create-order.clinic-info.open-hours")}
                                {showSchedule===false? <i className="fas fa-chevron-down ml-2"/> : <i className="fas fa-chevron-up ml-2"/>}
                            </Button>
                            </td>
                            <Collapse in={showSchedule}>
                                <td>
                                    {props.item.hours.map((piano) => (
                                        <div key={"oh_"+id+"_"+piano.day}>
                                            <span>{t('days.day-'+piano.day)}</span>&nbsp;&nbsp;&nbsp;
                                            <span>{piano.openTime + " - " + piano.closeTime}</span>
                                        </div>
                                    ))}
                                </td>
                            </Collapse>
                        </tr>
                        <tr>
                            <td>
                                <Button
                                    variant="secondary"
                                    onClick={() => {setShowPlans(!showPlans);}}
                                >
                                    {t("create-order.clinic-info.insurance")}
                                    {showPlans===false? <i className="fas fa-chevron-down ml-2"/> : <i className="fas fa-chevron-up ml-2"/>}
                                </Button>
                            </td>
                            <Collapse in={showPlans}>
                                <td className="output">
                                    {props.item.acceptedPlans.map((pico) => (
                                        <span
                                            key={"plan_"+ id +"_"+pico.id}
                                            className="badge-sm badge-pill badge-secondary mr-1 d-inline-block"
                                        >{pico.plan}</span>

                                    ))}
                                </td>
                            </Collapse>
                        </tr>
                        <tr>
                            <td>
                                <Button
                                    variant="secondary"
                                    onClick={() => {setShowStudies(!showStudies);}}
                                >
                                    {t("create-order.clinic-info.studies")}
                                    {showStudies===false? <i className="fas fa-chevron-down ml-2"/> : <i className="fas fa-chevron-up ml-2"/>}
                                </Button>
                            </td>
                            <Collapse in={showStudies}>
                                <td className="output">
                                    {props.item.medicalStudies.map((study) => (
                                        <p key={"study_"+ id +"_"+study.name}>{study.name}</p>
                                    ))}
                                </td>
                            </Collapse>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }

    const Day = (props) => {
        return (
            <tr>
                <th><Trans t={t} i18nKey={"days.day-"+props.item.id} >{props.item.name}</Trans></th>
                <th>
                    <Form.Group controlId={"isAvailable" + props.item.id}>
                        <Form.Control defaultChecked={searchInputs.schedule[props.item.id].checked}
                            type="checkbox" name={"isAvailable" + props.item.id}
                            onChange={(val) => {
                                let aux = searchInputs.schedule;
                                aux[props.item.id].checked = val.target.checked
                                setSearchInputs({...searchInputs, schedule: aux})
                            }}
                        />
                    </Form.Group>
                </th>
                <th>
                    <Form.Group controlId={"day-" + props.item.id + "-ot"}>
                        <Form.Control
                            type="time" className="form-control time-input"
                            placeholder="00:00" maxLength="5"
                            name={"day-" + props.item.id + "-ot"}
                            defaultValue={searchInputs.schedule[props.item.id].fromTime}
                            pattern="[0-9]{2}:[0-9]{2}"
                            onChange={(val) => {
                                let aux = searchInputs.schedule;
                                aux[props.item.id].fromTime = val.target.value
                                setSearchInputs({...searchInputs, schedule: aux})
                            }}
                        />
                    </Form.Group>
                </th>
                <th>
                    <Form.Group controlId={"day-" + props.item.id + "-ct"}>
                        <Form.Control
                            type="time" className="form-control time-input"
                            placeholder="00:00" maxLength="5"
                            name={"day-" + props.item.id + "-ct"}
                            defaultValue={searchInputs.schedule[props.item.id].toTime}
                            pattern="[0-9]{2}:[0-9]{2}"
                            onChange={(val) => {
                                let aux = searchInputs.schedule;
                                aux[props.item.id].toTime = val.target.value
                                setSearchInputs({...searchInputs, schedule: aux})
                            }}
                        />
                    </Form.Group>
                </th>
            </tr>
        );
    }



    /*******************************
    **CLIENT-SIDE FORM VALIDATION
    *******************************/
    //step 1 (order info) form validation
    const [infoValidated, setInfoValidated] = useState(false);
    const handleInfoSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            console.log("event", event);
            let aux = orderInfo;
            aux.patientEmail = getValueFromEvent("patientEmail", event);
            aux.patientName = getValueFromEvent("patientName", event);
            aux.patientInsurancePlan = getValueFromEvent("patientInsurancePlan", event);
            aux.patientInsuranceNumber = getValueFromEvent("patientInsuranceNumber", event);
            aux.studyType = getValueFromEvent("studyType", event);
            aux.orderDescription = getValueFromEvent("orderDescription", event);
            setOrderInfo(aux);
            console.log("orderInfo", orderInfo)

            let searchInputsAux = searchInputs;
            searchInputsAux.insurancePlan = orderInfo.patientInsurancePlan;
            searchInputsAux.studyType = orderInfo.studyType;
            setSearchInputs(searchInputsAux)

            QueryClinics({plan: orderInfo.patientInsurancePlan, studyType: orderInfo.studyType, clinicName: ""}, setClinicsList, count, setCount, 1, setTotalClinicPages, setStatusCode, setErrors);
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
            let aux = orderInfo;
            let idx = studyTypes.findIndex(s => s.name === aux.studyType);
            if(idx !== -1){
                aux.studyTypeId = studyTypes[idx].id;
                setOrderInfo(aux);
            }
            //console.log(orderInfo);
            CreateMedicalOrder(orderInfo, setStatusCode, setErrors, setOrderStatus);
            //now it should send the order to the API and redirect the user to /view-study
        }

        setVerifyValidated(true);
    };

    useEffect(() => {
        if(orderStatus.code === 201){
            history.push('/dashboard/view-study/'+orderStatus.id)
        }
    }, [orderStatus]);



    return(
        <div className="row justify-content-center" key={"create-order_"+ count}>
            <div className={"card form-card " + (activeClinicStep === "active" ? "clinic" : "")}>

                <div className="stepper-wrapper-horizontal">
                    <div className="step-wrapper">
                        <div className={"step-number " + activeOrderStep}>1</div>
                        <div className={"step-description text-center " + activeOrderStep}>
                            <Trans t={t} i18nKey="create-order.steps.step-1.title" />
                        </div>
                        <div className="divider-line"></div>
                    </div>
                    <div className="step-wrapper">
                        <div className={"step-number " + activeClinicStep}>2</div>
                        <div className={"step-description text-center " + activeClinicStep}>
                            <Trans t={t} i18nKey="create-order.steps.step-2.title" />
                        </div>
                        <div className="divider-line"></div>
                    </div>
                    <div className="step-wrapper">
                        <div className={"step-number " + activeVerifyStep}>3</div>
                        <div className={"step-description text-center " + activeVerifyStep}>
                            <Trans t={t} i18nKey="create-order.steps.step-3.title" />
                        </div>
                    </div>
                </div>
                <div className="row justify-content-center">
                    <hr className="divider spacer"/>
                </div>


                <div className="tab-content mt-9">
                    <div id="info-form" className={"custom-form tab-pane fade in " + orderStep}>
                            <Form noValidate validated={infoValidated} onSubmit={handleInfoSubmit}>
                                <Form.Group>
                                    <Form.Label className="text-muted"><Trans t={t} i18nKey="create-order.steps.step-1.form.medic.label" /></Form.Label>
                                    <p className="lead mb-0">{orderInfo.medicName}</p>
                                    <Form.Control
                                            required type="text"
                                            name="medicName" readOnly="true"
                                            value={orderInfo.medicName}
                                            className="custom-hidden"
                                    />
                                </Form.Group>
                                <hr className="divider mt-0"/>
                                <div className="justify-content-center">
                                    <Alert show={patientInfo.error} variant="warning">
                                        <div className="d-flex justify-content-between">
                                        <span className="my-2">
                                            <Trans t={t} i18nKey="create-order.steps.step-1.form.patient-email.errors.missing.message" />
                                        </span>
                                        <Button
                                            variant="outline-warning"
                                            onClick={() => {closePatientInfoAlert()}}
                                        >
                                            <Trans t={t} i18nKey="create-order.steps.step-1.form.patient-email.errors.missing.close-alert" />
                                        </Button>
                                        </div>

                                    </Alert>
                                </div>
                                <div className="row mx-1">
                                    <Form.Group className="form-group col" controlId="patientEmail">
                                        <Form.Label className="bmd-label-floating"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-email.label" /></Form.Label>
                                        <Form.Control
                                            required type="email"
                                            name="patientEmail"
                                            defaultValue={patientInfo.email}
                                            onBlur={(e) => {lookUpPatientByEmail(e);}}
                                        />
                                        {patientInfo.loading ?
                                        <Spinner
                                            as="span" variant="primary"
                                            animation="border"
                                            size="sm" role="status"
                                        /> : <></>}

                                        <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-email.errors.invalid" /></Form.Control.Feedback>
                                        <InvalidFeedback
                                            condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.INVALID && e.property === "patientEmail"}).length>0}
                                            message={t("create-order.steps.step-1.form.patient-email.errors.invalid")}
                                        />
                                        <InvalidFeedback
                                            condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "patientEmail"}).length>0}
                                            message={t("create-order.steps.step-1.form.patient-email.errors.missing-field")}
                                        />
                                    </Form.Group>
                                </div>

                                <div className="row mx-1">
                                    <Form.Group className="form-group col" controlId="patientName">
                                        <Form.Label className="bmd-label-floating"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-name.label" /></Form.Label>
                                        <Form.Control
                                            required type="text"
                                            name="patientName"
                                            defaultValue={patientInfo.name}
                                        />
                                        <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-name.errors.missing-field" /></Form.Control.Feedback>
                                        <InvalidFeedback
                                            condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "patientName"}).length>0}
                                            message={t("create-order.steps.step-1.form.patient-name.errors.missing-field")}
                                        />
                                    </Form.Group>
                                </div>
                                <div className="row mx-1">
                                    <Form.Group className="form-group col" controlId="patientInsurancePlan">
                                        <Form.Label className="bmd-label-floating"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-insurance-plan.label" /></Form.Label>
                                        <Form.Control
                                            required as="select"
                                            name="patientInsurancePlan"
                                        >
                                            {insurancePlans.map((item) => (
                                                <option selected={item.name === patientInfo.insurance.plan}>{item.name}</option>
                                            ))}
                                        </Form.Control>
                                        <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-insurance-plan.errors.missing-field" /></Form.Control.Feedback>
                                    </Form.Group>
                                    <Form.Group className="form-group col" controlId="patientInsuranceNumber">
                                        <Form.Label className="bmd-label-floating"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-insurance-number.label" /></Form.Label>
                                        <Form.Control
                                            required type="text"
                                            name="patientInsuranceNumber"
                                            className="mt-2"
                                            defaultValue={patientInfo.insurance.number}
                                        />
                                        <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="create-order.steps.step-1.form.patient-insurance-plan.errors.missing-field" /></Form.Control.Feedback>
                                    </Form.Group>

                                    <Alert show={statusCode === 400 && errors.filter(e => {return e.code === ERROR_CODES.INVALID && e.property === "medicPlan"}).length>0 && "true"} variant="warning">
                                        <div className="d-flex justify-content-between">
                                        <span className="my-2">
                                            <Trans t={t} i18nKey="create-order.steps.step-1.form.patient-insurance-plan.errors.invalid.message" />
                                        </span>
                                        <Button
                                            variant="outline-warning"
                                            onClick={() => {setStatusCode(0)}}
                                        >
                                            <Trans t={t} i18nKey="create-order.steps.step-1.form.patient-insurance-plan.errors.invalid.close-alert" />
                                        </Button>
                                        </div>
                                    </Alert>
                                </div>
                                <hr className="mt-3 mb-2"/>
                                <div className="row mx-1">
                                    <div className="col">
                                        <Form.Group className="form-group" controlId="studyType">
                                            <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="create-order.steps.step-1.form.study-type.label" /></Form.Label>
                                            <Form.Control
                                                required as="select"
                                                name="studyType"
                                                placeholder={t("create-order.steps.step-1.form.study-type.placeholder")}
                                            >
                                                {studyTypes.map((item) => (
                                                    <option>{item.name}</option>
                                                ))}
                                            </Form.Control>
                                            <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="create-order.steps.step-1.form.study-type.errors.missing-field" /></Form.Control.Feedback>
                                            <InvalidFeedback
                                                condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "studyType"}).length>0}
                                                message={t("create-order.steps.step-1.form.study-type.errors.missing-field")}
                                            />
                                        </Form.Group>
                                    </div>
                                </div>
                                <div className="col">
                                    <Form.Group controlId="orderDescription">
                                        <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="create-order.steps.step-1.form.description.label" /></Form.Label>
                                        <textarea
                                            type="text"
                                            name="orderDescription"
                                            rows="10" className="form-control area-custom"
                                        />
                                        <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="create-order.steps.step-1.form.description.errors.missing-field" /></Form.Control.Feedback>
                                    </Form.Group>
                                </div>
                                <hr className="mt-3 mb-2"/>

                                <a onClick={history.goBack} className="btn btn-secondary mt-4 mb-4 float-left" role="button"><Trans t={t} i18nKey="create-order.buttons.cancel" /></a>

                                <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="infoSubmit"
                                    value="infoSubmit"
                                ><Trans t={t} i18nKey="create-order.buttons.next" /></Button>
                            </Form>
                    </div>

                    <div id="clinic-form" className={"custom-form tab-pane fade in " + clinicStep}>
                        <Form noValidate validated={clinicSearchValidated} onSubmit={searchClinics}>
                            <div className="search-block">
                                <div className="row mx-1 pt-2">
                                    <Form.Group className="form-group col mt-1" controlId="clinicName">
                                        <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="create-order.steps.step-2.form.clinic-name.label" /></Form.Label>
                                        <Form.Control
                                            type="text" style={{paddingTop: "10px"}}
                                            name="clinicName" defaultValue={searchInputs.clinicName}
                                            onChange={(val) => {setSearchInputs({...searchInputs, clinicName: val.target.value})}}
                                        />
                                    </Form.Group>
                                    <Form.Group className="form-group col" controlId="insurancePlan">
                                        <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="create-order.steps.step-2.form.insurance-plan.label" /></Form.Label>
                                        <Form.Control
                                            as="select"
                                            name="insurancePlan"
                                            onChange={(val) => {setSearchInputs({...searchInputs, insurancePlan: val.target.value})}}
                                        >
                                            {insurancePlans.map((item) => (
                                                <option selected={item.name === searchInputs.insurancePlan} value={item.id}>{item.name}</option>
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
                                        {t("create-order.steps.step-2.form.available-hours.buttons.show")}
                                        {show===false? <i className="fas fa-chevron-down ml-2"/> : <i className="fas fa-chevron-up ml-2"/>}
                                    </Button>
                                    <Button
                                        className="clinic-btn search-btn mx-auto"
                                        name="clinicSearchSubmit"
                                        value="create-order.steps.step-2.form.submit.value"
                                        onClick={(e)=> {
                                            e.preventDefault();
                                            searchClinics();
                                        }}
                                    >
                                        <Trans t={t} i18nKey="create-order.steps.step-2.form.submit.value" />
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
                                                        <th><Trans t={t} i18nKey="create-order.steps.step-2.form.available-hours.columns.day" /></th>
                                                        <th><Trans t={t} i18nKey="create-order.steps.step-2.form.available-hours.columns.available" /></th>
                                                        <th><Trans t={t} i18nKey="create-order.steps.step-2.form.available-hours.columns.from-time" /></th>
                                                        <th><Trans t={t} i18nKey="create-order.steps.step-2.form.available-hours.columns.to-time" /></th>
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
                                    <p className="card-title h4"><Trans t={t} i18nKey="create-order.steps.step-2.form.results.title" /></p>
                                    <hr/>
                                    <div className="d-flex flex-row">
                                        <div id="results" className="list-group result-section">
                                            {clinicsList.length === 0 ? <h3 className="text-center py-5 lead"><Trans t={t} i18nKey="create-order.steps.step-2.form.results.no-results" /></h3>
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
                                            <h5 class="text-muted"><Trans t={t} i18nKey="create-order.steps.step-2.form.results.selected" /></h5>
                                            <div className="">
                                                {selectedClinic === null ? <h4><Trans t={t} i18nKey="create-order.steps.step-2.form.results.no-selected" /></h4> : <ClinicInfo key={"selected_" + selectedClinic.userId} item={selectedClinic}/>}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <Alert show={statusCode === 400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING && e.property === "clinicId"}).length>0 && "true"} variant="warning">
                                <div className="d-flex justify-content-between">
                                <span className="my-2">
                                    <Trans t={t} i18nKey="create-order.steps.step-2.form.errors.missing" />
                                </span>
                                <Button
                                    variant="outline-warning"
                                    onClick={() => {closePatientInfoAlert()}}
                                >
                                    <Trans t={t} i18nKey="create-order.steps.step-2.form.errors.close-alert" />
                                </Button>
                                </div>
                            </Alert>

                            <Alert show={statusCode === 400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "clinicId"}).length>0 && "true"} variant="warning">
                                <div className="d-flex justify-content-between">
                                <span className="my-2">
                                    <Trans t={t} i18nKey="create-order.steps.step-2.form.errors.missing-field" />
                                </span>
                                <Button
                                    variant="outline-warning"
                                    onClick={() => {closePatientInfoAlert()}}
                                >
                                    <Trans t={t} i18nKey="create-order.steps.step-2.form.errors.close-alert" />
                                </Button>
                                </div>
                            </Alert>

                            <a onClick={changeToOrderInfoStep} className="btn btn-secondary mt-4 mb-4 float-left" role="button"><Trans t={t} i18nKey="create-order.buttons.prev" /></a>
                            <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="clinicSubmit"
                                    value="clinicSubmit"
                            ><Trans t={t} i18nKey="create-order.buttons.next" /></Button>
                        </Form>
                    </div>

                    <div id="verify-form" className={"custom-form tab-pane fade in " + verifyStep}>
                        <Form noValidate validation={verifyValidated} onSubmit={handleVerifySubmit}>
                            <div className="card">
                                <div className="card-body">
                                    <div className="row">
                                        <div className="col">
                                            <p className="card-title ml-3 h4"><Trans t={t} i18nKey="create-order.steps.step-3.form.title" /></p>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col">
                                            <p className="card-subtitle ml-3 text-muted lead"><Trans t={t} i18nKey="create-order.steps.step-3.form.date" values={{today: getCurrentDate()}} /></p>
                                        </div>
                                    </div>
                                    <hr className="mt-3 mb-4"/>
                                    <div className="row justify-content-start">
                                        <div className="col type">
                                            <Trans t={t} i18nKey="create-order.steps.step-3.form.patient" values={{patientName: orderInfo.patientName}} >
                                                <p className="type-title">Patient</p>
                                                {orderInfo.patientName}
                                            </Trans>
                                        </div>
                                        <div className="col type">
                                            <Trans t={t} i18nKey="create-order.steps.step-3.form.clinic" values={{clinicName: selectedClinic !== null ? selectedClinic.name : ""}} >
                                                <p className="type-title">Medical Clinic</p>
                                                {selectedClinic !== null ? selectedClinic.name : ""}
                                            </Trans>
                                        </div>
                                        <div className="w-100"></div>
                                        <div className="col type">
                                            <Trans t={t} i18nKey="create-order.steps.step-3.form.patient-insurance-plan" values={{patientInsurancePlan: orderInfo.patientInsurancePlan}} >
                                                <p className="type-title">Patient insurance plan</p>
                                                {orderInfo.patientInsurancePlan}
                                            </Trans>
                                        </div>
                                        <div className="col type">
                                            <Trans t={t} i18nKey="create-order.steps.step-3.form.patient-insurance-number" values={{patientInsuranceNumber: orderInfo.patientInsuranceNumber}} >
                                                <p className="type-title">Patient insurance number</p>
                                                {orderInfo.patientInsuranceNumber}
                                            </Trans>
                                        </div>
                                    </div>
                                    <hr className="mt-3 mb-5"/>
                                    <p className="card-text text-center h5">
                                        <Trans t={t} i18nKey="create-order.steps.step-3.form.study-type" values={{studyType: orderInfo.studyType}} />
                                    </p>
                                    <p className="card-text text-center">{orderInfo.orderDescription}</p>
                                    <hr className="mt-5 mb-4"/>
                                    <div className="media">
                                        <div className="media-body">
                                            <Trans t={t} i18nKey="create-order.steps.step-3.form.medic-id" values={{medicName: orderInfo.medicName, medicLicence: orderInfo.medicLicenceNumber}} >
                                                <h5 className="mt-0 mb-1 text-center">{orderInfo.medicName}</h5>
                                                <p className="text-center">M.N.: {orderInfo.medicLicenceNumber}</p>
                                            </Trans>
                                        </div>
                                        <img
                                            className="align-self-end ml-3 signature"
                                            alt="medic's signature"
                                            src={image}
                                            key={"img-"+count}
                                        />
                                    </div>
                                </div>

                            </div>
                            <a onClick={changeToClinicStep} className="btn btn-secondary mt-4 mb-4 float-left" role="button"><Trans t={t} i18nKey="create-order.buttons.prev" /></a>
                            <Button className="create-btn mt-4 mb-2 float-right"
                                    type="submit" name="verifySubmit"
                                    value="verifySubmit"
                            ><Trans t={t} i18nKey="create-order.buttons.submit" /></Button>
                        </Form>
                    </div>
                </div>

            </div>
        </div>

    );

}

export default CreateOrder;