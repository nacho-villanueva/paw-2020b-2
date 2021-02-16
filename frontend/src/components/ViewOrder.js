import {Form, Button, Table, Collapse, Pagination, Spinner, Alert} from "react-bootstrap";
import {useState, useEffect} from "react";
import {useHistory} from "react-router-dom";
import {GetLoggedMedic, GetStudyTypes, QueryClinics, CreateMedicalOrder, FindPatient} from "../api/Auth";

import "./Style/ViewOrder.css";
import { store } from "../redux";

function ViewOrder(){
    const orderId = window.location.href.split('/').pop();

    const imageAssets = "http://localhost:8080/api??????";

    const [count, setCount] = useState(0);
    const [orderInfo, setOrderInfo] = useState({orderId: '', date: ''});
    const [results, setResults] = useState([{resultId: ''}]);
    const [userInfo, setUserInfo] = useState({role: ''});

    useEffect(async () => {
        const fetchData = async () => {
            await GetLoggedUser(userInfo, setUserInfo, count, setCount);
            await GetOrderInfo(orderId, orderInfo, setOrderInfo, count, setCount);
            await GetResults(orderId, results, setResults, count, setCount);
        };

        fetchData();
    }, []);

    const ResultCardView = (props) => {
        const result = props.result;
        return(
            <div
                id={"res-"+result.id}
                className="tab-pane fade tab-result"
            >
                <div className="card overflow-auto border-primary">
                    <div className="card-body">
                        <div className="row justify-content-start">
                            <div className="col type">
                                <p>Date: {result.date}</p>
                            </div>
                            <div className="col type">
                                <p>Clinic: {orderInfo.clinic}</p>
                            </div>
                        </div>
                        <hr className="mt-3 mb-4 text-center"/>
                        <img
                            src={imageAssets+"/result/"+orderId+"/"+result.id+"?attr=result-data"}
                            className="align-self-end ml-3"
                            alt="result image" id={"resultImage-"+result.id}
                        />
                        <hr className="mt-3 mb-4"/>
                        <div className="media">
                            <div className="media-body">
                                <h5 className="mt-0 mb-1 text-center">{result.responsibleName}</h5>
                                <p className="text-center">M.N.: {result.responsibleLicenceNumber}</p>
                            </div>
                            <img
                                className="ml-1"
                                alt="responsible's signature"
                                src={imageAssets+"/result/"+orderId+"/"+result.id+"?attr=identification"}
                                style={{maxHeight="5em"}} id={"resultAttr-"+result.id}
                            />
                        </div>
                    </div>
                </div>

            </div>
        );
    }

    const ResultItem = (props) => {
        const result = props.result;
        return(
            <aside>
                <li className="nav-item">
                    <a
                        className="nav-link" id={"res-"+result.id+"-tab"}
                        data-toggle="tab" href={"#res-"+result.id} role="tab"
                        aria-controls={"res-"+result.id} aria-selected="false"
                    >
                        <div className="d-flex w-100 justify-content-between">
                            <p className="type-title">Date {result.date}</p>
                            </div>
                        <p>{result.responsibleName}</p>
                    </a>
                </li>
                <br/>
            </aside>
        );
    }

    const ResultsViewer = () => {
        return(
            <div className="row">
                <div
                    className="col-4 float-left"
                    style={{overflowY="scroll", overflowX="hidden", height="32em"}}
                >
                    <ul className="nav flex-column" id="myTab" role="tablist">
                        {results.map((item) => (
                            <ResultItem result={item}/>
                        ))}
                    </ul>
                </div>
                <div className="col-8 float-right">
                    <div className="tab-content">
                        {results.map((item) => (
                            <ResultCardView result={item}/>
                        ))}
                    </div>
                </div>
            </div>
        );
    }

    const NoResults = () => {
        return(
            <section>
                <div className="align-items-end result-not">
                    <h1 className="text-cener mt-5 py-5">No Results Found For This Medical Order</h1>
                </div>
                {userInfo.role === 'PATIENT' ?
                    <div className="content-align-center">
                        <h4 className="text-center mt-5 pt-5">
                            Change Clinic
                        </h4>
                        <a
                            href={changeClinicPath} role="button"
                            className="btn upload-btn"
                        >
                            Change Clinic
                        </a>
                    </div>
                    :
                    <></>
                }
            </section>
        );
    }

    return(
        <div className="row justify-content-center" key={"view-order_" + count}>
            <div className="col-sm-5">
                <div className="card order-card bg-light float-right">
                    <div className="row">
                        <div className="col">
                            <p className="card-title ml-3 h4">Order Number: {orderInfo.orderId}</p>
                        </div>
                        {userInfo.role === 'PATIENT' ?
                            <div className="col">
                                <div className="row justify-content-end">
                                    <a href={shareOrderPath}
                                        className="btn upload-btn mt-0 mb-3 mr-4"
                                        role="button"
                                    >
                                        Share Order
                                    </a>
                                </div>
                            </div> : <></>
                        }
                    </div>
                    <div className="row">
                        <div className="col">
                            <p className="card-subtitle ml-3 text-muted lead">
                                Date: {orderInfo.date}
                            </p>
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
                            {orderInfo.clinic}
                        </div>
                        <div class="w-100"></div>
                        <div className="col type">
                            <p className="type-title">Patient insurance plan</p>
                            {orderInfo.medicPlan.name}
                        </div>
                        <div className="col type">
                            <p className="type-title">Patient insurance number</p>
                            {orderInfo.medicPlan.number}
                        </div>
                    </div>

                    <hr className="mt-3 mb-5"/>

                    <p className="card-text text-center h5">
                        Study type: {orderInfo.studyType}
                    </p>
                    <p className="card-text text-center">{orderInfo.description}</p>
                    <hr className="mt-5 mb-4"/>

                    <div className="media">
                        <div className="media-body">
                            <h5 className="mt-0 mb-1 text-center">{orderInfo.medicName}</h5>
                            <p className="text-center">M.N.: {orderInfo.medicLicenceNumber}</p>
                        </div>
                        <img
                            className="align-self-end ml-3 signature"
                            alt="medic's signature"
                            src={orderInfo.identificationSrc}
                        />
                    </div>

                </div>
            </div>
            <div className="col-sm-6">
                <div className="card results-card float-left">
                    <div className="card-body">
                        <div className="row">
                            <div className="col">
                                <p className="card-title h4">Results</p>
                            </div>
                            <div className="col">
                                <div className="row justify-content-end">
                                    {userInfo.role === "CLINIC" ?
                                        <a
                                            href={uploadPath}   role="button"
                                            className="btn upload-btn mt-0 mb-3 mr-4"
                                        >
                                            Upload Results
                                        </a>
                                        : <></>
                                    }
                                </div>
                            </div>
                        </div>
                        {results.length === 0 ?
                            <NoResults/>
                            :
                            <ResultsViewer/>
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}
export default ViewOrder;