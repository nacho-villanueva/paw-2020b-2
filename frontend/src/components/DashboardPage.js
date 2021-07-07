import Sidebar from "./Sidebar";
import NavBar from "./NavBar";

import "./Style/Dashboard.css";
import {Route} from "react-router-dom";
import Test from "./Test";
import {useSelector} from "react-redux";
import CreateOrder from "./CreateOrder";
import EditProfilePage from "./editProfile/EditProfilePage";
import RequestAccessPage from "./requestStudies/RequestAccessPage";
import {Routes} from "../constants/Routes";
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
                    {/*{*/}
                    {/*    Object.keys(Routes).map((key) => {*/}
                    {/*        return Routes[key].role.includes(roleType) && <Route path={Routes[key].path} component={Routes[key].component}/>*/}
                    {/*    })*/}
                    {/*}*/} {/*TODO: UPDATE ROUTES AND UNCOMMENT THIS*/}

                    <Route path="/dashboard/search-clinic" component={DashboardPage}/>
                    <Route path="/dashboard/create-order" component={CreateOrder}/> {/*TODO ONLY ALLOW MEDICS*/}
                    <Route path={Routes.EDIT_PROFILE.path} component={Routes.EDIT_PROFILE.component}/>
                    <Route path={Routes.REQUEST_ORDER.path} component={Routes.REQUEST_ORDER.component}/>
                    <Route path={Routes.ACCESS_REQUESTS.path} component={Routes.ACCESS_REQUESTS.component}/>
                    <Route path="/dashboard/view-study" component={ViewOrder}/>
                    <Route path="/dashboard/my-orders"
                    component={MyOrders}/>
                </div>
            </div>
        </div>
    );
}

export default DashboardPage;