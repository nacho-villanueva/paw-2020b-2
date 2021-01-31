import NavBar from "./NavBar";
import Sidebar from "./Sidebar";
import CreateOrder from "./CreateOrder";

import "./Style/Dashboard.css";
import {Route} from "react-router-dom";
import Test from "./Test";
import {useSelector} from "react-redux";


const Dashboard = () => {
    const roleType = useSelector(state => state.role);

    return(
        <div className={"dashboard"}>
            <NavBar />
            <div className={"dashboardWrapper"}>
                <Sidebar />
                <div className={"dashboardMainContainer"}>
                    <Route path="/home/test" component={Test}/>
                    <Route path="/dashboard/search-clinic" component={Dashboard}/>
                    <Route path="/dashboard/create-order" component={CreateOrder}/>
                </div>
            </div>
        </div>
    );
}

export default Dashboard;