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
        <h2><spring:message code="medic-registration.body.form.title1"/></h2>
    </div>
    <div class="row justify-content-center">
        <h3><spring:message code="medic-registration.body.form.title2"/></h3>
    </div>

    <div class="container">
        <div class="row">
            <h6 class="justify-content-center align-self-center">I'm a: </h6>
            <div class="col">
                <ul class="nav nav-pills nav-justified" id="myTab" role="tablist">
                    <li class="nav-item">
                        <a class="nav-item nav-link active"
                           id="patient-tab" data-toggle="tab" href="<c:url value="#patient"/>">Patient</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"
                           id="medic-tab" data-toggle="tab" href="<c:url value="#medic"/>">Medic</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"
                           id="clinic-tab" data-toggle="tab" href="<c:url value="#clinic"/>">Clinic</a>
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
                                <f:label path="first_name" cssClass="bmd-label-floating">First Name</f:label>
                                <f:input type="text" cssClass="form-control" path="first_name" />
                            </fieldset>
                            <fieldset class="form-group">
                                <f:label path="last_name" cssClass="bmd-label-floating">Last Name</f:label>
                                <f:input type="text" cssClass="form-control" path="last_name" />
                            </fieldset>
                        </div>
                        <fieldset class="form-group row">
                            <f:label path="medical_insurance_plan" cssClass="bmd-label-floating">Medical Insurance Plan</f:label>
                            <f:input type="text" cssClass="form-control" path="medical_insurance_plan" />
                        </fieldset>
                        <fieldset class="form-group row">
                            <f:label path="medical_insurance_number" cssClass="bmd-label-floating">Medical Insurance Number</f:label>
                            <f:input type="text" cssClass="form-control" path="medical_insurance_number" />
                        </fieldset>
                        <input type="submit" value="Continue" name="submit_1" class="row btn btn-lg btn-light  bg-primary btn-block">
                    </f:form>
                </div>
                <!-- MEDIC REGISTRATION -->
                <div id="medic" class="tab-pane fade in">
                    <c:url var="registerMedic" value="/apply-as-medic"/>
                    <f:form action="${registerMedic}" class="form-signin" method="post" modelAttribute="applyMedicForm" enctype="multipart/form-data">
                        <div class="row">
                            <div class="col pl-0">
                            <fieldset class="form-group">
                                <label for="firstName" class="bmd-label-floating">First Name</label>
                                <f:input type="text" cssClass="form-control" required="required" id="firstName"  path="first_name"/>
                            </fieldset>
                            </div>
                            <div class="col">
                            <fieldset class="form-group">
                                <label for="lastName" class="bmd-label-floating">Last Name</label>
                                <f:input type="text" cssClass="form-control" required="required" id="lastName" path="last_name"/>
                            </fieldset>
                            </div>
                        </div>
                        <fieldset class="form-group row">
                            <label for="phoneNumber" class="bmd-label-floating">Phone Number</label>
                            <f:input type="tel" cssClass="form-control" required="required" id="phoneNumber" pattern="\+?[0-9\-]+" path="telephone"/>
                            <small class="text-muted">+[Country Code][Area Code][Phone Number]</small>
                        </fieldset>
                        <fieldset class="form-group row">
                            <label for="email" class="bmd-label-floating">Email Address</label>
                            <f:input type="text" cssClass="form-control" required="required" id="email" path="email"/>
                        </fieldset>
                        <fieldset class="form-group row">
                            <label for="licenseNumber" class="bmd-label-floating">License Number</label>
                            <f:input type="text" cssClass="form-control" required="required" pattern="[a-zA-Z0-9]*" id="licenseNumber" path="licence_number"/>
                            <small class="text-muted">Please enter your license number only with numbers and letters.</small>
                        </fieldset>
                        <fieldset class="mt-4 form-group">
                            <label for="identificationFile">Seal and Signature</label><br/>
                            <f:input required="required" type="file" path="identification" id="identificationFile"/><br/>
                            <small class="text-muted">Please upload a photo of your seal and signature</small>
                        </fieldset>


                        <label class="mb-4">Medical Fields</label>
                        <f:select id="medicalFields" cssClass="selectpicker" title="Choose Medical Fields" data-live-search="true" path="known_fields" data-style="btn-custom">
                            <f:options items="${fieldsList}" itemLabel="name" itemValue="id" />
                        </f:select>


                        <input type="submit" value="Continue" name="submit_2" class="row btn btn-lg btn-light  bg-primary btn-block">
                    </f:form>
                </div>


                <!-- CLINIC REGISTRATION -->
                <div id="clinic" class="tab-pane fade in">
                    <c:url var="registerClinic" value="/apply-as-clinic"/>
                    <f:form action="${registerClinic}" method="post" modelAttribute="applyClinicForm">
                        <fieldset class="form-group">
                            <label for="clinicName" class="bmd-label-floating">Clinic's Name</label>
                            <f:input type="text" class="form-control" id="clinicName" path="name"/>
                        </fieldset>
                        <fieldset class="form-group row">
                            <label for="phoneNumber" class="bmd-label-floating">Phone Number</label>
                            <f:input type="tel" class="form-control" id="phoneNumber" pattern="\+?[0-9\-]+" path="telephone"/>
                            <small class="text-muted">+[Country Code][Area Code][Phone Number]</small>
                        </fieldset>

                        <label>Study Types </label>
                        <f:select id="studyFields" class="selectpicker" title="choose your fields" data-live-search="true" path="available_studies" data-style="btn-custom">
                            <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                        </f:select>
                        <input type="submit" value="Continue" name="submit_3" class="row btn btn-lg btn-light  bg-primary btn-block">
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
