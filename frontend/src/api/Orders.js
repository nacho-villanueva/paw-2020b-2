import {store} from "../redux";
import apiInstance from ".";
import { parameterSerializer } from "./utils";

export function GetOrders(orders, setOrders, searchFilters, setTotalOrderPages){
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
                console.log("HEADER:", headerInfo);
                console.log("DATA:", orders);
                setOrders(orders);
                setTotalOrderPages(1);

            }
        }
    )
}