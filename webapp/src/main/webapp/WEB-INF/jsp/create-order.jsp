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
                    <p class="card-title h4 mx-auto mt-3"><spring:message code="create-order.body.form.title"/> </p>
                </div>

                <c:url var="post_createorder"  value="/create-order"/>
                <f:form action="${post_createorder}" method="post" modelAttribute="orderForm">
                    <div class="border-1 border-secondary">
                        <label for="medicName" class="text-muted"><spring:message code="create-order.body.form.medicName.label"/> </label>
                        <p id="medicName" class="lead"><c:out value="${loggedMedic.name}"/> </p>
                    </div>
                    <hr class="divider"/>
                    <div class="row">
                        <fieldset class="form-group col">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientName.label"/> </label>
                            <f:input type="text" path="patientName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                            <f:errors path="patientName" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="form-group col">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientMail.label"/> </label>
                            <f:input type="email" path="patientEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                            <f:errors path="patientEmail" cssClass="text-danger" element="small" />
                        </fieldset>
                    </div>
                    <div class="row">
                        <fieldset class="form-group col ">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patient_insurance_plan.label"/></label>
                            <f:input type="text" path="patient_insurance_plan" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                            <f:errors path="patient_insurance_plan" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="form-group col">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patient_insurance_number.label"/> </label>
                            <f:input type="text" path="patient_insurance_number" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                            <f:errors path="patient_insurance_number" cssClass="text-danger" element="small" />
                        </fieldset>

                    </div>

                    <hr class="mt-3 mb-2"/>

                    <div class="row">
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-static"><spring:message code="create-order.body.form.studyId.label"/></label>
                                <spring:message code="create-order.body.form.studyId.placeholder" var="studyIdPlaceholder"/>
                                <f:select title="${studyIdPlaceholder}" cssErrorClass="selectpicker is-invalid" id="medicalStudy" cssClass="selectpicker" data-live-search="true" path="studyId" data-style="text-primary">
                                    <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                                </f:select>
                                <f:errors path="studyId" cssClass="text-danger" element="small" />
                            </fieldset>
                        </div>
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-static"><spring:message code="create-order.body.form.clinicId.label"/> </label>
                                <spring:message code="create-order.body.form.clinicId.placeholder.enabled" var="clinicIdPlaceholderEnabled"/>
                                <f:select id="clinic" title="${clinicIdPlaceholderEnabled}" cssClass="selectpicker" cssErrorClass="selectpicker is-invalid" data-live-search="true" path="clinicId" disabled="true" data-style="text-primary">
                                </f:select>
                                <small class="text-muted"><spring:message code="create-order.body.form.clinicId.format"/></small>
                                <br>
                                <f:errors path="clinicId" cssClass="text-danger" element="small" /><br>
                            </fieldset>
                        </div>
                    </div>
                    <div class="row form-group">
                        <label class="bmd-label-static"><spring:message code="create-order.body.form.description.label"/></label>
                        <f:textarea path="description" cssStyle="resize: none" cssClass="form-control" cssErrorClass="form-control is-invalid" rows="10"/>
                        <f:errors path="description" cssClass="text-danger" element="small" />
                    </div>


                    <hr class="mt-3 mb-2"/>

                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button"><spring:message code="create-order.body.form.button.cancel"/></a>
                    <button class="btn create-btn mt-4 mb-2 float-right" type="submit"><spring:message code="create-order.body.form.button.submit"/></button>

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