import { SearchClinics } from "../../api/Clinics";

export function InputsSearch(searchFilters, setSearchFilters, inputs, setInputs, setClinicsList, update, setUpdate, setTotalClinicPages, setStatusCode, setErrors){

    let auxFilters = searchFilters;
    auxFilters.studyType = inputs.studyType !== '-1' ?  inputs.studyType : '';
    auxFilters.clinicName = inputs.clinicName !== -1 ?  inputs.clinicName : '';
    auxFilters.plan = inputs.insurancePlan !== -1 ?  inputs.insurancePlan : '';
    auxFilters.days.fill(0);
    auxFilters.fromTime.fill(0);
    auxFilters.toTime.fill(0);
    for(let idx = 1; idx <= 7; idx++){
        if(inputs.schedule[idx - 1].checked === true){
            auxFilters.days[idx - 1] = 1;
            auxFilters.fromTime[idx - 1] = inputs.schedule[idx - 1].fromTime;
            auxFilters.toTime[idx - 1] = inputs.schedule[idx - 1].toTime;
        }
    }
    setSearchFilters(auxFilters);

    SearchClinics(searchFilters, setClinicsList, update, setUpdate, 1, setTotalClinicPages, setStatusCode, setErrors);
}