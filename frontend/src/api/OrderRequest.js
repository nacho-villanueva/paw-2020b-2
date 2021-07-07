import apiInstance from "./index";
import {ERROR_CODES, HTTP_CODES} from "../constants/ErrorCodes";

export function RequestAccessToOrder(email, studyType){

    const data = {
        patientEmail: email,
        studyTypeId: studyType.id
    }

    return apiInstance.post("/share-requests", data).then( () => {
        return HTTP_CODES.OK;
    }).catch((err) => {
        if(err.response.status === HTTP_CODES.BAD_REQUEST){
            let errors = {email: false, study: false}
            for (let e in err.response.data){
                if(e.property === "patientEmail")
                    errors.email = e.code;
                else if(e.property === "studyTypeId")
                    errors.study = e.code;
            }

            if(!errors.email && !errors.study)
                errors.email = ERROR_CODES.OTHER

            return errors;
        }
        return err.response.status;
    })
}

export function GetShareRequests(page, per_page) {
    return apiInstance.get("/share-requests?page="+ page + "&per_page=" + per_page)
        .then( (r) => {

            if (r.status === HTTP_CODES.OK) {
                let links = r.headers["link"];
                let last = links.split(",");
                let lastPage = last[last.length - 1].match(/&page=[0-9]+/)[0].split("=")[1]

                let pageCount = parseInt(lastPage)

                return {value: r.data, pageCount: pageCount}
            } else if (r.status === HTTP_CODES.EMPTY) {
                return {value: [], pageCount: 0}
            }
        })
}

export function AcceptAccessRequest(medicId, studyId){
    return apiInstance.post("/share-requests/"+medicId+"/"+studyId)
}

export function DenyAccessRequest(medicId, studyId){
    return apiInstance.delete("/share-requests/"+medicId+"/"+studyId)
}