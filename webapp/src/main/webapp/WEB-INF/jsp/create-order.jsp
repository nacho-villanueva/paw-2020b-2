<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">

    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">

    <%@ include file="fragments/include-scripts.jsp"%>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/registration.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/createorder.css"/>">
    <!-- Bootstrap JS Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>

    <!-- Query: Get Clinic from study id -->
    <c:url var="getClinicByMedicalStudy" value="/api/data/clinic/get-clinics-by-medical-study"/>
    <spring:message code="create-order.body.form.clinicId.placeholder.enabled" var="clinicIdPlaceholderEnabled"/>
    <script type="text/javascript">
        $(document).ready(function(){

            let clinicSelect = $('#clinic');

            $('#medicalStudy').on('change', function(){
                let studyId = $(this).val();

                if(studyId>=0){
                    $.getJSON('${getClinicByMedicalStudy}',{
                        study : studyId
                    }, function(response) {
                        let clinicList = '';
                        let clinicLen = response.length;
                        for(let i =0; i<clinicLen;i++){
                            clinicList += '<option value="' + response[i].user_id + '">' + sanitize(response[i].name) + '</option>';
                        }

                        clinicSelect.html(clinicList);
                        clinicSelect.attr('disabled',false);
                        clinicSelect.selectpicker('refresh');
                    });
                }else{
                    clinicSelect.html('');
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

</head>
<body>

<div class="main-container">
    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
    <div class="row justify-content-center">
        <div class="card" style="width: 40em; margin-top: 2em;">
            <div class="card-body">
                <div class="row">
                    <p class="card-title h4 mx-auto mt-3"><spring:message code="create-order.body.form.title"/> </p>
                </div>

                <c:url var="post_createorder"  value="/create-order"/>
                <f:form action="${post_createorder}" method="post" modelAttribute="orderForm">
                    <fieldset class="form-group col-7">
                        <label for="medicName" class="bmd-label-static"><spring:message code="create-order.body.form.medicName.label"/> </label>
                        <input id="medicName" class="form-control" type="text" disabled placeholder="<c:out value="${loggedMedic.name}"/>"/>
                    </fieldset>
                    <hr class="divider"/>

                    <div class="row justify-content-center">
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientName.label"/> </label>
                                <f:input type="text" path="patientName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patientName" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                            <fieldset class="form-group">
                                <label class="bmd-label-floating"><spring:message code="create-order.body.form.patient_insurance_plan.label"/></label>
                                <f:input type="text" path="patient_insurance_plan" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patient_insurance_plan" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                        </div>
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientMail.label"/> </label>
                                <f:input type="email" path="patientEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patientEmail" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                            <fieldset class="form-group">
                                <label class="bmd-label-floating"><spring:message code="create-order.body.form.patient_insurance_number.label"/> </label>
                                <f:input type="text" path="patient_insurance_number" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patient_insurance_number" cssClass="invalid-feedback" element="small" />
                            </fieldset>

                        </div>
                    </div>

                    <hr class="mt-3 mb-2"/>

                    <div class="row">
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-static"><spring:message code="create-order.body.form.studyId.label"/></label>
                                <spring:message code="create-order.body.form.studyId.placeholder" var="studyIdPlaceholder"/>
                                <f:select title="${studyIdPlaceholder}" cssErrorClass="selectpicker form-control is-invalid" id="medicalStudy" cssClass="selectpicker form-control" data-live-search="true" path="studyId" data-style="btn-custon">
                                    <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                                </f:select>
                                <f:errors path="studyId" cssClass="invalid-feedback" element="small" />
                            </fieldset>
                        </div>
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-static"><spring:message code="create-order.body.form.clinicId.label"/> </label>
                                <f:select id="clinic" title="${clinicIdPlaceholderEnabled}" cssClass="selectpicker" cssErrorClass="selectpicker is-invalid" data-live-search="true" path="clinicId" disabled="true" data-style="btn-custom">
                                </f:select>
                                <small class="text-muted"><spring:message code="create-order.body.form.clinicId.format"/></small>
                                <f:errors path="clinicId" cssClass="invalid-feedback" element="small" /><br>
                            </fieldset>
                        </div>
                    </div>
                    <div class="row form-group">
                        <label class="bmd-label-static"><spring:message code="create-order.body.form.description.label"/></label>
                        <f:textarea path="description" cssStyle="resize: none" cssClass="form-control" cssErrorClass="form-control is-invalid" rows="10"/>
                        <f:errors path="description" cssClass="invalid-feedback" element="small" />
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

</body>
</html>