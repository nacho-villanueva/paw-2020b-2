import {Roles} from "./Roles";

import LandingPage from "../components/LandingPage";
import DashboardPage from "../components/DashboardPage";
import MyOrders from "../components/MyOrders";
import {Route} from "react-router-dom";

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

    SEARCH_CLINIC: {
        name: "Search Clinics",
        path: "/dashboard/search-clinics",
        component: MyOrders, //TODO: CHANGE
        role: [Roles.CLINIC, Roles.MEDIC, Roles.PATIENT]
    }
}

export function getAllRoutes(role){
    return Object.entries(Routes).filter( ([name, value]) => (value.role.includes(role)));
}