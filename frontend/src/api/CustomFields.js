import apiInstance from "./index";
import {store} from "../redux";
import {authenticate} from "../redux/actions";

export function getAllInsurancePlans(setInsurancePlans) {

    apiInstance.get("/medic-plans")
        .then((r) => {
            let insurancePlans = []
            for (const i of r.data) {
                insurancePlans.push({name: i.name, id: i.id, url: i.url})
            }
            setInsurancePlans(insurancePlans)
        })
        .catch();
}

export function createInsurancePlan(field, onSuccess, onFail){
    apiInstance.post("/medic-plans",
        {"name": field}
    ).then(() => {onSuccess()}).catch(() => {onFail()});
}

export function getAllStudyTypes(setStudyTypes){
    apiInstance.get("/study-types")
        .then((r) => {
            setStudyTypes(r.data)
        })
        .catch();
}

export function createStudyType(field, onSuccess, onFail){
    apiInstance.post("/study-types",
        {"name": field}
    ).then(() => {onSuccess()}).catch(() => {onFail()});
}

export function getAllMedicalFields(setMedicalFields){
    apiInstance.get("/medical-fields")
        .then((r) => {
            setMedicalFields(r.data)
        })
        .catch();
}

export function createMedicalField(field, onSuccess, onFail){
    apiInstance.post("/medical-fields",
        {"name": field}
        ).then(() => {onSuccess()}).catch(() => {onFail()});
}
