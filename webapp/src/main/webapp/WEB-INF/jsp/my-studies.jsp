<%--
  Created by IntelliJ IDEA.
  User: matia
  Date: 05-Oct-20
  Time: 12:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="fragments/include-header.jsp"%>

    <!-- bootstrap-select CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">

    <link rel="stylesheet" href="<c:url value="/resources/css/registration.css"/>">

    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/mystudies.css"/>">
    <title>MedTransfer</title>
</head>
<c:url value="?" var="reloadPath"/>
<c:url value="/view-study/" var="studyPath"/>
<body>
<div class="main-container">
    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
    <div class="row justify-content-center">
        <div class="col-sm-7">
            <div class="card results-card bg-light anim-content float-right">
                <div class="card-body">
                    <p class="card-title h4">Results</p>
                    <hr/>
                    <div id="results" class="list-group" style="overflow-y: scroll; overflow-x: hidden; height: 90%;">
                        <c:if test="${ordersList.size() eq 0}">
                            <h3 class="text-center py-5 lead">It seems there are no studies matching with the filter criteria.</h3>
                        </c:if>
                        <c:forEach items="${ordersList}" var="order">
                            <a href="${studyPath}${encodedList.get(order.order_id)}" class="list-group-item list-group-item-action">
                                <div class="d-flex w-100 justify-content-between">
                                    <h5 class="mb-1">Study type: <c:out value="${order.study.name}" /></h5>
                                    <small>Date: <c:out value="${order.date}" /></small>
                                </div>
                                <div class="d-flex w-100 justify-content-between">
                                    <c:choose>
                                        <c:when test="${loggedUser.isMedic() == true && loggedUser.isVerifyingMedic() == false}">
                                            <p class="mb-1">Clinic: <c:out value="${order.clinic.name}"/></p>
                                            <small>Patient: <c:out value="${order.patient_name}" /></small>
                                        </c:when>
                                        <c:when test="${loggedUser.isClinic() == true && loggedUser.isVerifyingClinic() == false}">
                                            <p class="mb-1">Patient: <c:out value="${order.patient_name}" /></p>
                                            <small>Medic: <c:out value="${order.medic.name}" /></small>
                                        </c:when>
                                        <c:when test="${loggedUser.isPatient() == true}">
                                            <p class="mb-1">Clinic: <c:out value="${order.clinic.name}"/></p>
                                            <small>Medic: <c:out value="${order.medic.name}" /></small>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-5">
            <div class="card filters-card bg-light float-left">
                <div class="card-body">
                    <p class="card-title h4">Filters</p>
                    <hr/>
                    <c:url var="filter" value="/filter-search"/>
                    <f:form action="${filter}" method="get" modelAttribute="filterForm">
                        <fieldset>
                            <label class="bmd-label-static">Study Type</label>
                            <f:select id="studyTypes" cssClass="selectpicker" title="Choose a study type" data-live-search="true" path="study_id" data-style="btn-custom">
                                <f:option value="-1" label="Any medical study"/>
                                <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                            </f:select>
                        </fieldset>
                        <fieldset>
                            <label class="bmd-label-static">Clinic</label>
                            <f:select id="medicalClinic" cssClass="selectpicker" title="Choose a clinic" data-live-search="true" path="clinic_id" data-style="btn-custom">
                                <f:option value="-1" label="Any medical clinic"/>
                                <f:options items="${clinicsList}" itemLabel="name" itemValue="user_id"/>
                            </f:select>
                        </fieldset>
                        <fieldset>
                            <label class="bmd-label-static">Medic</label>
                            <f:select id="medicalFields" cssClass="selectpicker" title="Choose a medic" data-live-search="true" path="medic_id" data-style="btn-custom">
                                <f:option value="-1" label="Any medic"/>
                                <f:options items="${medicsList}" itemLabel="name" itemValue="user_id"/>
                            </f:select>
                        </fieldset>
                        <c:if test="${(loggedUser.isMedic() == true && loggedUser.isVerifyingMedic() == false )||(loggedUser.isClinic() == true && loggedUser.isVerifyingClinic() == false)}">
                            <fieldset>
                                <fieldset class="form-group">
                                    <label for="patientEmail" class="bmd-label-floating">Patient's Email Address</label>
                                    <f:input type="email" class="form-control" id="patientEmail" path="patient_email"/>
                                    <f:errors path="patient_email"/>
                                </fieldset>
                            </fieldset>
                        </c:if>

                        <fieldset class="form-group">
                            <label class="bmd-label-static" for="date">Date</label>
                            <div class="input-group date">
                                <f:input type="date" class="form-control" id="date" path="date"/>
                                <f:errors path="date"/>
                            </div>
                        </fieldset>
                        <div class="row justify-content-center">
                            <input type="submit" id="search" class="row btn btn-lg action-btn" value="Search">
                        </div>
                    </f:form>
                    <div class="row justify-content-center">
                        <a href="${reloadPath}" type="reset" role="button" class="row btn btn-outline-secondary">Reset filters</a>
                    </div>
                </div>
            </div>
        </div>


    </div>
</div>
<%@ include file="fragments/include-scripts.jsp"%>
<!-- bootstrap-select JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js" integrity="sha384-SfMwgGnc3UiUUZF50PsPetXLqH2HSl/FmkMW/Ja3N2WaJ/fHLbCHPUsXzzrM6aet" crossorigin="anonymous"></script>

</body>

</html>