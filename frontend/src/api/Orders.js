import {store} from "../redux";
import apiInstance from ".";
import { parameterSerializer, parseHeadersLinks, findLastPageNumber, prepareViewStudyUrl } from "./utils";
import {InternalQuery} from "./Auth";
import {GetClinicByURL, GetMedicByURL, GetPatientByURL, GetStudyTypesByURL} from "./UserInfo";

export function SetUpStudyTypesAndLink(studyTypesList, orders, setOrders){
    let aux = orders;
    console.log("WHY", studyTypesList);
    orders.forEach((val, idx) => {
        for(let s in studyTypesList){
            if(studyTypesList[s].url === val["studyType"]){
                aux[idx]["studyType"] = studyTypesList[s].name;
                console.log("YES");
            }
        }
        //eww...
        aux[idx]["url"] = prepareViewStudyUrl(val["url"]);
    });
    setOrders(aux);
}

export async function GetAndSetUpStudyTypesAndLink(orders, setOrders, update, setUpdate, setStudyTypesList){
    apiInstance.get("/study-types")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id, url: r.data[idx].url};
        }
        setStudyTypesList(stl);
        SetUpStudyTypesAndLink(stl, orders, setOrders);
        setUpdate((prevState) => {
            let next = prevState + 1;
            return {...prevState, ...next};
        })
        return r.status;
    })
    .catch((error) => {return -1;} );
}

export async function GetOrders(setOrders, searchFilters, setTotalOrderPages){
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

    apiInstance.get( "/orders" + serializedParams).then(
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
                setOrders(orders);

            }
            return r.status;
        }
    ).catch((e) => {
        setOrders([]);
        setTotalOrderPages(0);
        return -1;
    })
}

export async function GetAllOrders(page, per_page){
    let orders = await apiInstance.get("/orders?page="+page+"&per_page="+per_page).then((r) => r.data);

    let newOrders = []

    for(let o of orders){
        let newOrder = {...o}

        console.log(newOrder)

        await Promise.allSettled([
            GetStudyTypesByURL(newOrder.studyType).then((r) => r),
            GetMedicByURL(newOrder.medic).then((r) => r),
            GetClinicByURL(newOrder.clinic).then((r) => r),

        ]).then((r) => {
            newOrder.studyType = r[0].value;
            newOrder.medic = r[1].value;
            newOrder.clinic = r[2].value;
            newOrders.push(newOrder)

        })


    }

    return newOrders;
}

export async function GetMedics(setMedicsList, update, setUpdate){

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