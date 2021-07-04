import apiInstance from "./"
import {store} from "../redux";

function checkUndefinedArray(array){
    var count = 0;
    for(var idx in array){
        if(array[idx] !== undefined){
            count++;
        }
    }
    return (count > 0);
}

export function parameterSerializer(params){
    let output = "";
    if(Object.keys(params).length !== 0){
        output += "?";
        for(var key of Object.keys(params)){
            let param = params[key];
            if(Array.isArray(param) && param && checkUndefinedArray(param)){
                output += key + "=";
                for(var idx in param){
                    let element = (param[idx] !== 0 ? encodeURIComponent(param[idx]) : "") + (idx < (param.length - 1) ? "," : "");
                    output += element;
                }
               output += "&";

            }else if(param !== undefined){
                output += key + "=" + encodeURIComponent(param);
                output += "&";
            }

        }
        output = output.slice(0, -1);
    }

    return output;
}

export function getAuthorizedImage(src, setImage){
    apiInstance.get( src )
    .then((r) => {
        let src = "data:image;base64," + r.data;
        setImage(src)
    })
    .catch((e) => { return undefined;});
}

export function parseHeadersLinks(link){
    let data = link.split(",");
    let parsedLinks = {};
    for(var d of data){
        let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(d);
        parsedLinks[linkInfo[2]] = linkInfo[1];
    }
    return parsedLinks;
}

export function findLastPageNumber(last){
    let sts = last.split("&");
    console.log("STRINGS", sts);
    for(var s of sts){
        if(!s.includes("per_page") && s.includes("page")){
            let aux = s.split("=");
            return aux[1];
        }
    }
    return 1;
}