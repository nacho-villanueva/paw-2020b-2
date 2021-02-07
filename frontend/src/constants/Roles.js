export const Roles = {
    ANY: "Any",
    ANON: "Anon",
    MEDIC: "Medic",
    CLINIC: "Clinic",
    PATIENT: "Patient",
    UNREGISTERED: "Unregistered",
}

export function getRoleFromID(id){
    switch (id){
        case 0:
        case 1:
        case 2:
        case 3: //TODO
        case 4:
            return Roles.UNREGISTERED;
    }
}