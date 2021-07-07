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

export function prepareViewStudyUrl(url){
    let sts = url.split("/");
    return sts[sts.length - 1];
}

export function getValueFromEvent(name, event){
    for(var t of event.target){
        if(t.name === name){
            //console.log("NAME", t.name);
            //console.log("VALUE", t.value);

            return t.value;
        }
    }
}

export function getFilesFrom(name,event){
    let files = [];
    let i = 0
    for(var t of event.target){
        if(t.name === name){
            for(var f of t['files']){
                files[i++] = f;
            }
        }
    }
    return files;
}

export function convertToBase64(file){
    return new Promise((resolve) => {
        let fileReader = new FileReader();
        fileReader.onload = (e) => resolve(fileReader.result.split(",")[1]);
        fileReader.readAsDataURL(file);
    });
    //return btoa(decodeURIComponent(encodeURIComponent(file)));
}

export function getBase64Image(src, setData){
    apiInstance
    .get(src,
        {
            headers: {
                'Accept': 'image/*;encoding=base64'
            }
        }
    )
    .then((res) => {
        console.log(res);
    })
    .catch((e) => {
        console.log(e);
    });
}

export function isValidImage(type){
    return (type === "image/jpeg" || type === "image/png" || type === "image/jpg");
}

export function getDaySchedule(event, idx){
    let dayOp = {
        checked: '',
        fromTime: '',
        toTime: ''
    };

    let avail = "isAvailable"+idx;
    let ot = "day-"+idx+"-ot";
    let ct = "day-"+idx+"-ct";
    for(let idx = 0; idx <= event.target.length; idx++){

        let t = event.target[idx];
        if(t.hasOwnProperty('name')){
            if(t.name === ot){
                console.log(ot, t.value);
                dayOp.fromTime = t.value;
            }else if(t.name === ct){
                console.log(ct, t.value);
                dayOp.toTime = t.value;
            }else if(t.name === avail){
                console.log(avail, t.checked);
                dayOp.checked = t.checked;
            }
        }
    }

    return dayOp;
}