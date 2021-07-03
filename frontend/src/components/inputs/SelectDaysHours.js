import {Form} from "react-bootstrap";
import MultiSelect from "react-multi-select-component";
import {useState} from "react";
import TimeInput from "./TimeInput";
import "./style/SelectDaysHours.css"

const SelectDaysHours = (props) => {

    const [value, setValue] = useState([])

    let onChange = (x) => {
        setValue(x);
        if(props.onChange !== null)
            props.onChange(x)
    };

    const getDayIndex = (day) => {
        for (let i=0; i < value.length; i++){
            if(value[i].day === day)
                return i;
        }
        return -1;
    }

    const updateDays = (days) => {
        let newValue = [];
        for(let d of days){
            let filter = value.filter((x) => x.day === d.value);
            if(filter.length === 1) {
                newValue.push(filter[0])
            }
            else{
                newValue.push({
                    day: d.value,
                    openTime: "",
                    closeTime: ""
                })
            }
        }
        onChange(newValue)
    }

    const updateOpenHour = (day, hour) => {
        let newValue = [...value];
        newValue[getDayIndex(day)].openTime = hour + ":00";
        onChange(newValue)
    }

    const updateCloseHour = (day, hour) => {
        let newValue = [...value];
        newValue[getDayIndex(day)].closeTime = hour + ":00";
        onChange(newValue)
    }

    const selectedPlaceholder = props.selectPlaceholder !== null ? props.selectPlaceholder : "Select Days";

    const dropdownOverrideStrings = {
        "selectSomeItems": selectedPlaceholder
    }

    const days = [
        { label: "Monday", value: 0 },
        { label: "Tuesday", value: 1 },
        { label: "Wednesday", value: 2 },
        { label: "Thursday", value: 3 },
        { label: "Friday", value: 4 },
        { label: "Saturday", value: 5 },
        { label: "Sunday", value: 6 },
    ];

    const [selectedDays, setSelectedDays] = useState([]);

    return <div>
        <MultiSelect
            options={days}
            value={selectedDays}
            onChange={(days) => {
                setSelectedDays(days);
                updateDays(days);
            }}
            labelledBy="Select"
            hasSelectAll={false}
            disableSearch={true}
            overrideStrings={dropdownOverrideStrings}
            style={props.selectStyle}
        />
        <table>
            <tbody>
                {selectedDays.sort((a, b) => a.value - b.value).map((x, i) => {return <tr key={x.value}>
                    <th><p>{x.label}</p></th>
                    <th><div className={"time-picker"}><Form.Label className="bmd-label-static">Opening Hour</Form.Label>
                        <TimeInput onChange={(hour) => {updateOpenHour(x.value, hour)}} placeholder={"00:00"}/>
                    </div></th>
                    <th><div className={"time-picker"}><Form.Label className="bmd-label-static">Closing Hour</Form.Label>
                        <TimeInput onChange={(hour) => {updateCloseHour(x.value, hour)}} placeholder={"23:59"}/>
                    </div></th>
                </tr>})}
            </tbody>
        </table>
    </div>
}

export default SelectDaysHours;

