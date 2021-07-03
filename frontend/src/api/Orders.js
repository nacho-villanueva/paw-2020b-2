import {store} from "../redux";
import apiInstance from ".";
import { parameterSerializer } from "./utils";
import {InternalQuery} from "./Auth";

export async function SetUpStudyTypes(orders, setOrders){
    apiInstance.get("/study-types")
    .then((r) => {
        let stl = [];
        for(var idx in r.data){
            stl[idx] = {name: r.data[idx].name, id: r.data[idx].id, url: r.data[idx].url};
        }
        let aux = orders;
        orders.forEach((val, idx) => {
            for(let s in stl){
                if(stl[s].url === val["studyType"]){
                    aux[idx]["studyType"] = stl[s].name;
                }
            }
        });
        setOrders(aux);
        return r.status;
    })
    .catch((error) => {return error;} );
}

export async function GetOrders(setOrders, searchFilters, setTotalOrderPages){
    let params = {
        'page': searchFilters.page,
        'clinics' : searchFilters.clinicIDs,
        'medics' : searchFilters.medicIDs,
        'patientEmails' : searchFilters.patientEmail,
        'fromTime' : searchFilters.fromTime,
        'toTime' : searchFilters.toTime,
        'studyTypes' : searchFilters.studyType,
        'includeShared' : searchFilters.includeShared
    }

    let serializedParams = parameterSerializer(params);

    apiInstance.get( "/orders" + serializedParams).then(
        (r) => {
            if(r.status === 200){
                let headerInfo = r.headers;
                /*
                let headerInfo = r.headers.link;
                headerInfo = headerInfo.split(',');
                headerInfo = headerInfo.pop().split('?').pop().split('>').reverse().pop().split('=').pop();
                */
                let orders = r.data;
                //setUpdate(update+1);
                setOrders(orders);
                setTotalOrderPages(1);

            }
            return r.status;
        }
    ).catch((e) => {
        setOrders([]);
        setTotalOrderPages(0);
        //setUpdate(update+1);
        return e;
    })
}