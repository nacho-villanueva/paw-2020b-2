import {Modal, Button} from "react-bootstrap";
import {ClinicsFilters} from "../search_clinic/ClinicsFilters";
import {ClinicsResults} from "../search_clinic/ClinicsResults";

import { useCallback, useEffect, useState } from "react";
import { GetStudyTypes } from "../../api/Auth";
import { getValueFromEvent, getDaySchedule } from "../../api/utils";
import { getAllInsurancePlans } from "../../api/CustomFields";

import { SearchClinics } from "../../api/Clinics";

import { Trans, useTranslation } from 'react-i18next'
import { UpdateOrderClinic } from "../../api/Orders";

import { InputsSearch } from "../search_clinic/utils";

import "../Style/SearchClinics.css";


function ChangeClinic(props){
    const {t} = useTranslation();

    const show = props.show;
    const setShow = props.setShow;
    const orderInfo = props.orderInfo;
    const orderId = props.orderId;
    const showUpdateToast = props.showUpdateToast;

    const [selectedClinic, setSelectedClinic] = useState(null);

    const daysOfTheWeek = [
        {name:"Sunday", id: 0},
        {name:"Monday", id: 1},
        {name:"Tuesday", id: 2},
        {name:"Wednesday", id: 3},
        {name:"Thursday", id: 4},
        {name:"Friday", id: 5},
        {name:"Saturday", id: 6}
    ];

    //what we have to send out
    let sendableFilters = {
        clinicName: '',
        plan: '',
        studyType: '',
        days: new Array(7),
        fromTime: new Array(7),
        toTime: new Array(7)
    };
    const [searchFilters, setSearchFilters] = useState(sendableFilters);

    //used in the FORM
    let defaultInputs = {
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
    };
    const [inputs, setInputs] = useState(defaultInputs);

    const [insurancePlans, setInsurancePlans] = useState([]);
    const [studyTypesList, setStudyTypesList] = useState([]);

    const [clinicsList, setClinicsList] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalClinicPages, setTotalClinicPages] = useState(1);

    const [update, setUpdate] = useState(0);
    const [statusCode, setStatusCode] = useState(0);
    const [errors, setErrors] = useState([]);


    const fetchOptions = useCallback(async () => {
        GetStudyTypes(setStudyTypesList, update, setUpdate);
        await getAllInsurancePlans(setInsurancePlans);
    }, []);

    useEffect(()=>{
        fetchOptions().then((r) => {
            setUpdate((prevState) => {
                let next = prevState + 1;
                return {...prevState, ...next};
            });
        });
    }, []);

    const changePageAndFetch = (pageNumber) => {
        if(pageNumber >= 1 && pageNumber <= totalClinicPages){
            SearchClinics(searchFilters, setClinicsList, update, setUpdate, pageNumber, setTotalClinicPages, setStatusCode, setErrors);
            setCurrentPage(pageNumber)
        }
    }


    const handleClose = () => {
        setShow(false);
    }

    const [filtersValidated, setFiltersValidated] = useState(false);
    const handleFiltersSubmit = () => {
        InputsSearch(searchFilters, setSearchFilters, inputs, setInputs, setClinicsList, update, setUpdate, setTotalClinicPages, setStatusCode, setErrors);
    }
    const handleSubmitChange = () => {
        UpdateOrderClinic(orderId, selectedClinic.userId, setStatusCode, setErrors).then((r) => {showUpdateToast(); handleClose();});
    }

    return(
        <Modal className="cl-modal" show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>
                    <Trans t={t} i18nKey="advanced-search-clinics.title-change" />
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="row-custom justify-content-center mt-2">
                    <ClinicsFilters
                        handleFiltersSubmit={handleFiltersSubmit}
                        inputs={inputs}
                        setInputs={setInputs}
                        studyTypesList={studyTypesList}
                        insurancePlans={insurancePlans}
                        daysOfTheWeek={daysOfTheWeek}
                        filtersValidated={filtersValidated}
                        />
                    <ClinicsResults
                        results={clinicsList}
                        currentPage={currentPage}
                        totalClinicPages={totalClinicPages}
                        changePageAndFetch={changePageAndFetch}
                        selectedClinic={selectedClinic}
                        chooseClinic={setSelectedClinic}
                    />
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                <Trans t={t} i18nKey="advanced-search-clinics.form.change.cancel" />
                </Button>
                <Button variant="primary" onClick={(e) => {handleSubmitChange(); e.stopPropagation();}}>
                    <Trans t={t} i18nKey="advanced-search-clinics.form.change.submit" />
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ChangeClinic;