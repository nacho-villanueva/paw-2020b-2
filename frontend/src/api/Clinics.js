import { parameterSerializer, parseHeadersLinks, findLastPageNumber } from "./utils";
import apiInstance from ".";
import { InternalQuery } from "./Auth";

export async function SearchClinics(filters, setClinicsList, update, setUpdate, page, setTotalClinicPages, setStatusCode, setErrors){
    let params = {
        'page': page,
        'days': filters.days,
        'fromTime': filters.fromTime,
        'toTime': filters.toTime,
        'plan': filters.plan,
        'study-type': filters.studyType,
        'clinic': filters.clinicName
    };

    let serializedParams = parameterSerializer(params);

    apiInstance.get("/clinics" + serializedParams ).then((r) => {
        //this is just horrible
        if(r.status === 200){
            let headerInfo = r.headers;
            if(headerInfo.hasOwnProperty("link")){
                let headerInfo = r.headers.link;
                //this is ugly
                let links = parseHeadersLinks(headerInfo);
                let lastPageNumber = findLastPageNumber(links['last']);

                console.log("links", links);
                console.log("last", lastPageNumber);

                setTotalClinicPages(lastPageNumber);
            }else{
                setTotalClinicPages(1);
            }

            let clinics = r.data;
            let clinicsList = [];

            for(var idx in clinics){
                let clinic = {};
                clinic["name"]  = clinics[idx].name;
                //clinic["email"] = 'nothere@medtransfer.com';
                clinic["hours"] = clinics[idx].hours;
                clinic["telephone"] = clinics[idx].telephone;
                //I NEED TO CALL UP THE API FOR SOME MORE INFO....
                InternalQuery(clinics[idx].user).then(
                    (response) => {
                        clinic["email"] = response.email;
                        setUpdate(update + 1);
                    }
                );
                InternalQuery(clinics[idx].acceptedPlans).then(
                    (response) => {
                        clinic["acceptedPlans"] = response;
                        setUpdate(update + 3);
                    }
                );
                InternalQuery(clinics[idx].availableStudies).then(
                    (response) => {
                        clinic["medicalStudies"] = response;
                        setUpdate(update + 5);
                    }
                );

                clinic["userId"] = clinics[idx].user.split('/').pop();

                clinicsList[idx] = clinic;
            }
            setClinicsList(clinicsList);

            setStatusCode(r.status);

            setUpdate(update + 7);
        }

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