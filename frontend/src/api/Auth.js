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
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id};
        }
        setStudyTypesList(stl);
        setCount(count+1);
    })
    .catch((error) => {console.log("bruh", error)});
}

async function InternalQuery(request){
    return apiInstance.get(request)
    .then((r) => {
        console.log(r.data);
        return r.data;
    })
    .catch((error) => {console.log("internalqueryError", error)});
}

export function QueryClinics(filters, setClinicsList, count, setCount, page, setTotalClinicPages){
    apiInstance.get("/clinics", {
        "page": page,
        "clinic": filters.clinicName,
        "plan": filters.insurancePlan,
        "hours": filters.hours
    })
    .then((r) => {
        //this is just horrible
        let headerInfo = r.headers.link;
        headerInfo = headerInfo.split(',');
        headerInfo = headerInfo.pop().split('?').pop().split('>').reverse().pop().split('=').pop();
        setTotalClinicPages(headerInfo);

        let clinics = r.data;
        let clinicsList = [];
        for(var idx in clinics){
            let clinic = {};
            clinic["name"]  = clinics[idx].name;
            clinic["userId"] = clinics[idx].user.split('/').pop();
            clinic["email"] = 'nothere@medtransfer.com';
            clinic["hours"] = clinics[idx].hours;
            clinic["telephone"] = clinics[idx].telephone;
            //I NEED TO CALL UP THE API FOR SOME MORE INFO....
            InternalQuery(clinics[idx].acceptedPlans).then(
                (response) => {
                    clinic["acceptedPlans"] = response;
                }
            );
            InternalQuery(clinics[idx].availableStudies).then(
                (response) => {
                    clinic["medicalStudies"] = response;
                }
            );

            clinicsList[idx] = clinic;
        }
        setClinicsList(clinicsList);

        setCount(count+3);
    })
    .catch();
}

export function CreateMedicalOrder(order){
    apiInstance.post("/orders",{
        clinicId: order.clinicId,
        patientEmail: order.patientEmail,
        patientName: order.patientName,
        studyTypeId: order.studyType,
        description: order.orderDescription,
        medicPlan : {
            plan: order.patientInsurancePlan,
            number: order.patientInsuranceNumber
        }
    }).then((r) => {console.log("nice order", r);})
    .catch((error) => { console.log("OH NO", error);});
}