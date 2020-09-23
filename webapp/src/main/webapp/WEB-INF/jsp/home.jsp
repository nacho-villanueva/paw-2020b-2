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
    <%@include file="fragments/navbar-fragment.jsp"%>
    <div class="row justify-content-center">

        <div class="col-sm-3">
            <div class="my-4 py-5  float-right">
                <p class="h5 lead anim-content">Welcome back, </p><p class="h4 anim-content">${loggedUser.email}</p>
            </div>
        </div>
        <div class="col-sm-1">
            <div class="py-2 mx-2" style="margin-top: 2.5em;">
                <c:if test="${loggedUser.role eq 2 || loggedUser.role eq 4}">
                    <div class=" mt-2 mb-3 pb-4">
                        <div>
                            <a href="${createPath}" class="btn create-btn mb-4" role="button">Create a new order</a>
                        </div>
                    </div>
                </c:if>
                <c:if test="${loggedUser.role eq 1}">
                    <div class="mt-2">
                        <div>
                            <a href="${applyMedicPath}" class="btn create-btn" role="button">Apply as a Medic</a>
                        </div>
                    </div>
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


    <c:choose>
        <c:when test="${has_studies != true}">
            <div class="align-items-end result-not">
                <h1 class="text-center mt-5 py-5 anim-content">It seems there are no studies linked to your account right now.</h1>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row mx-2">
                    <c:if test="${loggedUser.role ge 1}">
                        <div class="col">
                            <div class="card bg-light anim-content">
                                <div class="card-body">
                                    <p class="card-title h4">As a patient, here are your medical studies</p>
                                    <div class="list-group">
                                        <c:forEach items="${patient_studies}" var="pat_study">
                                            <a href="${studyPath}${patient_encodeds.get(pat_study.order_id)}" class="list-group-item list-group-item-action">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1">Study type: ${pat_study.study.name}</h5>
                                                    <small>Date: ${pat_study.date}</small>
                                                </div>
                                                <div class="d-flex w-100 justify-content-between">
                                                    <p class="mb-1">Clinic: ${pat_study.clinic.name}</p>
                                                    <small>Medic: ${pat_study.medic.name}</small>
                                                </div>

                                            </a>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${loggedUser.role eq 2 || loggedUser.role eq 4}">
                        <div class="col">
                            <div class="card bg-light anim-content">
                                <div class="card-body">
                                    <p class="card-title h4">As a medic, here are your medical studies</p>
                                    <div class="list-group">
                                        <c:forEach items="${medic_studies}" var="med_study">
                                            <a href="${studyPath}${medic_encodeds.get(med_study.order_id)}" class="list-group-item list-group-item-action">
                                                    <div class="d-flex w-100 justify-content-between">
                                                        <h5 class="mb-1">Study type: ${med_study.study.name}</h5>
                                                        <small>Date: ${med_study.date}</small>
                                                    </div>
                                                    <div class="d-flex w-100 justify-content-between">
                                                        <p class="mb-1">Clinic: ${med_study.clinic.name}</p>
                                                        <small>Patient: ${med_study.patient_name}</small>
                                                    </div>
                                            </a>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${loggedUser.role ge 3}">
                        <div class="col">
                            <div class="card bg-light anim-content">
                                <div class="card-body">
                                    <p class="card-title h4">As a clinic, here are your medical studies</p>
                                    <div class="list-group">
                                        <c:forEach items="${clinic_studies}" var="cli_study">
                                            <a href="${studyPath}${clinic_encodeds.get(cli_study.order_id)}" class="list-group-item list-group-item-action">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1">Study type: ${cli_study.study.name}</h5>
                                                    <small>Date: ${cli_study.date}</small>
                                                </div>
                                                <div class="d-flex w-100 justify-content-between">
                                                    <p class="mb-1">Clinic: ${cli_study.clinic.name}</p>
                                                    <small>Patient: ${cli_study.patient_name}</small>
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