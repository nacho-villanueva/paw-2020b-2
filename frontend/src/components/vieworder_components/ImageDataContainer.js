import { useState } from "react";
import { getBase64Image } from "../../api/utils";

export function ImageDataContainer(props){
    const src = props.src;
    const alt = props.alt;
    const id = props.id;
    const [data, setData] = useState('');
    getBase64Image(src, setData);
    return(
        <img src={`data:image/jpeg;base64,${data}`} alt={alt}/>
    );
}