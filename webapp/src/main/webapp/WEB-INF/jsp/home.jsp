<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">

<head>

    <link href="<c:url value="/resources/css/sbadmin.css" />" rel="stylesheet">
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/home.css"/>">
    <title>MedTransfer</title>

    <!-- Custom styles for this template-->


</head>

<body id="page-top">

<c:url value="/create-order" var="createPath"/>
<c:url value="/view-study/" var="studyPath"/>
<c:url value="/logout" var="logoutPath"/>
<c:url value="/register-as-medic" var="applyMedicPath"/>
<c:url value="/register-as-clinic" var="applyClinicPath"/>

<!-- Page Wrapper -->
<div id="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="orders"/>
    </jsp:include>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <div class="row justify-content-center">

            <div class="col-sm-3">
                <div class="my-4 py-5  float-right">
                    <p class="h5 lead anim-content">Welcome back, </p><p class="h4 anim-content"><c:out value="${loggedUser.email}" /></p>
                </div>
            </div>
            <div class="col-sm-1">
                <div class="py-2 mx-2" style="margin-top: 2.5em;">
                    <c:if test="${loggedUser.role eq 2 || loggedUser.role eq 4}">
                        <div class="mt-2">
                            <div>
                                <a href="${createPath}" class="btn create-btn mb-4" role="button">Create a new order</a>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${loggedUser.role eq 1 || loggedUser.role eq 3}">
                        <div class="mt-2">
                            <div>
                                <a href="${applyMedicPath}" class="btn create-btn" role="button">Apply as a Medic</a>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${loggedUser.role eq 1 || loggedUser.role eq 2}">
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
                    <c:if test="${loggedUser.role ge 3}">
                        <div class="col">
                            <div class="card bg-light anim-content">
                                <div class="card-body">
                                    <p class="card-title h4">As a clinic, here are your medical studies</p>
                                    <div class="list-group">
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
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <a class="btn btn-primary" href="login.html">Logout</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
<!-- Custom scripts for all pages-->
<script src="<c:url value="/resources/js/sbadmin.js" />"></script>

</body>
</html>
