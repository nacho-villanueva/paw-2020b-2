export const Roles = {
    ANY: "Any",
    ANON: "Anon",
    MEDIC: "Medic",
    CLINIC: "Clinic",
    PATIENT: "Patient",
    UNREGISTERED: "Unregistered",
}

export function getRoleFromID(id){
    switch (parseInt(id)){
        case 1:
            return Roles.PATIENT;
        case 2:
            return Roles.MEDIC;
        case 3:
            return Roles.CLINIC;
        case 4:
            return Roles.UNREGISTERED;
        default:
            return Roles.ANON;
    }
}