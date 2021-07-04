import {store} from "../redux";
import apiInstance from ".";
import { parameterSerializer, parseHeadersLinks, findLastPageNumber, prepareViewStudyUrl } from "./utils";
import {InternalQuery} from "./Auth";

export function SetUpStudyTypesAndLink(studyTypesList, orders, setOrders){
    let aux = orders;
    orders.forEach((val, idx) => {
        for(let s in studyTypesList){
            if(studyTypesList[s].url === val["studyType"]){
                aux[idx]["studyType"] = studyTypesList[s].name;
            }
        }
        //eww...
        aux[idx]["url"] = prepareViewStudyUrl(val["url"]);
    });
    setOrders(aux);
}

export async function GetAndSetUpStudyTypesAndLink(orders, setOrders){
    apiInstance.get("/study-types")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id, url: r.data[idx].url};
        }
        SetUpStudyTypesAndLink(stl, orders, setOrders);
        /*
        let aux = orders;
        orders.forEach((val, idx) => {
            for(let s in stl){
                if(stl[s].url === val["studyType"]){
                    aux[idx]["studyType"] = stl[s].name;
                }
            }
            aux[idx]["url"] = prepareViewStudyUrl(val["url"]);
        });
        setOrders(aux);
        */
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
                }
                let orders = r.data;
                setOrders(orders);
                setTotalOrderPages(1);

            }
            return r.status;
        }
    ).catch((e) => {
        setOrders([]);
        setTotalOrderPages(0);
        return -1;
    })
}