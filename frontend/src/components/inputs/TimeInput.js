import {Form} from "react-bootstrap";
import {useState} from "react";

function isDigit(s){
    return s >= '0' && s <= '9';
}

const TimeInput = (props) => {



    const [time, setTime] = useState('');

    const onTimeChange = (event) => {
        let j;
        let val = event.target.value;
        let newVal = val;
        const last = val[val.length - 1];

        const format = "hh:mm";

        const size = format.length < val.length ? format.length : val.length;
        if(last === ":" && val.length === 2) {
            val = "0" + val;
            newVal = val;
        }

        for(let i = 0; i < size; i++){
            if(format[i] === 'h'){
                if(!isDigit(val[i])) {
                    j = i - 1 < 0 ? 0 : i - 1;
                    newVal = val.slice(0, j);
                }
                if(newVal.length === 2){
                    let h = parseInt(newVal.substring(0, 2));
                    if(h > 24)
                        newVal = "24"
                }
            }
            else if(format[i] === 'm'){
                if(!isDigit(val[i])) {
                    j = i - 1 < 0 ? 0 : i - 1;
                    newVal = val.slice(0, j);
                }
                if(newVal.length === 5) {
                    const m = parseInt(newVal.substring(3, 5));
                    if (m > 59)
                        newVal = newVal.substring(0, 3) + "59";
                }
            }
        }

        for(let i = 0; i < size; i++){
            if(format[i] === ':'){
                if(isDigit(val[i]))
                    newVal = newVal.substring(0, i) + ":" + newVal.substring(i, newVal.length);
            }
        }

        if(val.length > format.length)
            newVal = val.slice(0,format.length);

        setTime(newVal);

        if(props.onChange !== null && newVal.length === format.length){
            props.onChange(newVal)
        }
    }


    return <Form.Control
        type="text" className={props.className}
        name={props.name} placeholder={props.placeholder}
        value={time} onChange={onTimeChange}
    />

}

export default TimeInput;