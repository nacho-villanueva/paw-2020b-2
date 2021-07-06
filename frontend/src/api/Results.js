import {store} from "../redux";
import apiInstance from ".";
import { parameterSerializer, parseHeadersLinks, findLastPageNumber, prepareViewStudyUrl, convertToBase64 } from "./utils";
import {InternalQuery} from "./Auth";

export function UploadResult(result, setStatusCode){
    apiInstance.post('/orders/'+ result.id +'/results?orderId=' + result.id, {
        file : result.responseFile,
        identification: result.responsibleIdentification,
        responsibleName: result.responsibleName,
        responsibleLicence: result.responsibleLicenceNumber
    }).then( (r) => {
        console.log("STATUS:", r.status);
        setStatusCode(r.status);
    })
    .catch((e)  => {
        if(e.response){
            // error in response
            console.log("ERROR:",e.response.status);
            setStatusCode(e.response.status);
            if(e.response.status === 400 && e.response.data !== undefined){
                console.log("MESSAGE:", e.response.data)
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