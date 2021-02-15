import axios from "axios"
import {store} from "../redux"

const settings = (token) => ( {
    baseURL: 'http://127.0.0.1:8080/',
    timeout: 30000,
});

const apiInstance = axios.create(settings);

apiInstance.interceptors.request.use(function (config) {
    config.baseURL = "http://127.0.0.1:8080/";
    config.headers["authorization"] = "Bearer " + store.getState().auth.token;
    return config
})

export default apiInstance;