import {useState, useLayoutEffect, useEffect} from "react";
import { GetOrders, GetAndSetUpStudyTypesAndLink, SetUpStudyTypesAndLink, GetMedics, GetClinics } from "../api/Orders";
import {useSelector} from "react-redux";
import { Form, Button} from "react-bootstrap";
import { Trans, useTranslation } from 'react-i18next'

import "./Style/MyOrders.css";
import { GetStudyTypes, InternalQuery } from "../api/Auth";
import { Roles } from "../constants/Roles";
import { OrderItem} from "./order_components/OrderItem";
import { MyOrdersFilters} from "./order_components/MyOrdersFilter";
import { PaginationComponent } from "./order_components/PaginationComponent";
import { getValueFromEvent } from "../api/utils";

function MyOrders(){

    const { t } = useTranslation();

    const roleType = useSelector(state => state.auth.role);
    const status = useSelector(state => state.auth.status);
    const auth = useSelector(state => state.auth);

    const [orders, setOrders] = useState([]);
    const [searchFilters, setSearchFilters] = useState({
        page: 1,
        perPage: '',
        clinicIDs: '',
        medicIDs: '',
        patientEmail: '',
        fromTime: '',
        toTime: '',
        studyType: '',
        includeShared: true
    });
    const [studyTypesList, setStudyTypesList] = useState([]);
    const [clinicsList, setClinicsList] = useState([]);
    const [medicsList, setMedicsList] = useState([]);

    const [totalOrderPages, setTotalOrderPages] = useState(1);
    const [currentPage, setCurrentPage] = useState(1);
    const [update, setUpdate] = useState(0);

    //unused states....
    const [count, setCount] = useState(0);
    const [query, setQuery] = useState("redux");

    const SetUpClinicNames = () => {
        if(orders.length > 0){
            let aux = orders;
            Promise.all(orders.map(async (order, index) => {
                if(order["clinic"].includes("http")){
                    await InternalQuery(order["clinic"]).then((res) => {
                        aux[index]["clinic"] = res.name;
                        setOrders((prevState) => {
                            let next = aux;
                            return {...prevState, ...next};
                        });
                    })
                    //setOrders(aux);
                    setCount((prevState) => {
                        let next = prevState + 1;
                        return {...prevState, ...next};
                    })
                    setUpdate((prevState) => {
                        let next = prevState + 1;
                        return {...prevState, ...next};
                    })
                }
                if(order["medic"].includes("http")){
                    await InternalQuery(order["medic"]).then((res) => {
                        aux[index]["medic"] = res.name;
                        setOrders((prevState) => {
                            let next = aux;
                            return {...prevState, ...next};
                        });
                    });
                    //setOrders(aux);
                    setCount((prevState) => {
                        let next = prevState + 1;
                        return {...prevState, ...next};
                    })
                    setUpdate((prevState) => {
                        let next = prevState + 1;
                        return {...prevState, ...next};
                    })
                }
                    /*
                    setCount(count +1)
                    setUpdate(update+count);
                    */
            })).then((r) => {
                setCount((prevState) => {
                    let next = prevState + 1;
                    return {...prevState, ...next};
                });
                setUpdate((prevState) => {
                    let next = count + 1;
                    return {...prevState, ...next};
                })})
        }
    }

    const cleanUpFilters = () => {
        let aux = searchFilters;
        if(aux.medicIDs === '-1'){
            aux.medicIDs = '';
        }
        if(aux.clinicIDs === '-1'){
            aux.clinicIDs = '';
        }
        if(aux.studyType === '-1'){
            aux.studyType = '';
        }
        if(aux.page < 1 || aux.page > totalOrderPages){
            aux.page = 1;
        }
        setSearchFilters(aux);
    }

    const resetSearchFilters = () => {
        let aux = {
            page: 1,
            perPage: '',
            clinicIDs: -1,
            medicIDs: -1,
            patientEmail: '',
            fromTime: '',
            toTime: '',
            studyType: -1,
            includeShared: true
        };
        setSearchFilters(aux);
    }

    const fetchAndChangePage = (pageNumber) => {

        setCurrentPage(searchFilters.page);

        GetOrders(searchFilters, setTotalOrderPages)
            .then( (res) => {
                if(studyTypesList.length === 0 ) {
                    GetAndSetUpStudyTypesAndLink(res, update, setUpdate, setStudyTypesList)
                        .then((ord) => {
                            //console.log("running on fetched studytypeslist");
                            setOrders(ord);
                            SetUpClinicNames();
                            setUpdate((prevState) => {
                                let next = prevState + 1;
                                return {...prevState, ...next};
                            })
                           // console.log("FETCHING PAGE", orders)

                        });
                }else{
                    //console.log("running on saved studytypeslist", studyTypesList);
                    let newOrders = SetUpStudyTypesAndLink(studyTypesList, res);
                    setOrders(newOrders)
                    SetUpClinicNames();
                    setUpdate((prevState) => {
                        let next = prevState + 1;
                        return {...prevState, ...next};
                    });
                }
            });
        setCurrentPage(pageNumber);
    }

    const changePageAndFetch = (pageNumber) => {
        if(pageNumber >= 1 && pageNumber <= totalOrderPages){
            let aux = searchFilters;
            aux.page = pageNumber;
            setSearchFilters(aux);
            setSearching(searching+1);
        }
    }

    const [searching, setSearching] = useState(false);
    const [load, setLoad] = useState(true);

    //calling on mount...
    useLayoutEffect( () => {
        if(searching){
            fetchAndChangePage(searchFilters.page);
            setSearching(false);
        }
    }, [searching]);


    //calling on mount...
    useEffect( () => {
        if(load){
            fetchAndChangePage(searchFilters.page);
            GetMedics(setMedicsList, update, setUpdate);
            GetClinics(setClinicsList, update, setUpdate);
            setLoad(false);
        }
    }, []);


    const [filtersValidated, setFiltersValidated] = useState(false);
    const handleFilterSubmit = (event) => {
        event.preventDefault();
        const form = event.target;

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            let aux = searchFilters;
            //console.log("BEFORE READING", aux);
            aux.page = 1;
            aux.studyType = getValueFromEvent("studyTypeSelect", event);
            aux.medicIDs = getValueFromEvent("medicSelect", event);
            aux.clinicIDs = getValueFromEvent("clinicSelect", event);

            aux.patientEmail = getValueFromEvent("patientEmailInput", event);
            aux.fromTime = getValueFromEvent("from-datePick", event);
            aux.toTime = getValueFromEvent("to-datePick", event);



            setCurrentPage(aux.page);
            setSearchFilters(aux);
            cleanUpFilters();
            //console.log("after READING", searchFilters);

            setUpdate(update+1);
            setSearching(searching +1);
            //fetchAndChangePage(currentPage);
        }

        setFiltersValidated(true);
    }


    const NoOrdersFound = () => {
        return(
            <section>
                <h1 className="text-center mt-5 py-5"><Trans t={t} i18nKey="my-orders.orders.no-orders"/></h1>
            </section>
        );
    }

    const FetchedResults = () => {
        return(
            <div style={{height: "90%"}}>
                <div className="row list-results" key={"fetchedResults_"+update+"+"+searching}>
                    <ul className="nav flex-column w-100 ul-results" id="orders" role="tablist">
                        {
                            orders !== undefined && orders.length > 0 &&

                            orders.map((item, index) => (
                                <OrderItem order={item} role={roleType} index={index} key={"orderItem_"+index+"_"+update}/>
                            ))
                        }
                    </ul>
                </div>
                <PaginationComponent
                    update={update}
                    current={currentPage}
                    changePageAndFetch={changePageAndFetch}
                    total={totalOrderPages}
                />
            </div>
        );
    }


    return(
        <div className="custom-row justify-content-center mt-5" key={"my-orders_" + update}>
            <div className="card bg-light float-left filters-card reorder">
                <div className="card-body">
                    <p className="card-title h4"><Trans t={t} i18nKey="my-orders.filters.title"/></p>
                    <hr/>
                    <MyOrdersFilters
                        filtersValidated={filtersValidated}
                        handleFiltersSubmit={handleFilterSubmit}
                        searchFilters={searchFilters}
                        clinicsList={clinicsList}
                        studyTypesList={studyTypesList}
                        role={roleType}
                        medicsList={medicsList}
                        resetSearchFilters={resetSearchFilters}
                    />
                </div>
                {/* ::::::::!!!DEV TOOLS!!!::::::::
                <button onClick={()=>{fetchAndChangePage(currentPage);}}>CLICK ME {currentPage}</button>
                <button onClick={()=>{setCurrentPage(currentPage + 1);}}>pageNumber++</button>
                <button onClick={()=>{setCurrentPage(1);}}>RESET PAGE NUMBER</button>
                <button onClick={()=>{setUpdate(update+1);}}>UPDATE++</button>
                */}
            </div>
            <div className="card bg-light float-right orders-card">
                <div className="card-body">
                    <p className="card-title h4"><Trans t={t} i18nKey="my-orders.orders.title"/></p>
                    <hr className="mt-3 mb-4"/>
                    {orders.length === 0 ?
                        <NoOrdersFound/> :
                        <FetchedResults/>
                    }
                </div>
            </div>

        </div>
    );
}
export default MyOrders;