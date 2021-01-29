import {ActionType} from "./actions";

export const authenticate = (token, role) => {
    return {
        type: ActionType.AUTHENTICATE,
        token: token,
        role: role,
    }
}

export const deAuthenticate = () => {
    return {
        type: ActionType.DE_AUTHENTICATE
    }
}