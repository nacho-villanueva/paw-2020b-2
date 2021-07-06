import {Form} from "react-bootstrap";
import { Trans, useTranslation } from 'react-i18next'


export function Day(props){
    const filters = props.filters;
    const item = props.item;

    const { t } = useTranslation();

    return(
        <tr name={"dayOp-"+item.id}>
            <th><Trans t={t} i18nKey={"days.day-"+item.id} >{item.name}</Trans></th>
            <th>
                <Form.Group controlId={"isAvailable" + item.id}>
                    <Form.Control defaultChecked={filters.schedule[item.id].checked}
                        type="checkbox" name={"isAvailable-" + item.id}
                    />
                </Form.Group>
            </th>
            <th>
                <Form.Group controlId={"day-" + item.id + "-ot"}>
                    <Form.Control
                        type="time" className="form-control time-input"
                        placeholder="00:00" maxLength="5"
                        name={"day-" + item.id + "-ot"}
                        defaultValue={filters.schedule[item.id].fromTime}
                        pattern="[0-9]{2}:[0-9]{2}"
                    />
                </Form.Group>
            </th>
            <th>
                <Form.Group controlId={"day-" + item.id + "-ct"}>
                    <Form.Control
                        type="time" className="form-control time-input"
                        placeholder="00:00" maxLength="5"
                        name={"day-" + item.id + "-ct"}
                        defaultValue={filters.schedule[item.id].toTime}
                        pattern="[0-9]{2}:[0-9]{2}"
                    />
                </Form.Group>
            </th>
        </tr>
    );
}