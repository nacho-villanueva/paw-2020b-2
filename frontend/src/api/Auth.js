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
            console.log(store.getState().auth);
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
    return apiInstance.get( "/medics/" + store.getState().auth.userId )
    .then((r) => {
        orderInfo.medicName = r.data.name;
        orderInfo.medicEmail = store.getState().auth.email;
        orderInfo.medicLicenceNumber = r.data.licenceNumber;
        orderInfo.identificationSrc = r.data.identification;
        setOrderInfo(orderInfo);
        setCount(count+2);
    })
    .catch();
}

export function FindPatient(patientEmail, count, setCount, patientInfo, setPatientInfo){
    //i need a way to lookUp patients by email, currently there is no way to do that...
    //unless i use listUsers and look through that whole thing
    let output = {
        error: false,
        patientName: '',
        patientInsurance: {
            plan: '',
            number: ''
        }
    }

    apiInstance.get( "/patients" + "?email=" + encodeURIComponent(patientEmail))
    .then((r) => {
        let out = patientInfo;
        out.email = patientEmail;
        out.name = r.data.name;
        out.insurance.number = r.data.medicPlanNumber;
        out.insurance.plan= r.data.medicPlan;
        out.error = false;
        out.loading = false;
        console.log(out);
        setPatientInfo(out);
        setCount(count+3);

    })
    .catch((e) => {
        let out = patientInfo;
        patientInfo.email = patientEmail;
        patientInfo.error = true;
        patientInfo.loading = false;

        setPatientInfo(patientInfo);
        setCount(count+4);
    });
}

export function GetStudyTypes(setStudyTypesList, count, setCount){
    apiInstance.get("/study-types")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id, url: r.data[idx].url};
        }
        setStudyTypesList(stl);
        setCount(count+5);
    })
    .catch((error) => {console.log("bruh", error)});
}

export async function InternalQuery(request){
    return apiInstance.get(request)
    .then((r) => {
        return r.data;
    })
    .catch((error) => {console.log("internalqueryError", error)});
}

function checkUndefinedArray(array){
    var count = 0;
    for(var idx in array){
        if(array[idx] !== undefined){
            count++;
        }
    }
    return (count > 0);
}

export function QueryClinics(filters, setClinicsList, count, setCount, page, setTotalClinicPages){
    let params = {
        'page': page,
        'days': filters.days,
        'fromTime': filters.fromTime,
        'toTime': filters.toTime,
        'plan': filters.plan,
        'study-type': filters.studyType,
    };

    if(filters.clinicName !== ""){
        params['clinic'] =  filters.clinicName;
    }

    //manually serializing because jersey can't understand arrays otherwise
    let serializedParams = "";
    if(Object.keys(params).length !== 0){
        serializedParams += "?";
        for(var key of Object.keys(params)){
            let param = params[key];
            if(Array.isArray(param) && param && checkUndefinedArray(param)){
                serializedParams += key + "=";
                for(var idx in param){
                    let element = (param[idx] !== 0 ? encodeURIComponent(param[idx]) : "") + (idx < (param.length - 1) ? "," : "");
                    serializedParams += element;
                }
                serializedParams += "&";

            }else if(param !== undefined){
                serializedParams += key + "=" + encodeURIComponent(param);
                serializedParams += "&";
            }

        }
        serializedParams = serializedParams.slice(0, -1);
    }

    apiInstance.get("/clinics" + serializedParams ).then((r) => {
        //this is just horrible
        if(r.status === 200){
            let headerInfo = r.headers.link;
            headerInfo = headerInfo.split(',');
            headerInfo = headerInfo.pop().split('?').pop().split('>').reverse().pop().split('=').pop();
            setTotalClinicPages(headerInfo);

            let clinics = r.data;
            let clinicsList = [];
            for(var idx in clinics){
                let clinic = {};
                clinic["name"]  = clinics[idx].name;
                //clinic["email"] = 'nothere@medtransfer.com';
                clinic["hours"] = clinics[idx].hours;
                clinic["telephone"] = clinics[idx].telephone;
                //I NEED TO CALL UP THE API FOR SOME MORE INFO....
                InternalQuery(clinics[idx].user).then(
                    (response) => {
                        clinic["email"] = response.email;
                    }
                );
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

                clinic["userId"] = clinics[idx].user.split('/').pop();

                clinicsList[idx] = clinic;
            }
            setClinicsList(clinicsList);

            //setCount(count+3);
        }

    })
    .catch();
}

export function CreateMedicalOrder(order){
    apiInstance.post("/orders",{
        clinicId: order.clinicId,
        patientEmail: order.patientEmail,
        patientName: order.patientName,
        studyTypeId: order.studyTypeId,
        description: order.orderDescription,
        medicPlan : {
            plan: order.patientInsurancePlan,
            number: order.patientInsuranceNumber
        }
    }).then((r) => {console.log("nice order", r);})
    .catch((error) => { console.log("OH NO", error);});
}

export function GetLogguedUser(userInfo, setUserInfo, count, setCount){

}
export function GetOrderInfo(orderId, orderInfo, setOrderInfo, count, setCount, orderCount, setOrderCount){
    apiInstance.get("/orders/"+orderId)
    .then((r) => {
        console.log("nice order info", r);
        let data = r.data;

        let aux = orderInfo;

        aux.date = data.date;
        aux.description = data.description;
        aux.identification = data.identification;
        aux.medicPlan = data.medicPlan;
        aux.patientEmail = data.patientEmail;
        aux.patientName = data.patientName;

        InternalQuery(data.clinic).then((res) => {
            aux["clinic"] = res.name;
            setOrderCount(orderCount+2);
        });
        InternalQuery(data.medic).then((res) => {
            aux["medicName"] = res.name;
            aux["medicLicenceNumber"] = res.licenceNumber;
            setOrderCount(orderCount+3);
        });
        InternalQuery(data.studyType).then((res) => {
            aux["studyType"] = res.name;
            setOrderCount(orderCount+5);
        });


        setOrderInfo(aux);
        setCount(count+7);

    }).catch((e) => {
        console.log("getting results for order error", e)
    });
}

export function GetResults(orderId, results, setResults, count, setCount){
    const request = "/orders/"+orderId+"/results/";
    console.log("req", request);

    apiInstance.get(request)
    .then((r) => {
        console.log("results", r);
        let data = r.data;

        let aux = results;
        aux = data;

        setResults(aux);
        setCount(count+11);

    }).catch((e) => {
        console.log("getting results for order error", e)
    });
}