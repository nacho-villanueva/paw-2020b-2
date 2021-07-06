import {Modal, Button} from "react-bootstrap";
import {ClinicsFilters} from "../search_clinic/ClinicsFilters";
import {ClinicsResults} from "../search_clinic/ClinicsResults";

import { useCallback, useEffect, useState } from "react";
import { GetStudyTypes } from "../../api/Auth";
import { getValueFromEvent, getDaySchedule } from "../../api/utils";
import { getAllInsurancePlans } from "../../api/CustomFields";

import { SearchClinics } from "../../api/Clinics";

export function ChangeClinic(props){
    const show = props.show;
    const setShow = props.setShow;
    const orderInfo = props.orderInfo;

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
    const sendableFilters = {
        clinicName: '',
        insurancePlan: '',
        studyType: '',
        days: new Array(7),
        fromTime: new Array(7),
        toTime: new Array(7)
    };
    const [searchFilters, setSearchFilters] = useState(sendableFilters);

    //used in the FORM
    const defaultInputs = {
        clinicName: '',
        insurancePlan: '',
        studyType: '',
        schedule: [
            {day:0, checked: false, fromTime: '', toTime: ''},
            {day:1, checked: false, fromTime: '', toTime: ''},
            {day:2, checked: false, fromTime: '', toTime: ''},
            {day:3, checked: false, fromTime: '', toTime: ''},
            {day:4, checked: false, fromTime: '', toTime: ''},
            {day:5, checked: false, fromTime: '', toTime: ''},
            {day:6, checked: false, fromTime: '', toTime: ''}
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
        fetchOptions().then((r) => {setUpdate(update + 1);})
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
    const handleFiltersSubmit = (event) => {
        event.preventDefault();

        const form = event.target;
        if(form.checkValidity() === false){
            event.stopPropagation();

            setFiltersValidated(true);
            //FILTERS FORM FEEDBACK CHECKS HERE
        }else{
            setSearchFilters(sendableFilters);

            let auxInputs = inputs;
            auxInputs.studyType = getValueFromEvent("studyPlanSelect", event);
            auxInputs.clinicName = getValueFromEvent("clinicName", event);
            auxInputs.insurancePlan = getValueFromEvent("insurancePlanSelect", event);

            let auxFilters = searchFilters;
            auxFilters.studyType = auxInputs.studyType !== '-1' ?  auxInputs.studyType : '';
            auxFilters.clinicName = auxInputs.clinicName !== -1 ?  auxInputs.clinicName : '';
            auxFilters.insurancePlan = auxInputs.insurancePlan !== -1 ?  auxInputs.insurancePlan : '';
            auxFilters.days.fill(0);
            auxFilters.fromTime.fill(0);
            auxFilters.toTime.fill(0);


            for(let idx = 1; idx <= 7; idx++){
                //console.log("checking days", idx)
                auxInputs.schedule[idx - 1] = getDaySchedule(event, idx - 1);
                if(auxInputs.schedule[idx - 1].checked === true){
                    auxFilters.days[idx - 1] = 1;
                    auxFilters.fromTime[idx - 1] = auxInputs.schedule[idx - 1].fromTime;
                    auxFilters.toTime[idx - 1] = auxInputs.schedule[idx - 1].toTime;

                }
            }

            setSearchFilters(auxFilters);
            setInputs(auxInputs);

            console.log("event change clinic", event);

            SearchClinics(searchFilters, setClinicsList, update, setUpdate, 1, setTotalClinicPages, setStatusCode, setErrors);

        }


    }

    return(
        <Modal className="cl-modal" show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Change Clinic</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="row-custom justify-content-center mt-2">
                    <ClinicsFilters
                        handleFiltersSubmit={handleFiltersSubmit}
                        inputs={inputs}
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
                <Button variant="secondary" onClick={handleClose}>Cancel</Button>
                <Button variant="primary" type="submit">Change Clinic</Button>
            </Modal.Footer>
        </Modal>
    );
}