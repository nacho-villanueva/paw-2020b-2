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