<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Query: Get Clinic from study id -->
    <c:url var="getClinicByMedicalStudy" value="/api/data/clinic/get-clinics-by-medical-study"/>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/createorder.css"/>">
    <title>Create Order</title>
</head>
<body>

<div id="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp">
        <jsp:param name="current" value="create-order"/>
    </jsp:include>

<div id="content-wrapper" class="d-flex flex-column">
    <div class="row justify-content-center">
        <div class="card" style="width: 40em; margin-top: 2em;">
            <div class="card-body">
                <div class="row">
                    <p class="card-title h4 mx-auto mt-3">Create New Medical Order</p>
                </div>

                <c:url var="post_createorder"  value="/create-order"/>
                <f:form action="${post_createorder}" method="post" modelAttribute="orderForm">
                    <div class="border-1 border-secondary">
                        <label for="medicName" class="text-muted">Medic:</label>
                        <p id="medicName" class="lead"><c:out value="${loggedMedic.name}"/> </p>
                    </div>
                    <hr class="divider mt-0 p-0"/>

                    <div class="row justify-content-center">
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-floating">Patient's name</label>
                                <f:input type="text" path="patientName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patientName" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                            <fieldset class="form-group">
                                <label class="bmd-label-floating">Patient's insurance plan</label>
                                <f:input type="text" path="patient_insurance_plan" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patient_insurance_plan" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                        </div>
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-floating">Patient's email</label>
                                <f:input type="email" path="patientEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patientEmail" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                            <fieldset class="form-group">
                                <label class="bmd-label-floating">Patient's insurance number</label>
                                <f:input type="text" path="patient_insurance_number" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patient_insurance_number" cssClass="invalid-feedback" element="small" />
                            </fieldset>

                        </div>
                    </div>

                    <hr class="mt-3 mb-2"/>

                    <div class="row">
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-static">Study Type </label>
                                <f:select id="medicalStudy" cssClass="selectpicker" cssErrorClass="selectpicker is-invalid" title="Choose Study Type" data-live-search="true" path="studyId" data-style="text-primary">
                                    <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                                </f:select>
                                <f:errors path="studyId" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                        </div>
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-static">Medical Clinic </label>
                                <f:select id="clinic" class="selectpicker" title="Choose Study First" data-live-search="true" data-style="text-primary" path="clinicId" disabled="true"/>
                                <f:errors path="clinicId" cssClass="invalid-feedback" element="small" /><br>
                            </fieldset>
                        </div>
                    </div>
                    <div class="row form-group">
                        <label class="bmd-label-static">Order Description</label>
                        <f:textarea path="description" cssStyle="width: 100%" cssClass="form-control" cssErrorClass="form-control is-invalid" rows="10"/>
                        <f:errors path="description" cssClass="invalid-feedback" element="small" />
                    </div>


                    <hr class="mt-3 mb-2"/>

                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button">Cancel</a>
                    <button class="btn create-btn mt-4 mb-2 float-right" type="submit">Create Order</button>

                    <datalist id="studiesList">
                        <c:forEach var = "i" items="${studiesList}">
                        <option value="<c:out value = "${i}"/>">
                            </c:forEach>
                    </datalist>
                </f:form>
            </div>
        </div>
    </div>
</div>
</div>>

<%@ include file="fragments/include-scripts.jsp"%>
<script type="text/javascript">
    $(document).ready(function(){

        $('#medicalStudy').on('change', function(){
            let studyId = $(this).val();
            let clinicSelect = $('#clinic');

            if(studyId>=0){
                $.getJSON('${getClinicByMedicalStudy}',{
                    study : studyId
                }, function(response) {
                    let clinicList = '';
                    let clinicLen = response.length;
                    for(let i =0; i<clinicLen;i++){
                        clinicList += '<option value="' + response[i].user_id + '">' + sanitize(response[i].name) + '</option>';
                    }
                    clinicList += '</option>';

                    clinicSelect.html(clinicList);
                    clinicSelect.attr('disabled',false);
                    clinicSelect.selectpicker('refresh');
                });
            }else{
                clinicSelect.attr('disabled',true);
                clinicSelect.selectpicker('refresh');
            }
        });
    });

    // temporal string sanitizer, from https://stackoverflow.com/questions/2794137/sanitizing-user-input-before-adding-it-to-the-dom-in-javascript
    // based on https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html
    function sanitize(string) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#x27;',
            "/": '&#x2F;',
        };
        const reg = /[&<>"'/]/ig;
        return string.replace(reg, (match)=>(map[match]));
    }
</script>
</body>
</html>