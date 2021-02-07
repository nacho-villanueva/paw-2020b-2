import NavBar from "./NavBar";
import Sidebar from "./Sidebar";

import "./Style/Dashboard.css";
import {Route} from "react-router-dom";
import Test from "./Test";
import {useSelector} from "react-redux";


const DashboardPage = () => {
    const roleType = useSelector(state => state.auth.role);

    return(
        <div className={"dashboard"}>
            <NavBar />
            <div className={"dashboardWrapper"}>
                <Sidebar />
                <div className={"dashboardMainContainer"}>
                    <Route path="/dashboard/test" component={Test}/>
                    <Route path="/dashboard/search-clinic" component={DashboardPage}/>
                </div>
            </div>
        </div>
    );
}

export default DashboardPage;