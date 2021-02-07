import {useState} from "react";
import axios from "axios";
import {getAllRoutes} from "../constants/Routes";
import {Roles} from "../constants/Roles";

const Test = () => {

    // const [data, setData] = useState({userId:null, id:null, title:null, completed:null});
    //
    // axios.get("https://jsonplaceholder.typicode.com/todos/1").then(res => {setData(res.data)})
    // getAllRoutes(Roles.PATIENT)
    getAllRoutes(Roles.PATIENT);

    return <h1> TESTING </h1>
}

export default Test;