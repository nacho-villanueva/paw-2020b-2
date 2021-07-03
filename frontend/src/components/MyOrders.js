import {useState, useEffect, useLayoutEffect} from "react";
import { GetOrders, SetUpStudyTypes } from "../api/Orders";
import {useSelector} from "react-redux";

import "./Style/MyOrders.css";
import { GetStudyTypes, InternalQuery } from "../api/Auth";
import { Roles } from "../constants/Roles";

function MyOrders(){
    const roleType = useSelector(state => state.auth.role);
    const status = useSelector(state => state.auth.status);
    const auth = useSelector(state => state.auth);

    const [orders, setOrders] = useState([]);
    const [searchFilters, setSearchFilters] = useState({
        page: 1,
        clinicIDs: '',
        medicIDs: '',
        patientEmail: '',
        fromTime: '',
        toTime: '',
        studyType: '',
        includeShared: true
    });
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
       //GetOrders(orders, setOrders, searchFilters, setTotalOrderPages, update, setUpdate);
       GetOrders(setOrders,searchFilters, setTotalOrderPages).then( (r) => {SetUpStudyTypes(orders, setOrders).then((r) => { SetUpClinicNames(); console.log("FETCHING PAGE")})});
       setCurrentPage(pageNumber);
       console.log("ROLE TYPE", roleType);
       console.log("STATUS", status);
       console.log("auth", auth);

    }

    //calling on mount...
    useLayoutEffect( () => {
        fetchAndChangePage(currentPage);

    }, []);

    const OrderItem = (props) => {
        const order = props.order;
        return(
            <aside>
                <li className="nav-item">
                    <a className="nav-link" id={"order-"+props.index+"-tab"}
                        data-toggle="tab" role="tab" style={{cursor:"pointer"}}
                        onClick={() => {console.log("ORDER Selected:", order)}}
                        href={order.url}
                    >
                        <div className="d-flex w-100 justify-content-between">
                            <h5 className="mb-1">Study Type: {order.studyType}</h5>
                            <small>Date: {order.date}</small>
                        </div>
                        <div className="d-flex w-100 justify-content-between">
                            <p className="mb-1">
                            {(roleType === Roles.PATIENT || roleType === Roles.MEDIC) ?
                                "Clinic: "+order.clinic :
                                "Patient: " +order.patientName
                            }
                            </p>
                            <small>
                            {(roleType === Roles.CLINIC || roleType === Roles.PATIENT) ?
                                "Medic: " +order.medic :
                                "Patient: " +order.patientName
                            }
                            </small>
                        </div>
                    </a>
                </li>
            </aside>
        );
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
                            <OrderItem order={item} index={index} key={"orderItem_"+index+"_"+update}/>
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
                        <p className="card-title h4">Orders:</p>
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
                    <button onClick={()=>{fetchAndChangePage(currentPage);}}>CLICK ME {currentPage}</button>
                    <button onClick={()=>{setCurrentPage(currentPage + 1);}}>pageNumber++</button>
                    <button onClick={()=>{setCurrentPage(1);}}>RESET PAGE NUMBER</button>
                </div>
            </div>

        </div>
    );
}
export default MyOrders;