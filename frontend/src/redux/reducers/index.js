import {ActionType, InitState, StatusType} from "../actions/actions"
import {Roles} from "../../constants/Roles";
import {act} from "@testing-library/react";

const reducer = (state = InitState, action) => {
    switch (action.type) {
        case ActionType.AUTHENTICATE:
        case ActionType.DE_AUTHENTICATE:
            return {
                ...state,
                auth: {
                    status: action.auth.status,
                    token: action.auth.token,
                    role: action.auth.role,
                    userId: action.auth.userId,
                    email: action.auth.email,
                    expire: action.auth.expire,
                }};
        case ActionType.UPDATE_ROLE:
            console.log(action.role)
            return {
                ...state,
                auth: {
                    ...state.auth,
                    role: action.role
                }
            };
        default:
            return state;
    }
}

export default reducer;