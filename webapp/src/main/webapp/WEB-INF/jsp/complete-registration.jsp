<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                                <f:input type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" path="first_name" />
                                <f:errors path="first_name" cssClass="text-danger" element="small" />
                            </fieldset>
                            <fieldset class="form-group">
                                <f:label path="last_name" cssClass="bmd-label-floating"><spring:message code="complete-registration.body.form.last_name.label"/></f:label>
                                <f:input type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" path="last_name" />
                                <f:errors path="last_name" cssClass="text-danger" element="small" />
                            </fieldset>
                        </div>
                        <fieldset class="form-group row">
                            <f:label path="medical_insurance_plan" cssClass="bmd-label-floating"><spring:message code="complete-registration.body.form.patient.patient_insurance_plan.label"/></f:label>
                            <f:input type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" path="medical_insurance_plan" />
                            <f:errors path="medical_insurance_plan" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="form-group row">
                            <f:label path="medical_insurance_number" cssClass="bmd-label-floating"><spring:message code="complete-registration.body.form.patient.patient_insurance_number.label"/></f:label>
                            <f:input type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" path="medical_insurance_number" />
                            <f:errors path="medical_insurance_number" cssClass="text-danger" element="small" />
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
                                <f:input type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" id="firstNameMedic"  path="first_name"/>
                                <f:errors path="first_name" cssClass="text-danger" element="small" />
                            </fieldset>
                            </div>
                            <div class="col">
                            <fieldset class="form-group">
                                <label for="lastNameMedic" class="bmd-label-floating"><spring:message code="complete-registration.body.form.last_name.label"/></label>
                                <f:input type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" id="lastNameMedic" path="last_name"/>
                                <f:errors path="last_name" cssClass="text-danger" element="small" />
                            </fieldset>
                            </div>
                        </div>
                        <fieldset class="form-group row">
                            <label for="phoneNumberMedic" class="bmd-label-floating"><spring:message code="complete-registration.body.form.telephone.label"/></label>
                            <f:input type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" id="phoneNumberMedic" pattern="\+?[0-9\-]+" path="telephone"/>
                            <f:errors path="telephone" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="form-group row">
                            <label for="licenseNumber" class="bmd-label-floating"><spring:message code="complete-registration.body.form.medic.licence_number.label"/></label>
                            <f:input type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" pattern="[a-zA-Z0-9]*" id="licenseNumber" path="licence_number"/>
                            <small class="text-muted"><spring:message code="complete-registration.body.form.medic.licence_number.format"/></small>
                            <f:errors path="licence_number" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="mt-4 form-group">
                            <label for="identificationFile"><spring:message code="complete-registration.body.form.medic.identification.label"/></label><br/>
                            <f:input required="required" type="file" cssErrorClass="form-control is-invalid" path="identification" id="identificationFile"/><br/>
                            <small class="text-muted"><spring:message code="complete-registration.body.form.medic.identification.format"/></small>
                            <f:errors path="identification" cssClass="text-danger" element="small" />
                        </fieldset>


                        <label class="mb-4"><spring:message code="complete-registration.body.form.medic.known_fields.label"/></label>
                        <spring:message code='complete-registration.body.form.medic.known_fields.placeholder' var="medicalFieldsPlaceholder"/>
                        <f:select id="medicalFields" cssClass="selectpicker" title="${medicalFieldsPlaceholder}" data-live-search="true" path="known_fields" data-style="btn-custom">
                            <f:options items="${fieldsList}" itemLabel="name" itemValue="id" />
                        </f:select>
                        <a href="<c:url value='/create-field' />"><p><spring:message code="complete-registration.body.form.medic.add_medical_field" /></p></a>
                        <f:errors path="known_fields" cssClass="text-danger" element="small" />

                        <input type="submit" value="<spring:message code='complete-registration.body.form.submit'/>" name="submit_2" class="row btn btn-lg btn-light  bg-primary btn-block">
                    </f:form>
                </div>


                <!-- CLINIC REGISTRATION -->
                <div id="clinic" class="tab-pane fade in">

                    <c:url var="registerClinic" value="/apply-as-clinic"/>
                    <f:form action="${registerClinic}" method="post" id="clinicForm" modelAttribute="applyClinicForm">
                        <fieldset class="form-group">
                            <label for="clinicName" class="bmd-label-floating"><spring:message code='complete-registration.body.form.clinic.name.label'/></label>
                            <f:input type="text" class="form-control" cssErrorClass="form-control is-invalid" id="clinicName" path="name"/>
                            <f:errors path="name" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="form-group">
                            <label for="phoneNumber" class="bmd-label-floating"><spring:message code='complete-registration.body.form.telephone.label'/></label>
                            <f:input type="tel" class="form-control" cssErrorClass="form-control is-invalid" id="phoneNumber" pattern="\+?[0-9\-]+" path="telephone"/>
                            <f:errors path="telephone" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="form-group">
                            <label><spring:message code='complete-registration.body.form.clinic.available_studies.label'/></label>
                            <spring:message code='complete-registration.body.form.clinic.available_studies.placeholder' var="studiesPlaceholder"/>
                            <f:select id="studyFields" class="selectpicker" cssErrorClass="selectpicker is-invalid" title="${studiesPlaceholder}" data-live-search="true" path="available_studies" data-style="btn-custom">
                                <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                            </f:select>
                            <br>
                            <f:errors path="available_studies" cssClass="text-danger" element="small" />
                        </fieldset>
                        <br>

                        <!-- Accepted Medical Plans -->
                        <div>
                            <fieldset class="input-group pt-4">
                                    <label for="addPlanInput" class="bmd-label-static"><spring:message code="complete-registration.body.form.clinic.accepted_plans.label" /></label>
                                    <input id="addPlanInput" type="text" class="form-control">
                                    <div class="input-group-append">
                                        <input class="btn btn-primary" id="enter" type="button" onclick="addPlanToList();" value="Add" />
                                    </div>
                                <f:hidden id="plansInputList" path="accepted_plans" onload="onLoadPlanList(${applyClinicForm.accepted_plans})" value="" />
                            </fieldset>

                            <div id="plansList" class="mb-2 mt-2">
                                <small class="text-muted"><spring:message code="complete-registration.body.form.clinic.accepted_plans.confirmation" />: </small>
                                <template id="planPillTemplate">
                                    <a class="mr-2 mb-1"><span class="badge-md badge-pill badge-primary"><i class="fa fa-times ml-1"></i></span></a>
                                </template>
                            </div>
                        </div>
                        <br>

                    <div>
                        <!-- Open Day and Time Picker -->
                            <fieldset class="form-group">
                                <label><spring:message code="complete-registration.body.form.clinic.open_days.label" /></label>
                                <f:select path="open_days" id="openDaysSelect" class="selectpicker" onchange="onDayUpdate()" data-style="text-primary" multiple="true">
                                    <f:option value="0"><spring:message code="days.day-0" /></f:option>
                                    <f:option value="1"><spring:message code="days.day-1" /></f:option>
                                    <f:option value="2"><spring:message code="days.day-2" /></f:option>
                                    <f:option value="3"><spring:message code="days.day-3" /></f:option>
                                    <f:option value="4"><spring:message code="days.day-4" /></f:option>
                                    <f:option value="5"><spring:message code="days.day-5" /></f:option>
                                    <f:option value="6"><spring:message code="days.day-6" /></f:option>
                                </f:select>
                                <br>
                                <small class="text-muted"><spring:message code="complete-registration.body.form.clinic.open_days.help" /></small> <br>
                                <f:errors path="clinicHoursForm" cssClass="text-danger" /> <br>
                                <f:errors path="open_days" cssClass="text-danger" />
                            </fieldset>
                            <table>
                                <tbody id="daysHourList">
                                </tbody>
                            </table>
                            <f:hidden id="OT_0" path="clinicHoursForm.opening_time[0]" />
                            <f:hidden id="OT_1" path="clinicHoursForm.opening_time[1]" />
                            <f:hidden id="OT_2" path="clinicHoursForm.opening_time[2]" />
                            <f:hidden id="OT_3" path="clinicHoursForm.opening_time[3]" />
                            <f:hidden id="OT_4" path="clinicHoursForm.opening_time[4]" />
                            <f:hidden id="OT_5" path="clinicHoursForm.opening_time[5]" />
                            <f:hidden id="OT_6" path="clinicHoursForm.opening_time[6]" />

                            <f:hidden id="CT_0" path="clinicHoursForm.closing_time[0]" />
                            <f:hidden id="CT_1" path="clinicHoursForm.closing_time[1]" />
                            <f:hidden id="CT_2" path="clinicHoursForm.closing_time[2]" />
                            <f:hidden id="CT_3" path="clinicHoursForm.closing_time[3]" />
                            <f:hidden id="CT_4" path="clinicHoursForm.closing_time[4]" />
                            <f:hidden id="CT_5" path="clinicHoursForm.closing_time[5]" />
                            <f:hidden id="CT_6" path="clinicHoursForm.closing_time[6]" />
                        </div>
                        <input type="submit" value="<spring:message code='complete-registration.body.form.submit'/>" name="submit_3" class="row btn btn-lg btn-light  bg-primary btn-block">
                    </f:form>
                </div>
            </div>
        </div>
</div>

<%@ include file="fragments/include-scripts.jsp"%><!-- bootstrap-select JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js" integrity="sha384-SfMwgGnc3UiUUZF50PsPetXLqH2HSl/FmkMW/Ja3N2WaJ/fHLbCHPUsXzzrM6aet" crossorigin="anonymous"></script>
<script>
    const strings = {
        "openTime":"<spring:message code='openDaysPicker.open_time' javaScriptEscape='true' />",
        "closeTime":"<spring:message code='openDaysPicker.close_time' javaScriptEscape='true' />"

    };
</script>
<script src="<c:url value="/resources/js/PlansAddList.js" />"></script>
<script src="<c:url value="/resources/js/OpenDaysPicker.js" />"></script>
<script>
var hash = window.location.hash;
hash && $('ul.nav a[href="' + ${registrationTab} + '"]').tab('show');
$("#clinicForm").submit(beforeSubmit);
</script>

</body>
</html>
