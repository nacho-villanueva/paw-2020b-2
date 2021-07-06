import {Form, Button, Table, Collapse, Pagination, Spinner, Alert} from "react-bootstrap";
import {useState, useEffect, useCallback} from "react";
import {useHistory} from "react-router-dom";
import {GetLogguedUser, GetOrderInfo, GetResults} from "../api/Auth";

import "./Style/ViewOrder.css";
import { store } from "../redux";
import { Roles } from "../constants/Roles";
import {useSelector} from "react-redux";

import {UploadResults} from "./vieworder_components/UploadResults";
import {getFilesFrom, getValueFromEvent, convertToBase64, getAuthorizedImage} from "../api/utils";
import { UploadResult } from "../api/Results";

import {ImageDataContainer} from "./vieworder_components/ImageDataContainer";

function ViewOrder(){
    const roleType = useSelector(state => state.auth.role);
    var orderId = window.location.href.split('/');
    orderId = orderId[orderId.length - 1];

    const imageAssets = "http://localhost:8080/";
    const changeClinicPath = "http://localhost:8080/";
    const shareOrderPath = "http://localhost:8080/";
    const uploadPath = "http://localhost:8080/";

    const [count, setCount] = useState(0);
    const [orderInfo, setOrderInfo] = useState({
        id: '',
        date: '',
        medicPlan: {
            name:'',
            number:''
        },
    });
    const [results, setResults] = useState([]);
    const [userInfo, setUserInfo] = useState({role: ''});

    const fetchData =useCallback( async () => {
        //await GetLogguedUser(userInfo, setUserInfo, count, setCount);
        await GetOrderInfo(orderId, orderInfo, setOrderInfo,count, setCount);
        await GetResults(orderId, setResults);
    }, []);

    useEffect(() => {
        fetchData().then(setCount(count +1));
    }, []);

    const [image, setImage] = useState(undefined);

    const fetchIdentification = useCallback(async () => {
        if(orderInfo.identification !== undefined){
            console.log("identification", orderInfo.identification);
            setImage(await getAuthorizedImage(orderInfo.identification));
            console.log("IMAGE", image);
        }
     }, []);


    useEffect(() => {
        fetchIdentification().then((r) => {setCount(count +1)});
    }, [orderInfo]);

    const [selectedResult, setSelectedResult] = useState(null);
    const [viewCount, setViewCount] = useState(0);
    const [resultViewCount, setResultViewCount] = useState(0);

    const [statusCode, setStatusCode] = useState(0);

    const [showUploadModal, setShowUploadModal] = useState(false);
    const [uploadValidated, setUploadValidated] = useState(false);

    /*
    *!TODO: form validation before doing anything!!!!
    */
    const handleUploadResults = (event) => {
        event.preventDefault();
        console.log(event);
        let files = getFilesFrom("fileUpload",event);
        console.log("AH",files);
        let aux = {responsibleName:'', responsibleLicenceNumber: '', responsibleIdentification:''};
        aux.responsibleName = getValueFromEvent("responsibleMedicName", event);
        aux.responsibleLicenceNumber = getValueFromEvent("responsibleMedicLicenceNumber", event);
        aux.responsibleIdentification  = getFilesFrom("signatureUpload", event)[0];

        for(let idx in files){
            let out = {
                responsibleName:'',
                responsibleLicenceNumber: '',
                responsibleIdentification:
                    {image:'', contentType:''},
                responseFile:
                    {image:'', contentType:''},
                id: orderId
            };
            out.responsibleName = aux.responsibleName;
            out.responsibleLicenceNumber = aux.responsibleLicenceNumber;

            out.responsibleIdentification.image =convertToBase64(aux.responsibleIdentification);
            out.responseFile.image = convertToBase64(files[idx]);

            let auxExtension = aux.responsibleIdentification.name.split('.');
            out.responsibleIdentification.contentType="image/"+auxExtension[auxExtension.length - 1];

            auxExtension = files[idx].name.split('.');
            out.responseFile.contentType = "image/"+auxExtension[auxExtension.length - 1];

            UploadResult(out, setStatusCode);
        }
    }

    useEffect(() =>{
        if(statusCode === 201){
            setShowUploadModal(false);
            GetResults(orderId, setResults);
        }
    }, [statusCode]);


    const ResultCardView = (props) => {
        return(
            <div
                id={"res-"+viewCount}   key={"resultView_"+resultViewCount}
                style={{height: "100%"}}
            >
                {props.item !== null ?
                    <div className="card overflow-auto border-primary result-card-view" key={"resCardView_"+resultViewCount}>
                        <div className="card-body">
                            <div className="row justify-content-start">
                                <div className="col type">
                                    <p className="type-title">Date:</p> {props.item.date}
                                </div>
                            </div>

                            <hr className="mt-3 mb-4 text-center"/>
                            <section className="row justify-content-center" style={{height: "45%"}}>
                                <img
                                    src={props.item.file}
                                    className="result-image align-self-center"
                                    alt="study result"
                                />

                    <ImageDataContainer
                        src={props.item.file}
                        alt={"medic's signature"}
                        id={'order'}
                    />
                            </section>

                            <hr className="mt-3 mb-4"/>
                            <div className="media">
                                <div className="media-body">
                                    <h5 className="mt-0 mb-1 text-center">{props.item.responsibleName}</h5>
                                    <p className="text-center">M.N.: {props.item.responsibleLicenceNumber}</p>
                                </div>
                                <img
                                    className="ml-1"
                                    alt="responsible's signature"
                                    src={props.item.identification}
                                    style={{maxHeight:"5em"}}
                                />
                            </div>
                        </div>
                    </div>
                :
                <div className="align-items-end result-not">
                    <h1 className="text-center mt-5 py-5">Select a result</h1>
                </div>
                }


            </div>
        );
    }

    const ResultItem = (props) => {
        const result = props.result;
        return(
            <li className="nav-item result-item">
                <Button
                    className="nav-link" id={"res-"+props.index+"-tab"}
                    data-toggle="tab" role="tab" style={{cursor:"pointer"}}
                    onClick={(e)=>{e.preventDefault(); setSelectedResult(props.result); setResultViewCount(resultViewCount+1);}}
                    type="button"
                >
                    <div className="d-flex w-100 justify-content-between">
                        <p className="type-title">Date {result.date}</p>
                        </div>
                    <p>{result.responsibleName}</p>
                </Button>
            </li>

        );
    }

    const ResultsViewer = () => {
        return(
            <div className="row"
            key={"resultViewer_"+viewCount}>
                <div
                    className="col-4 float-left"
                    style={{overflowY:"scroll", overflowX:"hidden", height:"32em"}}
                >
                    <ul className="nav flex-column w-100 ul-results" id="myTab" role="tablist">
                        {results.map((item, index) => (
                            <ResultItem result={item} index={index} key={"resultItem_"+index}/>
                        ))}
                    </ul>
                </div>
                <div className="col-8 float-right">
                    <div className="tab-content" style={{height: "100%"}}>
                        {
                            <ResultCardView  item={selectedResult}/>
                        }
                    </div>
                </div>
            </div>
        );
    }

    const NoResults = () => {
        return(
            <section>
                <div className="align-items-end result-not">
                    <h1 className="text-center mt-5 py-5">No Results Found For This Medical Order</h1>
                </div>
                {userInfo.role === 'PATIENT' ?
                    <div className="content-align-center">
                        <h4 className="text-center mt-5 pt-5">
                            Change Clinic
                        </h4>
                        <a
                            href={changeClinicPath} role="button"
                            className="btn upload-btn" type="button"
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

    const OrderInfoCard = (props) => {
        return (
            <div className="card order-card bg-light float-right">
                <div className="row mt-4" key={"orderInfo_"}>
                    <div className="col">
                        <p className="card-title ml-3 h4">Order Number: {orderInfo.id}</p>
                    </div>
                    {userInfo.role === 'PATIENT' ?
                        <div className="col">
                            <div className="row justify-content-end">
                                <a href={shareOrderPath}
                                    className="btn upload-btn mt-0 mb-3 mr-4"
                                    role="button" type="button"
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
                <div className="row justify-content-start mx-4">
                    <div className="col type">
                        <p className="type-title">Patient</p>
                        {orderInfo.patientName}
                    </div>
                    <div className="col type">
                        <p className="type-title">Medical Clinic</p>
                        {orderInfo.clinic}
                    </div>
                    <div className="w-100"></div>
                    <div className="col type">
                        <p className="type-title">Patient insurance plan</p>
                        {/*orderInfo.medicPlan.name*/}
                    </div>
                    <div className="col type">
                        <p className="type-title">Patient insurance number</p>
                        {/*orderInfo.medicPlan.number*/}
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
                        src={image}
                    />
                </div>
            </div>

        );
    }

    const ResultsInfoCard = (props) => {
        return(
            <div className="card results-info-card bg-light float-left">
                <div className="card-body"  key={"results-container_"+count}>
                    <div className="row">
                        <div className="col">
                            <p className="card-title h4">Results</p>
                        </div>
                        <div className="col">
                            <div className="row justify-content-end">
                                {roleType === Roles.CLINIC ?
                                    <Button
                                        role="button" type="button"
                                        className="btn upload-btn mt-0 mb-3 mr-4"
                                        onClick={(e) => {e.preventDefault();setShowUploadModal(true);}}
                                    >
                                        Upload Results
                                    </Button>
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
        );
    }

    return(
        <>
            <div className="row-custom justify-content-center mt-5" key={"view-order_"+count}>
                <OrderInfoCard/>
                <ResultsInfoCard/>
            </div>
            <UploadResults
                show={showUploadModal}
                setShow={setShowUploadModal}
                handleUploadResults={handleUploadResults}
                uploadValidated={uploadValidated}
                orderInfo={orderInfo}
            />
        </>
    )
}
export default ViewOrder;