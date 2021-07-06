import apiInstance from "./index";
import {store} from "../redux";
import {ERROR_CODES, HTTP_CODES} from "../constants/ErrorCodes";
import {login, registerMedic} from "./Auth";
import {authenticate} from "../redux/actions";
import axios from "axios";

function getBase64(file) {
    return new Promise((resolve) => {
        let fileReader = new FileReader();
        fileReader.onload = (e) => resolve(fileReader.result.split(",")[1]);
        fileReader.readAsDataURL(file);
    });
}

export function UpdateAccount(info, oldPassword){
    let id = store.getState().auth.userId
    let data = {oldPassword : oldPassword}

    if(info.email !== "")
        data.email = info.email
    if (info.password !== "")
        data.password = info.password


    return apiInstance.put("/users/"+ id,data)
        .then((r) => {
            store.dispatch(authenticate(r.headers.authorization, store.getState().auth.expire));
            return true;
        }).catch((err) => {
            if(err.response.status === HTTP_CODES.UNAUTHORIZED)
                return {email: false, password:false, oldPassword: ERROR_CODES.INVALID}
            else if(err.response.status === HTTP_CODES.BAD_REQUEST) {
                let errors = {email: false, password:false, oldPassword: false}
                console.log(err.response.data)
                for(let e of err.response.data){
                    switch (e.property){
                        case "password":
                            errors.password = e.code
                            break;
                        case "email":
                            errors.email = ERROR_CODES.INVALID
                            break;
                        case "oldPassword":
                            errors.oldPassword = ERROR_CODES.INVALID
                            break;
                    }
                }
                return errors
            }
    })
}

export function UpdatePatient(info) {

    let id = store.getState().auth.userId;
    let data = {
        name: info.name,
        patientPlanInfo: {
            plan: info.insurancePlan,
            number: info.insuranceNumber
        }
    };

    return apiInstance.put("/patients/"+id, data)
        .then((r) => {return true})
        .catch((err) => {
            if(err.response.status === HTTP_CODES.BAD_REQUEST){
                let errors = {name: false, insurancePlan: false, insuranceNumber: false}
                for(let e of err.response.data){
                    switch (e.property){
                        case "name":
                            errors.name = e.code;
                            break;
                        case "patientPlanInfo.plan":
                            errors.insurancePlan = e.code;
                            break;
                        case "patientPlanInfo.number":
                            errors.insuranceNumber = e.code;
                            break;
                    }
                }
                return errors
            }
        })
}

export async function UpdateMedic(medic) {

    let id = store.getState().auth.userId;
    let data = {
        name: medic.name === "" ? null : medic.name,
        telephone: medic.telephone === "" ? null : medic.telephone,
        identification: null,
        licenceNumber: medic.licenseNumber === "" ? null : medic.licenseNumber,
        medicalFields: medic.medicalFields === [] ? null : medic.medicalFields,
    }

    if(medic.identification !== null) {
        data.identification = {contentType: medic.identification.type}
        await getBase64(medic.identification).then((img) => {
            data.identification.image = img;
        });
    }

    return apiInstance.put("/medics/" + id, data)
        .then(() => {return true})
        .catch((err) => {
            if(err.response.status === HTTP_CODES.BAD_REQUEST){
                let errors = {}
                for(let e of err.response.data){
                    switch (e.property){
                        case "name":
                            errors.name = e.code;
                            break;
                        case "telephone":
                            errors.telephone = e.code;
                            break;
                        case "identification.type":
                        case "identification.image":
                            errors.identification = e.code;
                            break;
                        case "licenceNumber":
                            errors.licenseNumber = e.code;
                            break
                        case "medicalFields":
                            errors.medicalFields = e.code;
                            break;
                    }
                }
                return errors
            }
        });
}

export function UpdateClinic(clinic){
    let id = store.getState().auth.userId;

    const data = {
        name: clinic.name === "" ? null : clinic.name,
        telephone: clinic.telephone === "" ? null : clinic.telephone,
        hours: clinic.hours,
        acceptedPlans: clinic.insurancePlans,
        availableStudies: clinic.studyTypes === [] ? null : clinic.studyTypes,
    }

    return apiInstance.put("/clinics/" + id, data).then(() => {return true})
        .catch((err) => {
            if(err.response.status === HTTP_CODES.BAD_REQUEST){
                let errors = {}
                for(let e of err.response.data){
                    switch (e.property){
                        case "name":
                            errors.name = e.code;
                            break;
                        case "telephone":
                            errors.telephone = e.code;
                            break;
                    }
                    if(e.property.includes("hours"))
                        errors.hours = e.code
                    if(e.property.includes("acceptedPlans"))
                        errors.insurancePlans = e.code
                    if(e.property.includes("availableStudies"))
                        errors.studyTypes = e.code
                }
                return errors
            }
        })
}