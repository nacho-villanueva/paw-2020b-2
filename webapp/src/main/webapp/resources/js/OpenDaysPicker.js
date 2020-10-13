function createDayEntry(dayIndex, day) {
    const row = $("<tr />");

    row.append(
        $("<td />", {text: day + ":" ,
            "class": "p-2 mr-2"})
    );

    row.append(
        $("<td />").append( $("<fieldset />",{"class":"mr-3"}).append(
            $("<label />").append($("<small />", {text: strings["openTime"], "class": "text-muted"})).append($("<br />",)).append($("<input />", {"type": "time", "class":"form-control m-2", "id":"iOT_" + dayIndex}))
        ))
    );

    row.append(
        $("<td />").append( $("<fieldset />",{"class":"mr-3"}).append(
            $("<label />").append($("<small />", {text: strings["closeTime"], "class": "text-muted"})).append($("<input />", {"type": "time", "class":"form-control m-2", "id":"iCT_" + dayIndex}))
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