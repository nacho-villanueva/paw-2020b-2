import Sidebar from "./Sidebar";
import NavBar from "./NavBar";

import "./Style/Dashboard.css";
import {Route} from "react-router-dom";
import Test from "./Test";
import {useSelector} from "react-redux";
import CreateOrder from "./CreateOrder";
import EditProfilePage from "./editProfile/EditProfilePage";
import ViewOrder from "./ViewOrder";
import MyOrders from "./MyOrders";


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
                    <Route path="/dashboard/view-study" component={ViewOrder}/>
                    <Route path="/dashboard/my-orders"
                    component={MyOrders}/>
                    <Route path="/dashboard/edit-profile" component={EditProfilePage}/>
                    <Route path="/dashboard/create-order" component={CreateOrder}/> {/*TODO ONLY ALLOW MEDICS*/ }
                </div>
            </div>
        </div>
    );
}

export default DashboardPage;