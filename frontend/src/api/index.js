import axios from "axios"
import {store} from "../redux"
import {logout} from "./Auth";

const settings = {
    baseURL: 'http://pawserver.it.itba.edu.ar/paw-2020b-2/api/',
    timeout: 30000,
};

const apiInstance = axios.create(settings);

apiInstance.interceptors.request.use(function (config) {
    if(store.getState().auth.token !== null)
        config.headers["authorization"] = "Bearer " + store.getState().auth.token;
    return config
})

const UNAUTHORIZED = 401;
apiInstance.interceptors.response.use(
    response => response,
    error => {
        if (error.response.status === UNAUTHORIZED && error.response.headers["Token-Status"] === "Expired") {
            logout();
        }
        return Promise.reject(error);
    }
);

export default apiInstance;


export const apiRedirects = axios.create({timeout: 30000})
apiRedirects.interceptors.request.use(function (config) {
    if(store.getState().auth.token !== null)
        config.headers["authorization"] = "Bearer " + store.getState().auth.token;
    return config
})
