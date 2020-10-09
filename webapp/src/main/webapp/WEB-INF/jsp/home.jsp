<%--
  Created by IntelliJ IDEA.
  User: matia
  Date: 21-Sep-20
  Time: 9:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/home.css"/>">
    <title><spring:message code="appname"/></title>
</head>
<c:url value="/create-order" var="createPath"/>
<c:url value="/view-study/" var="studyPath"/>
<c:url value="/logout" var="logoutPath"/>
<c:url value="/register-as-medic" var="applyMedicPath"/>
<c:url value="/register-as-clinic" var="applyClinicPath"/>
<body>
<div class="main-container">
    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
    <div class="row justify-content-center">

        <div class="col-sm-3">
            <div class="my-4 py-5  float-right">
                <p class="h5 lead anim-content"><spring:message code="home.body.greeting" arguments="${loggedUser.email}"/></p>
            </div>
        </div>
        <div class="col-sm-1">
            <div class="py-2 mx-2" style="margin-top: 2.5em;">
                <c:if test="${loggedUser.isMedic() eq true}">
                    <div class="mt-2">
                        <div>
                            <a href="${createPath}" class="btn create-btn mb-4" role="button"><spring:message code="home.body.button.createOrder"/></a>
                        </div>
                    </div>
                </c:if>
                <c:if test="${loggedUser.isMedic() eq false && loggedUser.isVerifyingMedic() eq false}">
                    <div class="mt-2">
                        <div>
                            <a href="${applyMedicPath}" class="btn create-btn" role="button"><spring:message code="home.body.button.applyMedic"/></a>
                        </div>
                    </div>
                </c:if>
                <c:if test="${loggedUser.isClinic() eq false && loggedUser.isVerifyingClinic() eq false}">
                    <div class="mb-2">
                        <div>
                            <a href="${applyClinicPath}" class="btn create-btn" role="button"><spring:message code="home.body.button.applyClinic"/></a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="mx-5" style="margin-top: 3.5em;">
                    <div class="ml-4">
                        <div>
                            <a href="${logoutPath}" class="btn logout-btn mb-4" role="button"><spring:message code="home.body.button.logout"/></a>
                        </div>
                    </div>
            </div>
        </div>

    </div>

    <c:if test="${loggedUser.isVerifyingMedic() eq true || loggedUser.isVerifyingClinic() eq true}">
        <div class="row justify-content-center mt-0 mb-4 anim-content">
            <div class="alert alert-info pb-3" role="alert">
                <h4 class="alert-heading"><spring:message code="home.body.verifying.title"/></h4>
                <p><spring:message code="home.body.verifying.text1"/>
                    <br/> <spring:message code="home.body.verifying.text2"/></p>
                <hr>
                <p class="mb-0"><spring:message code="home.body.verifying.text3"/></p>
            </div>
        </div>
    </c:if>


    <c:choose>
        <c:when test="${has_studies != true}">
            <div class="align-items-end result-not">
                <h1 class="text-center mt-5 py-5 anim-content"><spring:message code="home.body.noStudies"/></h1>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row mx-2">
                <div class="col">
                    <div class="card bg-light anim-content">
                        <div class="card-body">
                            <p class="card-title h4"><spring:message code="home.body.card.studies.title.patient"/> </p>
                            <div class="list-group">
                                <c:if test="${patient_studies.size() eq 0}">
                                    <h3 class="text-center py-5 lead"><spring:message code="home.body.card.studies.noStudies"/> </h3>
                                </c:if>
                                <c:forEach items="${patient_studies}" var="pat_study">
                                    <a href="${studyPath}${patient_encodeds.get(pat_study.order_id)}" class="list-group-item list-group-item-action">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h5 class="mb-1"><spring:message code="home.body.card.studies.studyType" arguments="${pat_study.study.name}" /></h5>
                                            <small><spring:message code="home.body.card.studies.date" arguments="${pat_study.date}" /></small>
                                        </div>
                                        <div class="d-flex w-100 justify-content-between">
                                            <p class="mb-1"><spring:message code="home.body.card.studies.clinic" arguments="${pat_study.clinic.name}" /></p>
                                            <small><spring:message code="home.body.card.studies.medic" arguments="${pat_study.medic.name}" /></small>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${loggedUser.isMedic() eq true && loggedUser.isVerifyingMedic() eq false}">
                    <div class="col">
                        <div class="card bg-light anim-content">
                            <div class="card-body">
                                <p class="card-title h4"><spring:message code="home.body.card.studies.title.medic"/></p>
                                <div class="list-group">
                                    <c:if test="${medic_studies.size() eq 0}">
                                        <h3 class="text-center py-5 lead"><spring:message code="home.body.noStudies"/> </h3>
                                    </c:if>
                                    <c:forEach items="${medic_studies}" var="med_study">
                                        <a href="${studyPath}${medic_encodeds.get(med_study.order_id)}" class="list-group-item list-group-item-action">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h5 class="mb-1"><spring:message code="home.body.card.studies.studyType" arguments="${med_study.study.name}"/></h5>
                                                <small><spring:message code="home.body.card.studies.date" arguments="${med_study.date}"/></small>
                                            </div>
                                            <div class="d-flex w-100 justify-content-between">
                                                <p class="mb-1"><spring:message code="home.body.card.studies.clinic" arguments="${med_study.clinic.name}"/></p>
                                                <small><spring:message code="home.body.card.studies.patient" arguments="${med_study.patient_name}"/></small>
                                            </div>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${loggedUser.isClinic() eq true && loggedUser.isVerifyingClinic() eq false}">
                    <div class="col">
                        <div class="card bg-light anim-content">
                            <div class="card-body">
                                <p class="card-title h4"><spring:message code="home.body.card.studies.title.clinic"/></p>
                                <div class="list-group">
                                    <c:if test="${clinic_studies.size() eq 0}">
                                        <h3 class="text-center py-5 lead"><spring:message code="home.body.noStudies"/></h3>
                                    </c:if>
                                    <c:forEach items="${clinic_studies}" var="cli_study">
                                        <a href="${studyPath}${clinic_encodeds.get(cli_study.order_id)}" class="list-group-item list-group-item-action">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h5 class="mb-1"><spring:message code="home.body.card.studies.studyType" arguments="${cli_study.study.name}"/></h5>
                                                <small><spring:message code="home.body.card.studies.date" arguments="${cli_study.date}"/></small>
                                            </div>
                                            <div class="d-flex w-100 justify-content-between">
                                                <p class="mb-1"><spring:message code="home.body.card.studies.clinic" arguments="${cli_study.clinic.name}"/></p>
                                                <small><spring:message code="home.body.card.studies.patient" arguments="${cli_study.patient_name}"/></small>
                                            </div>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>


</div>

</body>
</html>