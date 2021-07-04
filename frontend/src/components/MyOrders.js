import {useState, useEffect, useLayoutEffect} from "react";
import { GetOrders, SetUpStudyTypes } from "../api/Orders";
import {useSelector} from "react-redux";
import { Form, Button} from "react-bootstrap";

import "./Style/MyOrders.css";
import { GetStudyTypes, InternalQuery } from "../api/Auth";
import { Roles } from "../constants/Roles";
import { OrderItem} from "./order_components/OrderItem";
import { MyOrdersFilters} from "./order_components/MyOrdersFilter";

function MyOrders(){
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
        studyType: '5',
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
            let count = 0;
            let aux = orders;
            Promise.all(orders.map(async (order, index) => {
                if(order["clinic"].includes("http")){
                    await InternalQuery(order["clinic"]).then((res) => {aux[index]["clinic"] = res.name;});
                    setOrders(aux);
                    count++;
                    setUpdate(update+count);
                }
            }))
        }
    }


   const fetchAndChangePage = (pageNumber) => {
        searchFilters.page = pageNumber;

        GetOrders(setOrders,searchFilters, setTotalOrderPages)
            .then( (res) => {
                if(res !== -1 ) {
                    SetUpStudyTypes(orders, setOrders)
                        .then((r) => {
                            if(r !== -1){
                                SetUpClinicNames();
                                console.log("FETCHING PAGE", orders)
                            }
                        });
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

    //calling on mount...
    useLayoutEffect( () => {
        fetchAndChangePage(currentPage);
        GetStudyTypes(setStudyTypesList, update, setUpdate);
    }, []);


    const [filtersValidated, setFiltersValidated] = useState(false);
    const handleFilterSubmit = (event) => {
        event.preventDefault();
        const form = event.target;

        if(form.checkValidity() === false){
            event.stopPropagation();
        }else{
            let aux = searchFilters;
            aux.studyType = event.target[0].value;
            aux.clinicIDs = event.target[1].value;
            aux.patientEmail = event.target[2].value;
            aux.fromTime = event.target[3].value;
            aux.toTime = event.target[4].value;
            console.log("AUX", aux);
            console.log("event", event);

        }
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
            <div className="custom-row" key={"fetchedResults_"+update}>
                <ul className="nav flex-column" id="orders" role="tablist">
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
        <div className="custom-row justify-content-center" key={"my-orders_" + update}>
            <div className="col-sm-7">
                <div className="card bg-light">
                    <div className="custom-row mt-4">
                        <p className="card-title h4">Orders</p>
                        <hr className="mt-3 mb-4"/>
                    </div>
                    {orders.length === 0 ?
                        <NoOrdersFound/> :
                        <FetchedResults/>
                    }
                </div>
            </div>
            <div className="col-sm-5">
                <div className="card bg-light">
                    <div className="card-body">
                        <p className="card-title h4">Filters</p>
                        <hr/>
                        <MyOrdersFilters
                            filtersValidated={filtersValidated}
                            handleFiltersSubmit={handleFilterSubmit}
                            searchFilters={searchFilters}
                            clinicsList={clinicsList}
                            studyTypesList={studyTypesList}
                        />
                    </div>
                    {/* ::::::::!!!DEV TOOLS!!!:::::::: */}
                    <button onClick={()=>{fetchAndChangePage(currentPage);}}>CLICK ME {currentPage}</button>
                    <button onClick={()=>{setCurrentPage(currentPage + 1);}}>pageNumber++</button>
                    <button onClick={()=>{setCurrentPage(1);}}>RESET PAGE NUMBER</button>
                </div>
            </div>

        </div>
    );
}
export default MyOrders;