import apiInstance, {apiRedirects} from "./index";
import {GetInsurancePlanByURL} from "./CustomFields";
import {intermediateMinuteCircle} from "react-timekeeper/lib/components/styles/clock-hand";
import {StatusType} from "../redux/actions/actions";

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

export async function GetPatientInfo(id) {

    const patientInfo = await apiInstance.get("/patients/" + id).then((r) => {return r.data})

    let insurancePlan = null;

    if(patientInfo.medicPlan !== undefined)
        insurancePlan = await GetInsurancePlanByURL(patientInfo.medicPlan).then((r) => {return r.data})

    return {
        name: patientInfo.name,
        insurancePlan: insurancePlan,
        insuranceNumber: patientInfo.medicPlanNumber
    }
}

export function GetIdentificationByURL(url){

    if(url === "")
        return new Promise(resolve => {})

    return apiRedirects.get(url, {headers: {'Accept': 'image/*;encoding=base64'}}).then((r) =>{
        return "data:" + r.headers["content-type"].split(";")[0] + ";base64," + r.data;
    })
}

export function GetMedicalFieldsByURL(url){
    return apiRedirects.get(url).then((r) => {return r.data});
}

export function GetMedicByURL(url){
    return apiRedirects.get(url).then((r) => {return r.data});
}

export function GetClinicByURL(url){
    return apiRedirects.get(url).then((r) => {return r.data});
}

export function GetPatientByURL(url){
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
    return apiRedirects.get(url).then((r) => {
        if (r.status === 204)
            return []
        return r.data
    });
}

export function GetStudyTypesByURL(url){
    return apiRedirects.get(url).then((r) => {
        if (r.status === 204)
            return []
        return r.data
    });
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