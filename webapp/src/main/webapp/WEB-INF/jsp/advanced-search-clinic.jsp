<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%@ include file="fragments/include-header.jsp"%>

    <link rel="stylesheet" href="<c:url value="/resources/css/advanced-search-clinic.css"/>">
    <c:choose>
        <c:when test="${empty notLogged}">
            <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">
        </c:when>
        <c:otherwise>
            <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
        </c:otherwise>
    </c:choose>
    <link rel="stylesheet" href="<c:url value="/resources/css/callout.css"/>">
</head>
<body>
<c:set value="${empty daysOfWeek?7:daysOfWeek}" var="daysOfWeek"/>


    <c:choose>
        <c:when test="${empty notLogged}">
            <c:choose>
                <c:when test="${not empty orderForm}">
                    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
                    <div id="wrapper" class="wrapper">
                        <jsp:include page="fragments/sidebar-fragment.jsp" >
                            <jsp:param name="current" value="orders"/>
                        </jsp:include>
                </c:when>
                <c:otherwise>
                    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
                    <div id="wrapper" class="wrapper">
                        <jsp:include page="fragments/sidebar-fragment.jsp" >
                            <jsp:param name="current" value="search"/>
                        </jsp:include>
                </c:otherwise>
            </c:choose>
            <div id="content-wrapper" class="d-flex flex-column main-container">
        </c:when>
        <c:otherwise>
            <div id="wrapper" class="wrapper">
            <div id="content-wrapper" class="d-flex flex-column main-container">
                <%@include file="fragments/navbar-fragment.jsp"%>
        </c:otherwise>
    </c:choose>
    <!-- Content Wrapper -->

        <div class="row justify-content-center">
            <div class="col-sm-5">
                <div class="card filters-card bg-light anim-content float-right">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty orderForm}"><c:url var="formSubmitUrl" value="/create-order"/></c:when>
                            <c:otherwise><c:url var="formSubmitUrl" value="/advanced-search/clinic"/></c:otherwise>
                        </c:choose>
                        <f:form enctype="application/x-www-form-urlencoded" action="${formSubmitUrl}" method="get" modelAttribute="advancedSearchClinicForm">
                            <p class="card-title h4">
                                <c:choose>
                                    <c:when test="${not empty orderForm}">
                                        <spring:message code="create-order.body.form.title2"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="advanced-search-clinic.body.title"/>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <hr/>

                            <c:if test="${not empty orderForm}">
                                <f:input path="medicId" type="hidden" id="medicId"/>
                                <f:input path="studyId" type="hidden" id="studyId"/>
                                <f:input path="description" type="hidden" id="description"/>
                                <f:input path="patientInfo.insurancePlan" type="hidden" id="patientInfo.insurancePlan"/>
                                <f:input path="patientInfo.insuranceNumber" type="hidden" id="patientInfo.insuranceNumber"/>
                                <f:input path="patientInfo.email" type="hidden" id="patientEmail"/>
                                <f:input path="patientInfo.name" type="hidden" id="patientInfo.name"/>
                            </c:if>
                            <c:if test="${not empty orderForm}">
                                <div class="row justify-content-center">
                                    <a class="btn btn-outline" onclick="clickOnShowOrderButton(this);return false;" data-toggle="collapse" href="#collapseInfo" role="button" aria-expanded="true" aria-controls="collapseInfo">
                                        <spring:message code="advanced-search-clinic.form.order-info.toggle-label"/>
                                    </a>

                                    <div class="collapse show" id="collapseInfo">
                                        <div class="bs-callout bs-callout-med">
                                            <div class="row justify-content-start">
                                                <div class="col type"><p class="type-title"><spring:message code="advanced-search-clinic.form.order-info.patient-name"/></p><c:out value="${orderForm.patientInfo.name}"/></div>
                                                <div class="col type"><p class="type-title"><spring:message code="advanced-search-clinic.form.order-info.study-type"/></p><c:out value="${studyName}"/></div>
                                                <div class="w-100"></div>
                                                <div class="col type"><p class="type-title"><spring:message code="advanced-search-clinic.form.order-info.patient-insurance-plan"/></p> <c:out value="${orderForm.patientInfo.insurancePlan}"/></div>
                                                <div class="col type"><p class="type-title"><spring:message code="advanced-search-clinic.form.order-info.patient-insurance-number"/></p> <c:out value="${orderForm.patientInfo.insuranceNumber}"/></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <fieldset class="form-group">
                                <label class="bmd-label-floating" for="clinicName"><spring:message code="advanced-search-clinic.form.clinicName.label"/></label>
                                <div class="input-group">
                                    <f:input type="text" class="form-control" id="clinicName" path="clinicName"/>
                                    <f:errors path="clinicName"/>
                                </div>
                            </fieldset>

                            <fieldset class="form-group">
                                <label class="bmd-label-floating" for="medicalPlan"><spring:message code="advanced-search-clinic.form.medicalPlan.label"/></label>
                                <div class="input-group">
                                    <c:choose>
                                        <c:when test="${not empty orderForm}">
                                            <f:input type="text" readonly="true" class="form-control" id="medicalPlan" path="medicalPlan"/>
                                        </c:when>
                                        <c:otherwise>
                                            <f:input type="text" class="form-control" id="medicalPlan" path="medicalPlan"/>
                                            <f:errors path="medicalPlan"/>
                                        </c:otherwise>
                                    </c:choose>


                                </div>
                            </fieldset>

                            <c:choose>
                                <c:when test="${not empty orderForm}">
                                    <f:input path="medicalStudy" type="hidden" id="medicalStudy"/>
                                </c:when>
                                <c:otherwise>
                                    <fieldset class="form-group">
                                        <label class="bmd-label-floating" for="medicalStudy"><spring:message code="advanced-search-clinic.form.medicalStudy.label"/></label>
                                        <div class="input-group">
                                            <f:input type="text" class="form-control" id="medicalStudy" path="medicalStudy"/>
                                            <f:errors path="medicalStudy"/>
                                        </div>
                                    </fieldset>
                                </c:otherwise>
                            </c:choose>

                            <fieldset>
                                <spring:message code="advanced-search-clinic.form.fromTime.placeholder" var="placeholderOT"/>
                                <spring:message code="advanced-search-clinic.form.toTime.placeholder" var="placeholderCT"/>

                                <a class="btn btn-outline" data-toggle="collapse" href="#collapseHours" role="button" aria-expanded="false" aria-controls="collapseHours">
                                    <spring:message code="advanced-search-clinic.form.availableHours.label"/>
                                </a>


                                <div class="collapse" id="collapseHours">
                                    <table class="table table-scrollable">
                                        <thead>
                                        <tr>
                                            <th></th>
                                            <th><spring:message code="advanced-search-clinic.form.available.label"/></th>
                                            <th><spring:message code="advanced-search-clinic.form.fromTime.label"/></th>
                                            <th><spring:message code="advanced-search-clinic.form.toTime.label"/></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var = "day" begin = "0" end = "${daysOfWeek - 1}">
                                                <tr>
                                                    <th><spring:message code="days.day-${day}"/> </th>
                                                    <th><f:checkbox id="day" path="isAvailable[${day}]"/></th>
                                                    <th>
                                                        <fieldset>
                                                            <f:input id="day-${day}-OT" type="text" class="form-control time-input" placeholder="${placeholderOT}" path="availableTime.openingTime[${day}]" maxlength="5" cssClass="form-control time-input"/>
                                                            <f:errors path="availableTime.openingTime[${day}]" cssClass="text-danger" element="small" />
                                                        </fieldset>
                                                    </th>
                                                    <th>
                                                        <fieldset>
                                                            <f:input id="day-${day}-CT" type="text" class="form-control time-input" placeholder="${placeholderCT}" path="availableTime.closingTime[${day}]" maxlength="5" cssClass="form-control time-input"/>
                                                            <f:errors path="availableTime.closingTime[${day}]" cssClass="text-danger" element="small" />
                                                        </fieldset>
                                                    </th>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </fieldset>

                            <div class="row justify-content-center">
                                <f:button type="submit" name="submit" formenctype="application/x-www-form-urlencoded" value="search" id="searchButton" class="btn action-btn mt-4 mb-2">
                                    <spring:message code="advanced-search-clinic.form.button.search"/>
                                </f:button>
                            </div>
                            <div class="row justify-content-center">
                                <f:button type="submit" name="submit" value="reset" id="resetButton" class="row btn btn-outline-secondary">
                                    <spring:message code="advanced-search-clinic.form.button.resetFilters"/>
                                </f:button>
                            </div>
                            <c:if test="${errorAlert}"><div class="alert alert-danger" role="alert"><spring:message code="advanced-search-clinic.form.errorAlert"/></div></c:if>

                        </f:form>
                    </div>
                </div>
            </div>
            <div class="col-sm-7">
                <div class="card results-card bg-light float-left">
                    <div class="card-body">
                        <p class="card-title h4"><spring:message code="advanced-search-clinic.results.title"/></p>
                        <hr/>
                        <div class="d-flex flex-row">
                            <div id="results" class="list-group result-section" style="overflow-y: scroll; overflow-x: hidden; height: 90%;">
                                <c:if test="${clinicsList.size() eq 0}">
                                    <h3 class="text-center py-5 lead"><spring:message code="advanced-search.clinic.results.noResults"/></h3>
                                </c:if>
                                <ul class="nav flex-column" id="myTab" role="tablist">
                                    <c:forEach items="${clinicsList}" var="clinic">
                                        <li class="nav-item">
                                            <a id="${clinic.userId}" onclick="selectClinic(this);return false;" class="list-group-item list-group-item-action" data-toggle="tab" href="#clinic_${clinic.userId}" role="tab" aria-controls="clinic_${clinic.userId}" aria-selected="false">
                                                <div class="justify-content-between">
                                                    <h5 class="mb-1"><c:out value="${clinic.name}"/></h5>
                                                </div>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                            <div id="data" class="data-section">

                                <h5 class="text-muted"><spring:message code="create-order.body.form.clinic.title"/> </h5>
                                <div class=""></div>

                                <div id="noClinic" class="no-clinic">
                                    <h4><spring:message code="advanced-search.clinic.selectedClinic.noClinic"/> </h4>
                                </div>

                                <div id="clinicSelected"class="tab-content">
                                    <c:forEach items="${clinicsList}" var="clinic">
                                        <div id="clinic_${clinic.userId}" class="tab-pane fade tab-result">
                                            <h3><c:out value="${clinic.name}"/></h3>
                                            <table class="table table-borderless">
                                                <tbody>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.user.email.label"/></td>
                                                    <td class="output"><c:out value="${clinic.email}"/></td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.clinic.telephone.label"/></td>
                                                    <td class="output"><c:out value="${clinic.telephone}"/></td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.clinic.openHours.label" /></td>
                                                    <td>
                                                        <c:forEach var="day" begin="0" end="${daysOfWeek - 1}">
                                                            <c:if test="${clinic.hours.days[day]}">
                                                                <spring:message code="days.day-${day}" var="dayName"/>
                                                                <p><spring:message code="profile-view.body.tab.clinic.openHours.format" arguments="${dayName},${clinic.hours.openHours[day]},${clinic.hours.closeHours[day]}"/> </p>
                                                            </c:if>

                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.clinic.acceptedPlans.label" /> </td>
                                                    <td class="output"><c:forEach items="${clinic.acceptedPlans}" var="plan"><span class="badge-sm badge-pill badge-secondary mr-1 d-inline-block"><c:out value="${plan}" /></span></c:forEach></td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.clinic.medicalStudies.label"/></td>
                                                    <td class="output"><c:forEach items="${clinic.medicalStudies}" var="medicalStudy"><p><c:out value="${medicalStudy.name}"/></p></c:forEach></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:forEach>
                                </div>


                                <c:if test="${not empty orderForm}">
                                    <f:form action="${formSubmitUrl}" method="post" modelAttribute="orderForm">

                                        <f:input path="medicId" type="hidden" id="medicId"/>
                                        <f:input path="clinicId" type="hidden" id="clinicId"/>
                                        <f:input path="studyId" type="hidden" id="studyId"/>
                                        <f:input path="description" type="hidden" id="description"/>
                                        <f:input path="patientInfo.insurancePlan" type="hidden" id="patientInfo.insurancePlan"/>
                                        <f:input path="patientInfo.insuranceNumber" type="hidden" id="patientInfo.insuranceNumber"/>
                                        <f:input path="patientInfo.email" type="hidden" id="patientEmail"/>
                                        <f:input path="patientInfo.name" type="hidden" id="patientInfo.name"/>

                                        <c:if test="${clinicUnselected}"><div class="alert alert-danger" role="alert"><f:errors path="clinicId"/> </div></c:if>
                                        <div class="row justify-content-center">

                                            <f:button class="row btn btn-lg action-btn" type="submit" name="submit" disabled="true" id="createOrder" value="create-order"><spring:message code="create-order.body.form.button.submit"/></f:button>
                                        </div>
                                    </f:form>
                                    <f:form action="${formSubmitUrl}" method="post" modelAttribute="orderWithoutClinicForm">

                                        <f:input path="medicId" type="hidden" id="medicId"/>
                                        <f:input path="studyId" type="hidden" id="studyId"/>
                                        <f:input path="description" type="hidden" id="description"/>
                                        <f:input path="patientInfo.insurancePlan" type="hidden" id="patientInfo.insurancePlan"/>
                                        <f:input path="patientInfo.insuranceNumber" type="hidden" id="patientInfo.insuranceNumber"/>
                                        <f:input path="patientInfo.email" type="hidden" id="patientEmail"/>
                                        <f:input path="patientInfo.name" type="hidden" id="patientInfo.name"/>
                                        <f:input path="patientInfo.existingPatient" type="hidden" id="patientInfo.existingPatient"/>

                                        <div class="row justify-content-center">
                                            <f:button type="submit" name="submit" formmethod="post" value="back" id="backButton" class="row btn btn-outline-secondary">
                                                <spring:message code="create-order.body.form.button.back"/>

                                            </f:button>
                                    </f:form>
                                </c:if>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
<script type="text/javascript">
    function selectClinic(listItem) {

        document.getElementById("noClinic").style.display = "none";

        let clinic = document.getElementById("clinicId");
        if(clinic !== null){
            clinic.setAttribute("value",listItem.id);

            document.getElementById("createOrder").disabled=false;
        }

    }
</script>
</body>
</html>
