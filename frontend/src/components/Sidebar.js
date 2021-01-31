import "./Style/Sidebar.css"
import {NavLink} from "react-router-dom"
import {useSelector} from "react-redux";


const navigations = [ /*TODO: MODIFY PATHS TO SINGLE PAGE STANDARD*/
    {name:"Home", path:"/home", icon:"fa-clinic-medical"},
    {name:"My Orders", path:"/dashboard/test", icon:"fa-clinic-medical"},
    {name:"Search Clinic", path:"/search-clinic", icon:"fa-clinic-medical"},
]

const Item = (props) => {
    return <div>
        <NavLink to={props.item.path}><div className={"sidebarItem"}>{props.item.name}</div></NavLink>
    </div>
}

const Sidebar = () => {
    const roleType = useSelector(state => state.role);

    return(
    <div className={"sidebar2"}>
        {navigations.map((item) => (
            <Item key={item.path} item={item} />
        ))}

        {roleType === "Medic" && <Item key={"Medic"} item={{name: "Create Order", path:"/dashboard/create-order", icon:""}} />}
    </div>
    )
}

export default Sidebar;