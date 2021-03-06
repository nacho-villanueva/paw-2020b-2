import {Roles} from "../../constants/Roles";
import {Button} from "react-bootstrap";
import {Link} from "react-router-dom";
import { Trans, useTranslation } from 'react-i18next'

export function OrderItem(props) {

    const { t } = useTranslation();

    const order = props.order;
    let url = order.url.split('/');
    url = url[url.length -1];
    const roleType = props.role;
    return(

            <li className="nav-item order-item">
                <Link to={"/dashboard/view-study/"+url }>
                    <Button className="nav-link w-100" id={"order-"+props.index+"-tab"}
                        data-toggle="tab" role="tab" style={{cursor:"pointer"}}
                    >
                        <div className="d-flex w-100 justify-content-between">
                            <h5 className="mb-1"><Trans t={t} i18nKey="my-orders.orders.order.study-type" values={{studyType: order.studyType}} /></h5>
                            <small><Trans t={t} i18nKey="my-orders.orders.order.date" values={{date: order.date}} /></small>
                        </div>
                        <div className="d-flex w-100 justify-content-between">
                            <p className="mb-1">
                            <Trans t={t} i18nKey="my-orders.orders.order.patient" values={{patient: order.patientName}} />
                            </p>
                        </div>
                    </Button>

                </Link>
            </li>

    );
}