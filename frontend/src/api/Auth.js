import { useJwt, decodeToken } from "react-jwt";
import apiInstance from "./"
import {store} from "../redux";
import {authenticate, deAuthenticate} from "../redux/actions";
import {Roles} from "../constants/Roles";

export function login(user, pass){
    apiInstance.post("/", {
        "username": user,
        "password": pass
    })
        .then((r) => {
            store.dispatch(authenticate(r.headers.authorization));
        }).catch();
}

export function registerUser(email, pass){

    apiInstance.post("/users",
        {
            "email": email,
            "password": pass,
            "locale": navigator.language
        })
        .then( (r) => console.log(r.status)).catch(() => "Fuck you");

}

export function logout(){
    store.dispatch(deAuthenticate());
}


export function GetLoggedMedic(orderInfo, setOrderInfo, count, setCount){
    apiInstance.get( "/medics/" + store.getState().auth.userId )
    .then((r) => {
        orderInfo.medicName = r.data.name;
        orderInfo.medicEmail = store.getState().auth.email;
        orderInfo.medicLicenceNumber = r.data.licenceNumber;
        setOrderInfo(orderInfo);
        setCount(count+2);
    })
    .catch();
}

export function FindPatient(email, orderInfo, setOrderInfo, count, setCount){
    //i need a way to lookUp patients by email, currently there is no way to do that...
    //unless i use listUsers and look through that whole thing
}

export function GetStudyTypes(setStudyTypesList, count, setCount){
    apiInstance.get("/study-types")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name};
        }
        setStudyTypesList(stl);
        setCount(count+1);
    })
    .catch((error) => {console.log("bruh", error)});
}

export function QueryClinics(filters, setClinicsList, count, setCount){

}