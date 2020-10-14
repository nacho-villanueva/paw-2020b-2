function createDayEntry(dayIndex, day) {
    const row = $("<tr />");

    row.append(
        $("<td />", {text: day + ":" ,
            "class": "p-2 mr-2"})
    );

    var iot = "iOT_" + dayIndex;

    row.append(
        $("<td />").append( $("<fieldset />",{"class":"mr-3"}).append(
            $("<label />").append($("<small />", {text: strings["openTime"], "class": "text-muted"})).append($("<br />",)).append($("<input />", {"type": "text", "class":"form-control m-2 time-input", "placeholder": "00:00", "id":iot}))
        ))
    );

    var ict = "iCT_" + dayIndex;

    row.append(
        $("<td />").append( $("<fieldset />",{"class":"mr-3"}).append(
            $("<label />").append($("<small />", {text: strings["closeTime"], "class": "text-muted"})).append($("<input />", {"type": "text", "class":"form-control m-2 time-input","placeholder": "23:59", "id":ict}))
        ))
    );

    return row;
}

days = []

function loadDaysArray(){
    $("#openDaysSelect option").each(
        function () {
            days.push(this.text);
        }
    )
}

function onDayUpdate() {
    const dayHourList = $("#daysHourList");
    const daySelect = $("#openDaysSelect");
    dayHourList.empty();
    for(const d of daySelect.val()){
        dayHourList.append(createDayEntry(d, days[d]));
    }

}

$("#daysHourList").on("load", onLoadDaysPicker());
function onLoadDaysPicker(){
    let i;
    let ot;
    loadDaysArray();

    const daySelect = $("#openDaysSelect");
    var selectedDays = []
    for(i = 0; i < 7; i++){
        ot = $("#OT_" + i).val();
        if(ot !== "" && ot != null){
            selectedDays.push(i);
        }
    }

    daySelect.selectpicker("val", selectedDays);
    onDayUpdate();

    for(i = 0; i < 7; i++){
        ot = $("#OT_"+i).val();
        const ct = $("#CT_" + i).val();
        if(ot !== "" && ot != null){

            $("#iOT_"+i).val(ot);
            $("#iCT_"+i).val(ct);
            selectedDays.push(i);
        }
    }

}

function clearValues(){
    for(var i = 0; i < 7; i++){
        $("#OT_"+i).val("");
        $("#CT_"+i).val("")
    }
}

function beforeSubmit(){
    clearValues();
    $("#daysHourList").find("input").each(
        function () {
            $("#" + this.id.substring(1)).val(this.value);});
}


function onTimeChange(){
    var input = $("#"+this.id);
    if(!input.hasClass("time-input"))
        return
    var val = input.val();
    var newVal = val;
    var last = val[val.length - 1];

    var format = "hh:mm";

    var size = format.length < val.length ? format.length : val.length;
    if(last === ":" && val.length === 2) {
        val = "0" + val;
        newVal = val;
    }

    for(let i = 0; i < size; i++){
        if(format[i] === 'h'){
            if(!isDigit(val[i])) {
                var j = i - 1 < 0 ? 0 : i - 1;
                newVal = val.slice(0, j);
            }
            if(newVal.length === 2){
                var h = parseInt(newVal.substring(0,2));
                if(h > 24)
                    newVal = "24"
            }
        }
        else if(format[i] === 'm'){
            if(!isDigit(val[i])) {
                var j = i - 1 < 0 ? 0 : i - 1;
                newVal = val.slice(0, j);
            }
            if(newVal.length === 5) {
                var m = parseInt(newVal.substring(3, 5));
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

    input.val(newVal);
}

$(document).on('input', 'input[type=text]', onTimeChange);

function isDigit(s){
    return s >= '0' && s <= '9';
}