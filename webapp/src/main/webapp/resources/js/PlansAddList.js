ids = 0;
function addPlan(plan, onlyDraw){
    badgeId = "planBadge_" + ids

    const planListPillTemplate = $("<a />", {
        "id": badgeId,
        "onclick": "removeFromList(\"" + badgeId + "\")",
        "class": "mr-2 mb-1",
        "style": "display:none"
    }).append($("<span/>", {
        "class": "badge-md badge-pill badge-primary d-inline-block"
    }).text(plan).append($("<i/>", {
        "class": "fa fa-times ml-1"
    })));

    ids++;
    $("#plansList").append(planListPillTemplate);

    if(!onlyDraw) {
        const plansInputList = $("#plansInputList");
        var ival = plansInputList.val();
        if (ival === "")
            plansInputList.val(plan);
        else
            plansInputList.val(ival + "," + plan);
    }

    $("#"+badgeId).fadeIn(250);
}

function addPlanToList(){
    if(document.getElementById("addPlanInput").value !== "") {
        const addPlanInput = $("#addPlanInput");
        addPlan(addPlanInput.val().trim(), false);
        addPlanInput.val("")
    }

    return false;
}

function removeFromList(id){

    const plan = $("#"+id + " span:first-child").text();
    const plansInputList = $("#plansInputList");

    var plans = plansInputList.val().split(",");
    var newPlans = "";
    if (plans.length <= 1)
        plansInputList.val("")
    else{
        for(const p of plans){

            if(p !== plan)
                newPlans += p + ",";
        }
        newPlans = newPlans.slice(0,-1);
    }
    plansInputList.val(newPlans);
    $("#"+id).fadeOut(100, function () {$(this).remove();});
}

document.getElementById("plansInputList").onload = onLoadPlanList();

function onLoadPlanList(){
    const acceptedPlans = $("#plansInputList").val();
    if(acceptedPlans != null && acceptedPlans !== "") {
        const plans = acceptedPlans.split(",");
        for(const p of plans){
            addPlan(p, true);
        }
    }
}

document.getElementById("addPlanInput").onkeypress = function (e) {
    if(e.code === "Enter") {
        addPlanToList();
        return false;
    }
};