import "./Style/Sidebar.css"
import {NavLink, Route, useHistory, useLocation} from "react-router-dom"
import {useSelector} from "react-redux";
import {getAllRoutes, Routes} from "../constants/Routes";
import {logout} from "../api/Auth";
import {deAuthenticate} from "../redux/actions";
import {store} from "../redux";
import { useTranslation } from 'react-i18next'

const Item = (props) => {
    const history = useHistory();
    const location = useLocation();
    const { t } = useTranslation();

    if(props.item.divider){
        return <hr className={"sidebarDivider"}/>
    }

    function click(){
        if(props.item.onClick)
            props.item.onClick();
        history.push(props.item.route.path);
    }
    return <li>
        <NavLink exact to={props.item.route.path} onClick={click} className={"sidebarItem"}><i className={props.item.icon} /><span>{t(props.item.label)}</span></NavLink>
    </li>
}

const navigations = [
    {route: Routes.DASHBOARD, label: "fragments.sidebar.home", icon: "fas fa-fw fa-clinic-medical"},
    {route: {path: "divider1"}, divider:true},
    {route: Routes.MY_ORDERS, label: "fragments.sidebar.my-orders", icon: "fas fa-fw fa-table"},
    {route: Routes.CREATE_ORDER, label: "fragments.sidebar.create-order", icon: "fas fa-fw fa-plus"},
    {route: Routes.SEARCH_CLINIC, label: "fragments.sidebar.search-clinic", icon: "fas fa-fw fa-search"},
    {route: {path: "divider2"}, divider:true},
    {route: {name: "Logout", path: "/"}, label: "fragments.sidebar.logout", icon: "fas fa-fw fa-sign-out-alt", onClick: () => {store.dispatch(deAuthenticate()); }},
    {route: {path: "divider3"}, divider:true},
    ]

function filterNavigations(role) {
    let navs = []
    for(let n of navigations){
        if(n.divider)
            navs.push(n)
        else if(n.route.role === undefined || n.route.role.includes(role))
            navs.push(n)
    }
    return navs
}

const Sidebar = () => {
    const roleType = useSelector(state => state.auth.role);
    return(
    <ul className={"sidebar2"}>
        {filterNavigations(roleType).map( (i) => (
            <Item key={i.route.path} item={i} />
        ))}
    </ul>
    )
}

export default Sidebar;