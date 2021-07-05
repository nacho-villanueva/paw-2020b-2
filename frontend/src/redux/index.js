import {createStore} from "redux";
import {persistStore, persistReducer, createTransform} from 'redux-persist'
import storage from 'redux-persist/lib/storage'

import reducer from "./reducers";
import {logout} from "../api/Auth";
import {deAuthenticate} from "./actions";
import {StatusType} from "./actions/actions";

const expireSessionTransform = createTransform(
    (inboundState, key) => {
        return inboundState;
    },
    (outboundState, key) => {
        if(outboundState.status === StatusType.AUTHENTICATED && outboundState.expire <= Math.floor(new Date() / 1000)){
            logout();
            return deAuthenticate();
        }
        return outboundState;
    },
    {whitelist:["auth"]}
)

const persistConfig = {
    key: 'auth',
    storage: storage,
    transforms: [expireSessionTransform],
}


const persistedReducer = persistReducer(persistConfig, reducer);
export const store = createStore(persistedReducer, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__())
export const persistor = persistStore(store)