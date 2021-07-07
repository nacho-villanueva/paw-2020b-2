import {Roles} from "./Roles";

import LandingPage from "../components/LandingPage";
import DashboardPage from "../components/DashboardPage";
import MyOrders from "../components/MyOrders";
import CreateOrder from "../components/CreateOrder";
import EditProfilePage from "../components/editProfile/EditProfilePage";
import RequestAccessPage from "../components/requestStudies/RequestAccessPage";
import DisplayAccessRequestsPage from "../components/requestStudies/DisplayAccessRequestsPage";
import ViewOrder from "../components/ViewOrder";

export const Routes = {
    LANDING_PAGE: {
        name: "Landing Page",
        path: "/",
        component: LandingPage,
        role: [Roles.ANON]
    },

    DASHBOARD: {
        name: "Home",
        path: "/dashboard",
        component: DashboardPage,
        role: [Roles.CLINIC, Roles.MEDIC, Roles.PATIENT]
    },

    MY_ORDERS: {
        name: "My Orders",
        path: "/dashboard/my-orders",
        component: MyOrders,
        role: [Roles.CLINIC, Roles.MEDIC, Roles.PATIENT]
    },

    CREATE_ORDER: {
        name: "Create Order",
        path: "/dashboard/create-order",
        component: CreateOrder,
        role: [Roles.MEDIC]
    },

    SEARCH_CLINIC: {
        name: "Search Clinics",
        path: "/dashboard/search-clinics",
        component: MyOrders, //TODO: CHANGE
        role: [Roles.CLINIC, Roles.MEDIC, Roles.PATIENT]
    },
    VIEW_ORDER: {
        name: "View Study",
        path: "/dashboard/view-study",
        component: ViewOrder,
        role: [Roles.ANY]
    },

    EDIT_PROFILE: {
        name: "Edit Profile",
        path: "/dashboard/edit-profile",
        component: EditProfilePage,
        role: [Roles.CLINIC, Roles.MEDIC, Roles.PATIENT]
    },

    REQUEST_ORDER: {
        name: "Request Access to Order",
        path: "/dashboard/request-orders",
        component: RequestAccessPage,
        role: [Roles.MEDIC]
    },

    ACCESS_REQUESTS: {
        name: "Access Requests",
        path: "/dashboard/access-requests",
        component: DisplayAccessRequestsPage,
        role: [Roles.PATIENT]
    },


}

export function getAllRoutes(role){
    return Object.entries(Routes).filter( ([name, value]) => (value.role.includes(role)));
}