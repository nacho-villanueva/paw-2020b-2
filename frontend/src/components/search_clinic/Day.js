import {Form} from "react-bootstrap";
import { Trans, useTranslation } from 'react-i18next'


export function Day(props){
    const filters = props.filters;
    const setFilters =props.setFilters;

    const item = props.item;

    const { t } = useTranslation();

    return(
        <tr name={"dayOp-"+item.id}>
            <th><Trans t={t} i18nKey={"days.day-"+item.id} >{item.name}</Trans></th>
            <th>
                <Form.Group controlId={"isAvailable" + item.id}>
                    <Form.Control defaultChecked={filters.schedule[item.id].checked}
                        type="checkbox" name={"isAvailable-" + item.id}
                        onChange={(val) => {
                            let aux = filters.schedule;
                            aux[item.id].checked = val.target.checked
                            setFilters({...filters, schedule: aux})
                        }}
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
                        onChange={(val) => {
                            let aux = filters.schedule;
                            aux[item.id].fromTime = val.target.value
                            setFilters({...filters, schedule: aux})
                        }}
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
                        onChange={(val) => {
                            let aux = filters.schedule;
                            aux[item.id].toTime = val.target.value
                            setFilters({...filters, schedule: aux})
                        }}
                    />
                </Form.Group>
            </th>
        </tr>
    );
}