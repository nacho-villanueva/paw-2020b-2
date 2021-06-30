import {useState, useEffect} from "react";
import { GetOrders } from "../api/Orders";

function MyOrders(){
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

    const [query, setQuery] = useState("redux");

    useEffect(() => {
        const fetchData = () => {
            console.log("fetching data for my orders....");
            GetOrders(orders,setOrders,searchFilters, setTotalOrderPages);
        };
    }, [orders, searchFilters, ]);

    const fetchAndChangePage = (pageNumber) => {
        searchFilters.page = pageNumber;
        GetOrders(orders, setOrders, searchFilters, setTotalOrderPages);
        setCurrentPage(pageNumber);
    }


    return(
        <div className="row justify-content-center">
            <div>pipo</div>
            <button onClick={()=>{fetchAndChangePage(currentPage);}}>CLICK ME {currentPage}</button>
            <button onClick={()=>{setCurrentPage(currentPage + 1);}}>pageNumber++</button>
            <button onClick={()=>{setCurrentPage(1);}}>RESET PAGE NUMBER</button>
        </div>
    );
}
export default MyOrders;