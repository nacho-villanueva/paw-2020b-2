import {Button, Card, Col, Row} from "react-bootstrap";

import "./Style/HomeOrders.css"
import {useEffect, useState} from "react";
import {GetAllOrders} from "../api/Orders";
import {getAllStudyTypes} from "../api/CustomFields";
import {Trans, useTranslation} from "react-i18next";
import {useHistory} from "react-router-dom";
import {useSelector} from "react-redux";
import {Roles} from "../constants/Roles";

const OrderItem = (props) => {

    const {t} = useTranslation()
    const history = useHistory();

    const role = useSelector(state => state.auth.role);

    const handleClick = () => {

        let url = props.order.url.split('/');
        url = url[url.length - 1];
        console.log(url)

        history.push("/dashboard/view-study/"+ url)
    }

    return (
        <div>
            <Button variant={"primary"} className={"orderItem"} onClick={handleClick}>
                <div className={"orderRow"}>
                    <p style={{fontSize:"1.25em"}}><Trans t={t} i18nKey={"dashboard.home.order.study"}/>: {props.order.studyType.name}</p>
                </div>
                <div className={"orderRow"} style={{fontWeight:"400"}}>
                    <p><Trans t={t} i18nKey={"dashboard.home.order.date"}/>: {props.order.date}</p>

                    {role === Roles.PATIENT && (
                        <div>
                            <p><Trans t={t} i18nKey={"dashboard.home.order.medic"}/>: {props.order.medic.name}</p>
                            <p><Trans t={t} i18nKey={"dashboard.home.order.clinic"}/>: {props.order.clinic.name}</p>
                        </div>
                    )}

                    {role === Roles.MEDIC && (
                        <div>
                            <p><Trans t={t} i18nKey={"dashboard.home.order.patient"}/>: {props.order.patientName}</p>
                            <p><Trans t={t} i18nKey={"dashboard.home.order.clinic"}/>: {props.order.clinic.name}</p>
                        </div>
                        )}

                    {role === Roles.CLINIC && (
                        <div>
                            <p><Trans t={t} i18nKey={"dashboard.home.order.medic"}/>: {props.order.medic.name}</p>
                            <p><Trans t={t} i18nKey={"dashboard.home.order.patient"}/>: {props.order.patientName}</p>
                        </div>
                        )}

                </div>
            </Button>
            <hr />
        </div>
    )
}

const HomeOrders = () => {
    const {t} = useTranslation()

    const [allOrders, setAllOrders] = useState([])

    useEffect(() => {
        let loaded = false
        if(!loaded) {
            GetAllOrders(1, 6).then((r) => {setAllOrders(r);});
        }
        return () => {loaded = true}
    }, [])

    return (
        <div className={"ordersContainer"}>
            <Card className={"ordersCard"}>
                <h4><Trans t={t} i18nKey={"dashboard.home.title"}/></h4>
                <hr className={"divider"} />

                <div className={"orderList"}>
                    {allOrders.map((o) => <OrderItem key={o.id} order={o} />)}
                </div>
            </Card>
        </div>
    )
}

export default HomeOrders;