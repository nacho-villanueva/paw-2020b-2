import {ActionType, StatusType} from "../actions/actions"
import {useAccordionToggle} from "react-bootstrap";

const initState = {
    auth: {
        status: StatusType.DE_AUTHENTICATED,
        token: null,
        role: null,
    }
}

const reducer = (state = initState, action) => {
    switch (action.type) {
        case ActionType.AUTHENTICATE:
            return {status: StatusType.AUTHENTICATED, token: action.token, role: action.role}
        case ActionType.DE_AUTHENTICATE:
            return {status: StatusType.DE_AUTHENTICATED, token: null, role: null}
        default:
            return state;
    }
}

export default reducer;