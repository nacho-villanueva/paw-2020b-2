<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="<c:url value="/resources/css/registration.css"/>">
</head>
<body>
<div class="background"></div>
<div class="p-5 col-auto card main-container mx-auto" >
    <div class="row justify-content-center">
        <h2><spring:message code="complete-registration.body.form.title1"/></h2>
    </div>
    <div class="row justify-content-center">
        <h3><spring:message code="complete-registration.body.form.title2"/></h3>
    </div>

    <div class="container">
        <div class="row">
            <h6 class="justify-content-center align-self-center"><spring:message code="complete-registration.body.form.ima"/></h6>
            <div class="col">
                <ul class="nav nav-pills nav-justified" id="myTab" role="tablist">
                    <li class="nav-item">
                        <a class="nav-item nav-link active"
                           id="patient-tab" data-toggle="tab" href="<c:url value="#patient"/>"><spring:message code="complete-registration.body.form.patient"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"
                           id="medic-tab" data-toggle="tab" href="<c:url value="#medic"/>"><spring:message code="complete-registration.body.form.medic"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"
                           id="clinic-tab" data-toggle="tab" href="<c:url value="#clinic"/>"><spring:message code="complete-registration.body.form.clinic"/></a>
                    </li>
                </ul>
                </div>
            </div>

            <div class="tab-content">
                <!-- PATIENT REGISTRATION -->
                <div id="patient" class="tab-pane fade in show active">
                    <c:url value="/register-patient" var="register" />
                    <f:form action="${register}" method="post" modelAttribute="registerPatientForm">
                        <div class="row">
                            <fieldset class="form-group">
                                <f:label path="first_name" cssClass="bmd-label-floating"><spring:message code="complete-registration.body.form.first_name.label"/></f:label>
                                <f:input type="text" cssClass="form-control" path="first_name" />
                            </fieldset>
                            <fieldset class="form-group">
                                <f:label path="last_name" cssClass="bmd-label-floating"><spring:message code="complete-registration.body.form.last_name.label"/></f:label>
                                <f:input type="text" cssClass="form-control" path="last_name" />
                            </fieldset>
                        </div>
                        <fieldset class="form-group row">
                            <f:label path="medical_insurance_plan" cssClass="bmd-label-floating"><spring:message code="complete-registration.body.form.patient.patient_insurance_plan.label"/></f:label>
                            <f:input type="text" cssClass="form-control" path="medical_insurance_plan" />
                        </fieldset>
                        <fieldset class="form-group row">
                            <f:label path="medical_insurance_number" cssClass="bmd-label-floating"><spring:message code="complete-registration.body.form.patient.patient_insurance_number.label"/></f:label>
                            <f:input type="text" cssClass="form-control" path="medical_insurance_number" />
                        </fieldset>
                        <input type="submit" value="<spring:message code='complete-registration.body.form.submit'/>" name="submit_1" class="row btn btn-lg btn-light  bg-primary btn-block">
                    </f:form>
                </div>
                <!-- MEDIC REGISTRATION -->
                <div id="medic" class="tab-pane fade in">
                    <c:url var="registerMedic" value="/apply-as-medic"/>
                    <f:form action="${registerMedic}" class="form-signin" method="post" modelAttribute="applyMedicForm" enctype="multipart/form-data">
                        <div class="row">
                            <div class="col pl-0">
                            <fieldset class="form-group">
                                <label for="firstNameMedic" class="bmd-label-floating"><spring:message code="complete-registration.body.form.first_name.label"/></label>
                                <f:input type="text" cssClass="form-control" required="required" id="firstNameMedic"  path="first_name"/>
                            </fieldset>
                            </div>
                            <div class="col">
                            <fieldset class="form-group">
                                <label for="lastNameMedic" class="bmd-label-floating"><spring:message code="complete-registration.body.form.last_name.label"/></label>
                                <f:input type="text" cssClass="form-control" required="required" id="lastNameMedic" path="last_name"/>
                            </fieldset>
                            </div>
                        </div>
                        <fieldset class="form-group row">
                            <label for="phoneNumberMedic" class="bmd-label-floating"><spring:message code="complete-registration.body.form.telephone.label"/></label>
                            <f:input type="tel" cssClass="form-control" required="required" id="phoneNumberMedic" pattern="\+?[0-9\-]+" path="telephone"/>
                        </fieldset>
                        <fieldset class="form-group row">
                            <label for="licenseNumber" class="bmd-label-floating"><spring:message code="complete-registration.body.form.medic.licence_number.label"/></label>
                            <f:input type="text" cssClass="form-control" required="required" pattern="[a-zA-Z0-9]*" id="licenseNumber" path="licence_number"/>
                            <small class="text-muted"><spring:message code="complete-registration.body.form.medic.licence_number.format"/></small>
                        </fieldset>
                        <fieldset class="mt-4 form-group">
                            <label for="identificationFile"><spring:message code="complete-registration.body.form.medic.identification.label"/></label><br/>
                            <f:input required="required" type="file" path="identification" id="identificationFile"/><br/>
                            <small class="text-muted"><spring:message code="complete-registration.body.form.medic.identification.format"/></small>
                        </fieldset>


                        <label class="mb-4"><spring:message code="complete-registration.body.form.medic.known_fields.label"/></label>
                        <spring:message code='complete-registration.body.form.medic.known_fields.placeholder' var="medicalFieldsPlaceholder"/>
                        <f:select id="medicalFields" cssClass="selectpicker" title="${medicalFieldsPlaceholder}" data-live-search="true" path="known_fields" data-style="btn-custom">
                            <f:options items="${fieldsList}" itemLabel="name" itemValue="id" />
                        </f:select>
                        <a href="<c:url value='/create-field' />"><p><spring:message code="complete-registration.body.form.medic.add_medical_field" /></p></a>

                        <input type="submit" value="<spring:message code='complete-registration.body.form.submit'/>" name="submit_2" class="row btn btn-lg btn-light  bg-primary btn-block">
                    </f:form>
                </div>


                <!-- CLINIC REGISTRATION -->
                <div id="clinic" class="tab-pane fade in">
                    <c:url var="registerClinic" value="/apply-as-clinic"/>
                    <f:form action="${registerClinic}" method="post" modelAttribute="applyClinicForm">
                        <fieldset class="form-group">
                            <label for="clinicName" class="bmd-label-floating"><spring:message code='complete-registration.body.form.clinic.name.label'/></label>
                            <f:input type="text" class="form-control" id="clinicName" path="name"/>
                        </fieldset>
                        <fieldset class="form-group row">
                            <label for="phoneNumber" class="bmd-label-floating"><spring:message code='complete-registration.body.form.telephone.label'/></label>
                            <f:input type="tel" class="form-control" id="phoneNumber" pattern="\+?[0-9\-]+" path="telephone"/>
                        </fieldset>

                        <label><spring:message code='complete-registration.body.form.clinic.available_studies.label'/></label>
                        <spring:message code='complete-registration.body.form.clinic.available_studies.placeholder' var="studiesPlaceholder"/>
                        <f:select id="studyFields" class="selectpicker" title="${studiesPlaceholder}" data-live-search="true" path="available_studies" data-style="btn-custom">
                            <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                        </f:select>
                        <a href="<c:url value='/create-type' />"><p><spring:message code="complete-registration.body.form.clinic.add_medical_study" /></p></a>

                        <input type="submit" value="<spring:message code='complete-registration.body.form.submit'/>" name="submit_3" class="row btn btn-lg btn-light  bg-primary btn-block">
                    </f:form>
                </div>
            </div>
        </div>
</div>

<%@ include file="fragments/include-scripts.jsp"%><!-- bootstrap-select JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js" integrity="sha384-SfMwgGnc3UiUUZF50PsPetXLqH2HSl/FmkMW/Ja3N2WaJ/fHLbCHPUsXzzrM6aet" crossorigin="anonymous"></script>
<script>
var hash = window.location.hash;
hash && $('ul.nav a[href="' + hash + '"]').tab('show');
</script>
</body>
</html>
