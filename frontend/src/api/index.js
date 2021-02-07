import axios from "axios"
import {store} from "../redux"

const settings = (token) => ( {
    baseURL: 'http://127.0.0.1:8080/webapp_war_exploded',
    timeout: 1000,
    headers: {
        "authorization" : token,
    }});

const apiInstance = axios.create(settings(store.getState().auth.token));

export default apiInstance;