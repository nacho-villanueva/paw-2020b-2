import {useEffect, useState} from "react";
import "./style/EditField.css"
import {Trans, useTranslation} from "react-i18next";

const EditFieldRow = (props) => {

    const {t} = useTranslation()

    const [edit, setEdit] = useState(false)

    useEffect(() => {
        if(!props.modified)
            setEdit(false)
    }, [props.modified])

    return <tr className={"fieldRow"}>
        <td className={"fieldName"}>{props.name}</td>
        <td className={"fieldInput"}>

            {!edit &&
            <div className={"field"}>
                {(props.type === "text") &&
                <span className={"fieldText"}>{props.field}</span>
                }

                {(props.type === "image") &&
                    <img src={props.field} alt={"Identification"} className={"fieldImage"}/>
                }

                {(props.type === "list") && (
                    <ul>
                        {Array.from(props.field, (o) => {return <li key={o.id}>{o.name}</li>})}
                    </ul>)
                }

                {(props.type === "hours") && (props.field !== null) && (
                    <ul>
                        {Array.from(props.field, (o) => {return <li key={o.day}>{o.day}: {o.openTime} - {o.closeTime}</li>})}
                    </ul>
                )}

            </div>
            }

            {edit && props.children}
        </td>
        <td>
            {!edit &&
                <a href={"#/"} onClick={() => {
                    setEdit(true);
                    if(props.onEdit)
                        props.onEdit()}}>
                    <Trans t={t} i18nKey={"edit-profile.edit"} />
                </a>
            }
        </td>
    </tr>

}

export default EditFieldRow;