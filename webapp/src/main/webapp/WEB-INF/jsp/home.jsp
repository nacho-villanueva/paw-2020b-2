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
<body>
<div class="main-container">
    <%@include file="fragments/navbar-fragment.jsp"%>
    <div class="row justify-content-center">

        <div class="col">
            <div class="my-4 py-5  float-right">
                <p class="h4 lead">Welcome back, </p><p class="h4">${loggedUser.email}</p>
            </div>
        </div>
        <c:if test="${loggedUser.role eq 1}">
            <div class="col">
                <div class="float-left py-2 mx-2" style="margin-top: 2.5em;">
                    <c:if test="${loggedUser.role eq 2 || loggedUser.role eq 4}">
                        <div class="my-4 py-5">
                            <div>
                                <a href="${createPath}" class="btn create-btn mb-4" role="button">Create a new order</a>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${loggedUser.role eq 1}">
                        <div class="mt-2">
                            <div>
                                <a href="${createPath}" class="btn create-btn" role="button">Apply as a Medic</a>
                            </div>
                        </div>
                        <div class="mb-2">
                            <div>
                                <a href="${createPath}" class="btn create-btn" role="button">Apply as a Clinic</a>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:if>

    </div>


    <c:choose>
        <c:when test="${has_studies eq true}">
            <div class="align-items-end result-not">
                <h1 class="text-center mt-5 py-5">It seems there are no studies linked to your account right now.</h1>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row">
                    <c:if test="${has_studies != true}">
                        <div class="col">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <p class="card-title h4">As a patient, here are your medical studies</p>
                                    <div class="list-group">
                                        <c:forEach items="${patient_studies}" var="pat_study" varStatus="counter">
                                            <a href="${studyPath}/${patient_encodeds[counter.index]}" class="list-group-item list-group-item-action">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1">Study type: ${pat_study.study.name}</h5>
                                                    <small>Date: ${pat_study.date}</small>
                                                </div>
                                                <p class="mb-1">Clinic: ${pat_study.clinic.name}</p>
                                                <small>Medic: ${pat_study.medic.name}</small>
                                            </a>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${false != true}">
                        <div class="col">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <p class="card-title h4">As a medic, here are your medical studies</p>
                                    <div class="list-group">
                                        <c:forEach items="${medic_studies}" var="med_study" varStatus="counter">
                                            <a href="${studyPath}/${medic_encodeds[counter.index]}" class="list-group-item list-group-item-action">
                                                    <div class="d-flex w-100 justify-content-between">
                                                        <h5 class="mb-1">Study type: ${med_study.study.name}</h5>
                                                        <small>Date: ${med_study.date}</small>
                                                    </div>
                                                    <p class="mb-1">Clinic: ${med_study.clinic.name}</p>
                                                    <small>Patient: ${med_study.patient_name}</small>
                                                </a>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                        </div>
                    </c:if>
                    <c:if test="${loggedUser.role eq 1}">
                        <div class="col">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <p class="card-title h4">As a clinic, here are your medical studies</p>
                                    <div class="list-group">
                                        <c:forEach items="${clinic_studies}" var="cli_study" varStatus="counter">
                                            <a href="${studyPath}/${clinic_encodeds[counter.index]}" class="list-group-item list-group-item-action">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1">Study type: ${cli_study.study.name}</h5>
                                                    <small>Date: ${cli_study.date}</small>
                                                </div>
                                                <p class="mb-1">Clinic: ${cli_study.clinic.name}</p>
                                                <small>Patient: ${cli_study.patient_name}</small>
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