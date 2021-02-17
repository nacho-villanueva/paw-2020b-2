import "./Style/Sidebar.css"
import {NavLink, Route, useHistory, useLocation} from "react-router-dom"
import {useSelector} from "react-redux";
import {getAllRoutes, Routes} from "../constants/Routes";
import {logout} from "../api/Auth";
import {deAuthenticate} from "../redux/actions";
import {store} from "../redux";

const Item = (props) => {
    const history = useHistory();
    const location = useLocation();

    if(props.item.divider){
        return <hr className={"sidebarDivider"}/>
    }

    function click(){
        if(props.item.onClick)
            props.item.onClick();
        history.push(props.item.route.path);
    }
    return <li>
        <NavLink exact to={props.item.route.path} onClick={click} className={"sidebarItem"}><i className={props.item.icon} /><span>{props.item.route.name}</span></NavLink>
    </li>
}

const navigations = [
    {route: Routes.DASHBOARD, icon: "fas fa-fw fa-clinic-medical"},
    {route: {path: "divider1"}, divider:true},
    {route: Routes.MY_ORDERS, icon: "fas fa-fw fa-table"},
    {route: Routes.CREATE_ORDER, icon: "fas fa-fw fa-plus"},
    {route: Routes.SEARCH_CLINIC, icon: "fas fa-fw fa-search"},
    {route: {path: "divider2"}, divider:true},
    {route: {name: "Logout", path: "/"}, icon: "fas fa-fw fa-sign-out-alt", onClick: () => {store.dispatch(deAuthenticate()); }},
    {route: {path: "divider3"}, divider:true},
    ]

const Sidebar = () => {
    const roleType = useSelector(state => state.auth.role);
    const routes = getAllRoutes(roleType);

    return(
    <ul className={"sidebar2"}>
        {navigations.map( (i) => (
            <Item key={i.route.path} item={i} />
        ))}
    </ul>
    )
}

export default Sidebar;