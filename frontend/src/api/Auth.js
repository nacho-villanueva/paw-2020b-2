import apiInstance from "./"
import {store} from "../redux";
import {authenticate, deAuthenticate, updateRole} from "../redux/actions";
import {Roles} from "../constants/Roles";
import {ERROR_CODES} from "../constants/ErrorCodes";
import { parseHeadersLinks, prepareViewStudyUrl } from "./utils";

export function login(user, pass, rememberMe, onSuccess, onFail){

    let expire = new Date();
    if(rememberMe === true)
        expire.setDate( expire.getDate() + 7);
    else if(rememberMe === false)
        expire.setHours( expire.getHours() + 12);
    const expireEpoch = Math.floor( expire / 1000 );

    apiInstance.post("/", {
        "username": user,
        "password": pass
    })
        .then((r) => {
            store.dispatch(authenticate(r.headers.authorization, expireEpoch));
            onSuccess()
        }).catch(onFail);
}

export function registerUser(email, pass, onSuccess, onFail){

    apiInstance.post("/users",
        {
            "email": email,
            "password": pass,
            "locale": navigator.language
        }).then(onSuccess)
        .catch((err) => {
            let errors = {email: false, password: false}
            if(err.response != null) {
                for (const e of err.response.data) {
                    switch (e.property) {
                        case "email":
                            if(e.code === ERROR_CODES.ALREADY_EXISTS)
                                errors.email = ERROR_CODES.ALREADY_EXISTS;
                            else
                                errors.email = ERROR_CODES.INVALID
                            break;
                        case "password":
                            errors.password = true;
                            break;
                        default:
                            break;
                    }
                }
            }
            onFail(errors)
        });

}

export function registerPatient(name, insurancePlan, insuranceNumber, onSuccess, onFail){
    apiInstance.post("/patients/",
        {
            "name": name,
            "patientPlanInfo": {
                "plan": insurancePlan,
                "number": insuranceNumber
            }
        })
        .then( (r) => {
            store.dispatch(updateRole(Roles.PATIENT));
            onSuccess()
        }).catch((err) => {
            onFail();
        });
}

export function registerMedic(medic, onSuccess, onFail){

    apiInstance.post("/medics/",
        {
            "name": medic.name,
            "telephone": medic.telephone,
            "identification": medic.identification,
            "licenceNumber": medic.licenceNumber,
            "medicalFields": medic.medicalFields
        })
        .then( (r) => {
            store.dispatch(updateRole(Roles.MEDIC));
            onSuccess();
        }).catch((err) => {
            let response = []
            if(err.response != null)
                response = err.response.data
            onFail(response);
        });
}

export function registerClinic(clinic, onSuccess, onFail){
    apiInstance.post("/clinics/",
        {
            "name": clinic.name,
            "telephone": clinic.phoneNumber,
            "availableStudies": clinic.studyTypes,
            "acceptedPlans": clinic.acceptedInsurance,
            "hours": clinic.hours
        })
        .then( (r) => {
            store.dispatch(updateRole(Roles.CLINIC));
            onSuccess()
        }).catch((err) => {
            let response = []
            if(err.response != null)
                response = err.response.data
            onFail(response);
    });
}

export function logout(){
    store.dispatch(deAuthenticate());
}


export function GetLoggedMedic(orderInfo, setOrderInfo, count, setCount, setStatusCode, setErrors){
    apiInstance.get( "/medics/" + store.getState().auth.userId )
    .then((r) => {
        orderInfo.medicName = r.data.name;
        orderInfo.medicEmail = store.getState().auth.email;
        orderInfo.medicLicenceNumber = r.data.licenceNumber;
        orderInfo.identificationSrc = r.data.identification;
        setOrderInfo(orderInfo);
        setCount(count+2);
        setStatusCode(r.status);
    })
    .catch((e)  => {
        if(e.response){
            // error in response
            setStatusCode(e.response.status);
            if(e.response.status === 400 && e.response.data !== undefined){
                setErrors(e.response.data)
            }
        }else if(e.request){
            // no response received
            console.log('Error in request: ',e.request);
        }else{
            // error in the request building, which shouldn't happen
            console.log('Error in request: ', e.message);
        }
    });
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
        setPatientInfo(out);
        setCount(count+3);

    })
    .catch((e) => {
        let out = patientInfo;
        out.email = patientEmail;
        out.error = true;
        out.loading = false;

        setPatientInfo(out);
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
        setCount((prevState) => {
            let next = prevState + 1;
            return {...prevState, ...next};
        })
    })
    .catch((error) => {console.log("error in request.", error)});
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

export function QueryClinics(filters, setClinicsList, count, setCount, page, setTotalClinicPages, setStatusCode, setErrors){
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

            setStatusCode(r.status);

            //setCount(count+3);
        }

    })
    .catch((e)  => {
        if(e.response){
            // error in response
            setStatusCode(e.response.status);
            if(e.response.status === 400 && e.response.data !== undefined){
                setErrors(e.response.data)
            }
        }else if(e.request){
            // no response received
            console.log('Error in request: ',e.request);
        }else{
            // error in the request building, which shouldn't happen
            console.log('Error in request: ', e.message);
        }
    });
}

export function CreateMedicalOrder(order, setStatusCode, setErrors, setOrderStatus){
    apiInstance.post("/orders",{
        clinicId: order.clinicId,
        patientEmail: order.patientEmail,
        patientName: order.patientName,
        studyTypeId: order.studyTypeId,
        description: order.orderDescription,
        patientMedicPlan : order.patientInsurancePlan,
        patientMedicPlanNumber : order.patientInsuranceNumber
    }).then( (r) => {
        setStatusCode(r.status);
        //uh
        let location = r.headers.location.split('/');
        let aux = {code: r.status, id:location[location.length -1 ] };
        setOrderStatus(aux);
    })
    .catch((e)  => {
        if(e.response){
            // error in response
            setStatusCode(e.response.status);
            if(e.response.status === 400 && e.response.data !== undefined){
                setErrors(e.response.data)
            }
        }else if(e.request){
            // no response received
            console.log('Error in request: ',e.request);
        }else{
            // error in the request building, which shouldn't happen
            console.log('Error in request: ', e.message);
        }
    });
}

export function GetLogguedUser(userInfo, setUserInfo, count, setCount){

}
export function GetOrderInfo(orderId, orderInfo, setOrderInfo, count, setCount){
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
        aux.id = data.id;

        InternalQuery(data.clinic).then((res) => {
            aux["clinic"] = res.name;
            setCount((prevState) => {
                let next = prevState + 1;
                return {...prevState, ...next};
            })
        });
        InternalQuery(data.medic).then((res) => {
            aux["medicName"] = res.name;
            aux["medicLicenceNumber"] = res.licenceNumber;
            setCount((prevState) => {
                let next = prevState + 1;
                return {...prevState, ...next};
            })
        });
        InternalQuery(data.studyType).then((res) => {
            aux["studyType"] = res.name;
            setCount((prevState) => {
                let next = prevState + 1;
                return {...prevState, ...next};
            })
        });


        setOrderInfo(aux);

    }).catch((e) => {
        console.log("error in orders request.", e)
    });
}

export function GetResults(orderId,setResults){
    const request = "/orders/"+orderId+"/results/";
    console.log("req", request);

    apiInstance.get(request)
    .then((r) => {
        console.log("results", r);
        let data = r.data;

        setResults(data);

    }).catch((e) => {
        console.log("error in results request.", e)
    });
}

export function RegisterPatient(patient){

}

export function RegisterMedic(medic){

}

export function RegisterClinic(clinic){

}