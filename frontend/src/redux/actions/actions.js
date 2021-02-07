import {Roles} from "../../constants/Roles";

export const ActionType = {
    AUTHENTICATE: "AUTHENTICATE",
    DE_AUTHENTICATE: "UN_AUTHENTICATE"
}

export const StatusType = {
    AUTHENTICATED: "AUTHENTICATED",
    DE_AUTHENTICATED: "DE_AUTHENTICATED"
}

export const InitState = {
    auth: {
        status: StatusType.DE_AUTHENTICATED,
        token: null,
        role: Roles.ANON,
        userId: null,
        email: null,
    }
}