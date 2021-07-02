import apiInstance from "./"
import {store} from "../redux";
import {authenticate, deAuthenticate, updateRole} from "../redux/actions";
import {Roles} from "../constants/Roles";

export function login(user, pass, rememberMe, setStatusCode, setErrors){

    let expire = new Date();
    if(rememberMe)
        expire.setDate( expire.getDate() + 7);
    else
        expire.setHours( expire.getHours() + 12);
    const expireEpoch = Math.floor( expire / 1000 );

    apiInstance.post("/", {
        "username": user,
        "password": pass
    })
        .then((r) => {
            setStatusCode(r.status);
            store.dispatch(authenticate(r.headers.authorization, expireEpoch));
        }).catch((e)  => {
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

export function registerUser(email, pass, setStatusCode, setErrors){

    apiInstance.post("/users",
        {
            "email": email,
            "password": pass,
            "locale": navigator.language
        })
        .then( (r) => {setStatusCode(r.status);})
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

export function registerPatient(name, insurancePlan, insuranceNumber, onSuccess, onFail){
    apiInstance.post("/patients/",
        {
            "name": name,
            "patientPlanInfo": {
                "plan": {
                    "id": insurancePlan.id,
                    "name": insurancePlan.name,
                    "url": insurancePlan.url
                },
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
        setCount(count+4);

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

export function CreateMedicalOrder(order, setStatusCode, setErrors){
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
    }).then( (r) => {setStatusCode(r.status);})
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

export function RegisterPatient(patient){

}

export function RegisterMedic(medic){

}

export function RegisterClinic(clinic){

}