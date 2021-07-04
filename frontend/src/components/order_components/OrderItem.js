import {Roles} from "../../constants/Roles";
import {Button} from "react-bootstrap";
import {Link} from "react-router-dom";
export function OrderItem(props) {
    const order = props.order;
    const roleType = props.role;
    return(
        <aside>
            <li className="nav-item">
                <Link to={"/dashboard/view-study/"+order.url }>
                    <Button className="nav-link w-100" id={"order-"+props.index+"-tab"}
                        data-toggle="tab" role="tab" style={{cursor:"pointer"}}
                    >
                        <div className="d-flex w-100 justify-content-between">
                            <h5 className="mb-1">Study Type: {order.studyType}</h5>
                            <small>Date: {order.date}</small>
                        </div>
                        <div className="d-flex w-100 justify-content-between">
                            <p className="mb-1">
                            {(roleType === Roles.PATIENT || roleType === Roles.MEDIC) ?
                                "Clinic: "+order.clinic :
                                "Patient: " +order.patientName
                            }
                            </p>
                            <small>
                            {(roleType === Roles.CLINIC || roleType === Roles.PATIENT) ?
                                "Medic: " +order.medic :
                                "Patient: " +order.patientName
                            }
                            </small>
                        </div>
                    </Button>

                </Link>
            </li>
        </aside>
    );
}