import apiInstance, {apiRedirects} from "./index";
import {GetInsurancePlanByURL} from "./CustomFields";

export function GetUserByURL(url){
    return apiRedirects.get(url).then((r) => {return r.data} );
}

export function GetUserInfo(id) {
    return apiInstance.get("/users/" + id)
        .then((r) => {return {
            email: r.data.email,
            id: r.data.id
        }})
}

export function GetPatientInfo(id) {
    return apiInstance.get("/patients/" + id)
        .then((pir) => {
            return GetInsurancePlanByURL(pir.data.medicPlan).
            then((ipr) => {
                return {
                    name: pir.data.name,
                    insurancePlan: ipr.data,
                    insuranceNumber: pir.data.medicPlanNumber
                }
            })})
}

export function GetIdentificationByURL(url){
    return apiRedirects.get(url, {headers: {'Accept': 'image/*;encoding=base64'}}).then((r) =>{
        return "data:" + r.headers["content-type"].split(";")[0] + ";base64," + r.data;
    })
}

export function GetMedicalFieldsByURL(url){
    return apiRedirects.get(url).then((r) => {return r.data});
}

export async function GetMedicInfo(id){
    let ret = {}

    const medicInfo = await apiInstance.get("/medics/"+id);

    ret.medicalFields = medicInfo.data.medicalFields;
    ret.identification = medicInfo.data.identification
    ret.licenseNumber = medicInfo.data.licenceNumber;
    ret.name = medicInfo.data.name;
    ret.telephone = medicInfo.data.telephone;
    ret.verified = medicInfo.data.verified;

    return ret;
}

export function GetMedicalPlansByURL(url){
    return apiRedirects.get(url).then((r) => {return r.data});
}

export function GetStudyTypesByURL(url){
    return apiRedirects.get(url).then((r) => {return r.data});
}

export function GetClinicInfo(id){
    return apiInstance("/clinics/" + id).then((r) => {
        return {
            name: r.data.name,
            telephone: r.data.telephone,
            hours: r.data.hours,
            insurancePlans: r.data.acceptedPlans,
            studyTypes: r.data.availableStudies,
            verified: r.data.verified
        }
    })
}