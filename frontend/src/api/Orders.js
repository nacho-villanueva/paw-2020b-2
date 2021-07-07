import {store} from "../redux";
import apiInstance from ".";
import { parameterSerializer, parseHeadersLinks, findLastPageNumber, prepareViewStudyUrl } from "./utils";
import {InternalQuery} from "./Auth";

export function SetUpStudyTypesAndLink(studyTypesList, orders){
    let aux = orders;
    orders.forEach((val, idx) => {
        for(let s in studyTypesList){
            if(studyTypesList[s].url === val["studyType"]){
                aux[idx]["studyType"] = studyTypesList[s].name;
                console.log(".");
            }
        }
        //eww...
        aux[idx]["url"] = prepareViewStudyUrl(val["url"]);
    });

    return aux;
}

export async function GetAndSetUpStudyTypesAndLink(orders, update, setUpdate, setStudyTypesList){
    return apiInstance.get("/orders/filters/study-type")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id, url: r.data[idx].url};
        }
        setStudyTypesList(stl);
        let out = SetUpStudyTypesAndLink(stl, orders);
        setUpdate((prevState) => {
            let next = prevState + 1;
            return {...prevState, ...next};
        })
        return out;
    })
    .catch((error) => {return orders;} );
}

export async function GetOrders(searchFilters, setTotalOrderPages){
    let params = {
        'page': searchFilters.page,
        'per_page': searchFilters.perPage,
        'clinics' : searchFilters.clinicIDs,
        'medics' : searchFilters.medicIDs,
        'patient-email' : searchFilters.patientEmail,
        'from-date' : searchFilters.fromTime,
        'to-date' : searchFilters.toTime,
        'study-type' : searchFilters.studyType,
        'include-shared' : searchFilters.includeShared
    }

    let serializedParams = parameterSerializer(params);

    return apiInstance.get( "/orders" + serializedParams).then(
        (r) => {
            if(r.status === 200){
                let headerInfo = r.headers;
                if(headerInfo.hasOwnProperty("link")){
                    let headerInfo = r.headers.link;
                    //this is ugly
                    let links = parseHeadersLinks(headerInfo);
                    let lastPageNumber = findLastPageNumber(links['last']);
                    setTotalOrderPages(lastPageNumber);
                }else{
                    setTotalOrderPages(1);
                }
                let orders = r.data;
                return orders;

            }
            return [];
        }
    ).catch((e) => {
        setTotalOrderPages(0);
        return [];
    })
}

export async function GetMedics(setMedicsList, update, setUpdate){
    apiInstance.get("/orders/filters/medic")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id, url: r.data[idx].url};
        }
        setMedicsList(stl);
        setUpdate((prevState) => {
            let next = prevState + 1;
            return {...prevState, ...next};
        })
        return r.status;
    })
    .catch((error) => {return -1;} );
}

export async function GetClinics(setClinicsList, update, setUpdate){
    apiInstance.get("/orders/filters/clinic")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id, url: r.data[idx].url};
        }
        setClinicsList(stl);
        setUpdate((prevState) => {
            let next = prevState + 1;
            return {...prevState, ...next};
        })
        return r.status;
    })
    .catch((error) => {return -1;} );
}

export async function UpdateOrderClinic(orderId, clinic, setStatusCode, setErrors){
    apiInstance.put('/orders/' + orderId, {clinicId: clinic})
    .then((r) => {
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