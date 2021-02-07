import {ActionType, StatusType} from "./actions";
import {getRoleFromID, Roles} from "../../constants/Roles";
import {decodeToken} from "react-jwt";

export const authenticate = (token) => {

   const user = decodeToken(token);

    return {
        type: ActionType.AUTHENTICATE,
        auth: {
            status: StatusType.AUTHENTICATED,
            token: token,
            role: getRoleFromID(user.role),
            userId: user.id,
            email: user.sub,
        }
    }
}

export const deAuthenticate = () => {
    return {
        type: ActionType.DE_AUTHENTICATE,
        auth: {
            status: StatusType.DE_AUTHENTICATED,
            token: null,
            role: Roles.ANON,
            userId: null,
            email: null,
        }
    }
}