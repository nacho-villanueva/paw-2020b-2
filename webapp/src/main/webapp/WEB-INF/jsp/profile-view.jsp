<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <!-- bootstrap-select CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="<c:url value="/resources/css/profile.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">

</head>
<body>
<c:url value="/api/image" var="imageAssets"/>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<div id="wrapper" class="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="profile"/>
    </jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">

        <div class="main-container">
            <div class="row justify-content-center" style="padding-bottom: 3rem !important;margin-bottom: 3rem !important;">
                <div class="card" style="width: 40em; margin-top: 2em;">
                    <div class="card-body">
                        <div class="row">
                            <p class="card-title h4 mx-auto mt-3"><spring:message code="profile-view.body.title"/> </p>
                        </div>
                        <hr class="divider my-4">

                        <ul class="nav nav-pills nav-justified" id="myTab" role="tablist">
                            <li class="nav-item">
                                <a class=" nav-link active"
                                   id="user-tab" data-toggle="tab" href="<c:url value="#user"/>" role="tab"
                                   aria-controls="user" aria-selected="true"><spring:message code="profile-view.body.pill.user"/> </a>
                            </li>

                            <c:choose>
                                <c:when test="${not empty patient}">
                                    <li class="nav-item">
                                        <a class="nav-link"
                                           id="patient-tab" data-toggle="tab" href="<c:url value="#patient"/>"
                                           role="tab" aria-controls="patient" aria-selected="false"><spring:message code="profile-view.body.pill.patient"/> </a>
                                    </li>
                                </c:when>

                                <c:when test="${not empty medic}">
                                    <li class="nav-item">
                                        <a class="nav-link"
                                           id="medic-tab" data-toggle="tab" href="<c:url value="#medic"/>"
                                           role="tab" aria-controls="medic" aria-selected="false"><spring:message code="profile-view.body.pill.medic"/> </a>
                                    </li>
                                </c:when>

                                <c:when test="${not empty clinic}">
                                    <li class="nav-item">
                                        <a class="nav-link"
                                           id="clinic-tab" data-toggle="tab" href="<c:url value="#clinic"/>"
                                           role="tab" aria-controls="clinic" aria-selected="false"><spring:message code="profile-view.body.pill.clinic"/> </a>
                                    </li>
                                </c:when>

                                <c:otherwise/>

                            </c:choose>
                        </ul>

                        <hr class="divider my-4">

                        <div class="tab-content">
                            <div id="user" class="tab-pane fade in show active">

                                <table class="table table-borderless table-responsive">
                                    <tbody>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.user.email.label"/></td>
                                            <td class="output"><c:out value="${loggedUser.email}"/></td>
                                            <td>
                                                <a class="btn btn-outline-secondary btn-sm" href="<c:url value="/profile/edit/user/email"/>" title="<spring:message code="profile-view.body.button.edit.user.email"/>"> <i class="fas fa-fw fa-edit"></i> </a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.user.password.label"/></td>
                                            <td class="output"><spring:message code="profile-view.body.tab.user.password.data"/></td>
                                            <td><a class="btn btn-outline-secondary btn-sm" href="<c:url value="/profile/edit/user/pass"/>" title="<spring:message code="profile-view.body.button.edit.user.password"/>"> <i class="fas fa-fw fa-edit"></i> </a></td>
                                        </tr>

                                    </tbody>
                                </table>
                            </div>

                            <c:if test="${not empty patient}">
                                <div id="patient" class="tab-pane fade in show">

                                    <table class="table table-borderless table-responsive">
                                        <tbody>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.patient.name.label"/></td>
                                            <td class="output"><c:out value="${patient.name}"/></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.patient.medicPlan.label"/></td>
                                            <td class="output"><c:out value="${patient.medicPlan}"/></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.patient.medicPlanNumber.label"/></td>
                                            <td class="output"><c:out value="${patient.medicPlanNumber}"/></td>
                                        </tr>

                                        </tbody>
                                    </table>

                                    <hr class="divider my-4">

                                    <div class="row justify-content-center">
                                        <a class="btn btn-lg action-btn" href="<c:url value="/profile/edit/patient"/>"><spring:message code="profile-view.body.button.edit.patient"/> </a>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${not empty medic}">
                                <div id="medic" class="tab-pane fade in">

                                    <table class="table table-borderless table-responsive">
                                        <tbody>

                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.medic.name.label"/></td>
                                            <td class="output"><c:out value="${medic.name}"/></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.medic.telephone.label"/></td>
                                            <td class="output"><c:out value="${medic.telephone}"/></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.medic.identification.label"/></td>
                                            <td class="output">
                                                <img src="${imageAssets}/medic/${loggedUser.id}?attr=identification" class="align-self-end ml-3"
                                                     alt="<spring:message code="profile-view.body.tab.medic.identification.altText"/>" style="width: 10rem;"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.medic.licenceNumber.label"/></td>
                                            <td class="output"><c:out value="${medic.licenceNumber}"/></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.medic.medical_fields.label"/></td>
                                            <td class="output"><c:forEach items="${medic.medical_fields}" var="medicalField"><p><c:out value="${medicalField.name}"/></p></c:forEach></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.medic.verified.label"/></td>
                                            <td class="output"><i class="fas <c:choose><c:when test="${medic.verified}">verified fa-check</c:when><c:otherwise>nverified fa-times</c:otherwise></c:choose> fa-lg"></i></td>
                                        </tr>

                                        </tbody>
                                    </table>

                                    <div class="row justify-content-center">
                                        <a class="btn btn-lg action-btn" href="<c:url value="/profile/edit/medic"/>"><spring:message code="profile-view.body.button.edit.medic"/> </a>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${not empty clinic}">
                                <div id="clinic" class="tab-pane fade in show">

                                    <table class="table table-borderless table-responsive">
                                        <tbody>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.clinic.name.label"/></td>
                                            <td class="output"><c:out value="${clinic.name}"/></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.clinic.telephone.label"/></td>
                                            <td class="output"><c:out value="${clinic.telephone}"/></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.clinic.open_hours.label" /></td>
                                            <td><c:forEach items="${openDayHour}" var="h">
                                                <spring:message code="days.day-${h.key}" var="day"/>
                                                <p><spring:message code="profile-view.body.tab.clinic.open_hours.format" arguments="${day},${h.value[0]},${h.value[1]}"/> </p>
                                            </c:forEach></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.clinic.acceptedPlans.label" /> </td>
                                            <td class="output"><c:forEach items="${clinic.acceptedPlans}" var="plan"><span class="badge-sm badge-pill badge-secondary mr-1 d-inline-block"><c:out value="${plan}" /></span></c:forEach></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.clinic.medical_studies.label"/></td>
                                            <td class="output"><c:forEach items="${clinic.medical_studies}" var="medicalStudy"><p><c:out value="${medicalStudy.name}"/></p></c:forEach></td>
                                        </tr>
                                        <tr>
                                            <td><spring:message code="profile-view.body.tab.clinic.verified.label"/></td>
                                            <td class="output"><i class="fas <c:choose><c:when test="${clinic.verified}">verified fa-check</c:when><c:otherwise>nverified fa-times</c:otherwise></c:choose> fa-lg"></i></td>
                                        </tr>


                                        </tbody>
                                    </table>

                                    <hr class="divider my-4">

                                    <div class="row justify-content-center">
                                        <a class="btn btn-lg action-btn" href="<c:url value="/profile/edit/clinic"/>"><spring:message code="profile-view.body.button.edit.clinic"/> </a>
                                    </div>
                                </div>
                            </c:if>

                        </div>

                        <div class="row justify-content-center">
                            <c:if test="${not empty editSuccess}"><div class="alert alert-success" role="alert"><spring:message code="profile-view.body.alert-success.message" arguments="${editSuccess}"/></div></c:if>
                        </div>
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
