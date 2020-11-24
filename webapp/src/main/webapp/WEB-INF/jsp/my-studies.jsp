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
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <%@ include file="fragments/include-header.jsp"%>

    <link rel="stylesheet" href="<c:url value="/resources/css/mystudies.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">

</head>
<c:url value="?" var="reloadPath"/>
<c:url value="/view-study/" var="studyPath"/>
<body>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<!-- Page Wrapper -->
<div id="wrapper" class="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="orders"/>
    </jsp:include>
    <!-- Content Wrapper -->
<div id="content-wrapper" class="d-flex flex-column main-container">

    <div class="row justify-content-center">
        <div class="col-sm-7">
            <div class="card results-card bg-light anim-content float-right">
                <div class="card-body">
                    <p class="card-title h4"><spring:message code="my-studies.results-card.title"/></p>
                    <hr/>
                    <div id="results" class="list-group" style="overflow-y: scroll; overflow-x: hidden; height: 30rem;">
                        <c:if test="${ordersList.size() eq 0}">
                            <h3 class="text-center py-5 lead"><spring:message code="my-studies.results-card.no-results"/></h3>
                        </c:if>
                        <c:forEach items="${ordersList}" var="order">
                            <a href="${studyPath}${encodedList.get(order.order_id)}" class="list-group-item list-group-item-action">
                                <div class="d-flex w-100 justify-content-between">
                                    <h5 class="mb-1"><spring:message code="my-studies.results-card.order.studytype" arguments="${order.study.name}"/></h5>
                                    <small><spring:message code="my-studies.results-card.order.date" arguments="${order.getLegacyDate()}"/></small>
                                </div>
                                <div class="d-flex w-100 justify-content-between">
                                    <c:choose>
                                        <c:when test="${loggedUser.isMedic() == true && loggedUser.isVerifying() == false}">
                                            <p class="mb-1"><spring:message code="my-studies.results-card.order.clinic" arguments="${order.clinic.name}"/></p>
                                            <small><spring:message code="my-studies.results-card.order.patient" arguments="${order.patient_name}"/></small>
                                        </c:when>
                                        <c:when test="${loggedUser.isClinic() == true && loggedUser.isVerifying() == false}">
                                            <p class="mb-1"><spring:message code="my-studies.results-card.order.patient" arguments="${order.patient_name}"/></p>
                                            <small><spring:message code="my-studies.results-card.order.medic" arguments="${order.medic.name}"/></small>
                                        </c:when>
                                        <c:when test="${loggedUser.isPatient() == true}">
                                            <p class="mb-1"><spring:message code="my-studies.results-card.order.clinic" arguments="${order.clinic.name}"/></p>
                                            <small><spring:message code="my-studies.results-card.order.medic" arguments="${order.medic.name}"/></small>
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
                    <p class="card-title h4"><spring:message code="my-studies.filters-card.title"/></p>
                    <hr/>
                    <c:url var="filter" value="/my-orders"/>
                    <f:form enctype="application/x-www-form-urlencoded" action="${filter}" method="get" modelAttribute="filterForm">
                        <fieldset>
                            <label class="bmd-label-static"><spring:message code="my-studies.filters-card.label.studytype"/></label>
                            <spring:message code="my-studies.filters-card.select.title.studytype" var="selectStudyTitle"/>
                            <spring:message code="my-studies.filters-card.select.option.studytype" var="selectStudyOption"/>
                            <f:select id="studyTypes" cssClass="selectpicker" title="${selectStudyTitle}" data-live-search="true" path="study_id" data-style="text-primary">
                                <f:option value="-1" label="${selectStudyOption}"/>
                                <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                            </f:select>
                        </fieldset>

                        <c:if test="${!(loggedUser.isClinic() eq true && loggedUser.isVerifying() == false)}">
                            <fieldset>
                                <label class="bmd-label-static"><spring:message code="my-studies.filters-card.label.clinic"/></label>
                                <spring:message code="my-studies.filters-card.select.title.clinic" var="selectClinicTitle"/>
                                <spring:message code="my-studies.filters-card.select.option.clinic" var="selectClinicOption"/>
                                <f:select id="medicalClinic" cssClass="selectpicker" title="${selectClinicTitle}" data-live-search="true" path="clinic_id" data-style="text-primary">
                                    <f:option value="-1" label="${selectClinicOption}"/>
                                    <f:options items="${clinicsList}" itemLabel="name" itemValue="user_id"/>
                                </f:select>
                            </fieldset>
                        </c:if>

                        <c:if test="${!(loggedUser.isMedic() eq true && loggedUser.isVerifying() == false)}">
                            <fieldset>
                                <label class="bmd-label-static"><spring:message code="my-studies.filters-card.label.medic"/></label>
                                <spring:message code="my-studies.filters-card.select.title.medic" var="selectMedicTitle"/>
                                <spring:message code="my-studies.filters-card.select.option.medic" var="selectMedicOption"/>
                                <f:select id="medicalFields" cssClass="selectpicker" title="${selectMedicTitle}" data-live-search="true" path="medic_id" data-style="text-primary">
                                    <f:option value="-1" label="${selectMedicOption}"/>
                                    <f:options items="${medicsList}" itemLabel="name" itemValue="user_id"/>
                                </f:select>
                            </fieldset>
                        </c:if>

                        <c:if test="${(loggedUser.isMedic() == true || loggedUser.isClinic() == true ) && loggedUser.isVerifying() == false}">
                            <fieldset>
                                <fieldset class="form-group">
                                    <label for="patientEmail" class="bmd-label-static"><spring:message code="my-studies.filters-card.label.patient-email"/></label>
                                    <f:input type="email" class="form-control" id="patientEmail" path="patient_email"/>
                                    <f:errors path="patient_email"/>
                                </fieldset>
                            </fieldset>
                        </c:if>

                        <fieldset class="form-group mb-3">
                            <label class="bmd-label-static" for="date"><spring:message code="my-studies.filters-card.label.date"/></label>
                            <div class="input-group date">
                                <f:input type="date" class="form-control" id="date" path="date"/>
                                <f:errors path="date"/>
                            </div>
                        </fieldset>
                        <div class="row justify-content-center mt-5">
                            <f:button type="submit" name="submit" formenctype="application/x-www-form-urlencoded" value="search" id="searchButton" class="row btn btn-lg action-btn">
                                <spring:message code="my-studies.filters-card.search"/>
                            </f:button>
                        </div>
                        <div class="row justify-content-center mt-2">
                            <f:button type="submit" name="submit" value="reset" id="resetButton" class="row btn btn-outline-secondary">
                                <spring:message code="my-studies.filters-card.reset"/>
                            </f:button>
                        </div>
                    </f:form>

                </div>
            </div>
        </div>

    </div>
</div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
</body>

</html>