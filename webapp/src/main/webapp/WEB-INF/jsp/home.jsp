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
    <title>MedTransfer</title>
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
                <p class="h5 lead anim-content">Welcome back, </p><p class="h4 anim-content"><c:out value="${loggedUser.email}" /></p>
            </div>
        </div>
        <div class="col-sm-1">
            <div class="py-2 mx-2" style="margin-top: 2.5em;">
                <c:if test="${loggedUser.isMedic() eq true}">
                    <div class="mt-2">
                        <div>
                            <a href="${createPath}" class="btn create-btn mb-4" role="button">Create a new order</a>
                        </div>
                    </div>
                </c:if>
                <c:if test="${loggedUser.isMedic() eq false && loggedUser.isVerifyingMedic() eq false}">
                    <div class="mt-2">
                        <div>
                            <a href="${applyMedicPath}" class="btn create-btn" role="button">Apply as a Medic</a>
                        </div>
                    </div>
                </c:if>
                <c:if test="${loggedUser.isClinic() eq false && loggedUser.isVerifyingClinic() eq false}">
                    <div class="mb-2">
                        <div>
                            <a href="${applyClinicPath}" class="btn create-btn" role="button">Apply as a Clinic</a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="mx-5" style="margin-top: 3.5em;">
                    <div class="ml-4">
                        <div>
                            <a href="${logoutPath}" class="btn logout-btn mb-4" role="button">Logout</a>
                        </div>
                    </div>
            </div>
        </div>

    </div>

    <c:if test="${loggedUser.isVerifyingMedic() eq true || loggedUser.isVerifyingClinic() eq true}">
        <div class="row justify-content-center mt-0 mb-4 anim-content">
            <div class="alert alert-info pb-3" role="alert">
                <h4 class="alert-heading">Verification in progress</h4>
                <p>We have received your application and our administrators will begin the
                verification process. <br/> We'll send you an alert to your email the process ends.</p>
                <hr>
                <p class="mb-0">You may continue to use your account as usual.</p>
            </div>
        </div>
    </c:if>


    <c:choose>
        <c:when test="${has_studies != true}">
            <div class="align-items-end result-not">
                <h1 class="text-center mt-5 py-5 anim-content">It seems there are no studies linked to your account right now.</h1>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row mx-2">
                <div class="col">
                    <div class="card bg-light anim-content">
                        <div class="card-body">
                            <p class="card-title h4">Your medical studies</p>
                            <div class="list-group">
                                <c:if test="${patient_studies.size() eq 0}">
                                    <h3 class="text-center py-5 lead">It seems there are no studies linked to you right now.</h3>
                                </c:if>
                                <c:forEach items="${patient_studies}" var="pat_study">
                                    <a href="${studyPath}${patient_encodeds.get(pat_study.order_id)}" class="list-group-item list-group-item-action">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h5 class="mb-1">Study type: <c:out value="${pat_study.study.name}" /></h5>
                                            <small>Date: <c:out value="${pat_study.date}" /></small>
                                        </div>
                                        <div class="d-flex w-100 justify-content-between">
                                            <p class="mb-1">Clinic: <c:out value="${pat_study.clinic.name}" /></p>
                                            <small>Medic: <c:out value="${pat_study.medic.name}" /></small>
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
                                <p class="card-title h4">Patient's medical studies</p>
                                <div class="list-group">
                                    <c:if test="${medic_studies.size() eq 0}">
                                        <h3 class="text-center py-5 lead">It seems there are no studies linked to you right now.</h3>
                                    </c:if>
                                    <c:forEach items="${medic_studies}" var="med_study">
                                        <a href="${studyPath}${medic_encodeds.get(med_study.order_id)}" class="list-group-item list-group-item-action">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h5 class="mb-1">Study type: <c:out value="${med_study.study.name}"/></h5>
                                                <small>Date: <c:out value="${med_study.date}"/></small>
                                            </div>
                                            <div class="d-flex w-100 justify-content-between">
                                                <p class="mb-1">Clinic: <c:out value="${med_study.clinic.name}"/></p>
                                                <small>Patient: <c:out value="${med_study.patient_name}"/></small>
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
                                <p class="card-title h4">Patient's appointments</p>
                                <div class="list-group">
                                    <c:if test="${clinic_studies.size() eq 0}">
                                        <h3 class="text-center py-5 lead">It seems there are no studies linked to you right now.</h3>
                                    </c:if>
                                    <c:forEach items="${clinic_studies}" var="cli_study">
                                        <a href="${studyPath}${clinic_encodeds.get(cli_study.order_id)}" class="list-group-item list-group-item-action">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h5 class="mb-1">Study type: <c:out value="${cli_study.study.name}"/></h5>
                                                <small>Date: ${cli_study.date}</small>
                                            </div>
                                            <div class="d-flex w-100 justify-content-between">
                                                <p class="mb-1">Clinic: <c:out value="${cli_study.clinic.name}"/></p>
                                                <small>Patient: <c:out value="${cli_study.patient_name}"/></small>
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