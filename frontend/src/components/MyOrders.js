import {useState, useLayoutEffect} from "react";
import { GetOrders, GetAndSetUpStudyTypesAndLink, SetUpStudyTypesAndLink } from "../api/Orders";
import {useSelector} from "react-redux";
import { Form, Button} from "react-bootstrap";

import "./Style/MyOrders.css";
import { GetStudyTypes, InternalQuery } from "../api/Auth";
import { Roles } from "../constants/Roles";
import { OrderItem} from "./order_components/OrderItem";
import { MyOrdersFilters} from "./order_components/MyOrdersFilter";
import { getValueFromEvent } from "../api/utils";

function MyOrders(){
    const roleType = useSelector(state => state.auth.role);
    const status = useSelector(state => state.auth.status);
    const auth = useSelector(state => state.auth);

    const [orders, setOrders] = useState([]);
    const [searchFilters, setSearchFilters] = useState({
        page: 1,
        perPage: '',
        clinicIDs: -1,
        patientEmail: '',
        fromTime: '',
        toTime: '',
        studyType: -1,
        includeShared: true
    });
    const [studyTypesList, setStudyTypesList] = useState([]);
    const [clinicsList, setClinicsList] = useState([]);

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
                    await InternalQuery(order["clinic"]).then((res) => {aux[index]["clinic"] = res.name;});
                    setOrders(aux);
                    setCount(count +1)
                    setUpdate(update+count);
                }
            }))
        }
    }

    const cleanUpFilters = () => {
        let aux = searchFilters;
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


    const fetchAndChangePage = (pageNumber) => {
        cleanUpFilters();
        setCurrentPage(searchFilters.page);

        GetOrders(setOrders,searchFilters, setTotalOrderPages)
            .then( (res) => {
                if(studyTypesList.length === 0 ) {
                    GetAndSetUpStudyTypesAndLink(orders, setOrders, update, setUpdate)
                        .then((r) => {
                            //console.log("running on fetched studytypeslist");
                            SetUpClinicNames();
                            setUpdate(update+1);
                           // console.log("FETCHING PAGE", orders)

                        });
                }else{
                    //console.log("running on saved studytypeslist", studyTypesList);
                    SetUpStudyTypesAndLink(studyTypesList, orders, setOrders);
                    SetUpClinicNames();
                    setUpdate(update+1);
                }
            });
        setCurrentPage(pageNumber);
       /*
       console.log("ROLE TYPE", roleType);
       console.log("STATUS", status);
       console.log("auth", auth);
       console.log("TOTAL PAGES", totalOrderPages);
        */
    }

    const [searching, setSearching] = useState(0);

    //calling on mount...
    useLayoutEffect( () => {
        fetchAndChangePage(searchFilters.page);
        GetStudyTypes(setStudyTypesList, update, setUpdate);
    }, [,searching]);


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

            aux.clinicIDs = getValueFromEvent("clinicSelect", event);

            aux.patientEmail = getValueFromEvent("patientEmailInput", event);
            aux.fromTime = getValueFromEvent("from-datePick", event);
            aux.toTime = getValueFromEvent("to-datePick", event);



            setCurrentPage(aux.page);
            setSearchFilters(aux);

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
                <h1 className="text-center mt-5 py-5">It seems there are no orders to be shown.</h1>
            </section>
        );
    }

    const FetchedResults = () => {
        return(
            <div className="custom-row" key={"fetchedResults_"+update+"+"+searching}>
                <ul className="nav flex-column w-100" id="orders" role="tablist">
                    {
                        orders.map((item, index) => (
                            <OrderItem order={item} role={roleType} index={index} key={"orderItem_"+index+"_"+update}/>
                        ))
                    }
                </ul>
            </div>
        );
    }


    return(
        <div className="custom-row justify-content-center mt-5" key={"my-orders_" + update}>
            <div className="card bg-light float-left filters-card reorder">
                <div className="card-body">
                    <p className="card-title h4">Filters</p>
                    <hr/>
                    <MyOrdersFilters
                        filtersValidated={filtersValidated}
                        handleFiltersSubmit={handleFilterSubmit}
                        searchFilters={searchFilters}
                        clinicsList={clinicsList}
                        studyTypesList={studyTypesList}
                        role={roleType}
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
                    <p className="card-title h4">Orders</p>
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