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