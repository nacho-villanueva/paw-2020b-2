<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <!-- bootstrap-select CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="<c:url value="/resources/css/profile.css"/>">

</head>
<body>
<c:url value="/api/image" var="imageAssets"/>

<div id="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="profile"/>
    </jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">

        <div class="main-container">
            <div class="row justify-content-center">
                <div class="card" style="width: 40em; margin-top: 2em;">
                    <div class="card-body">
                        <div class="row">
                            <p class="card-title h4 mx-auto mt-3"><spring:message code="profile-edit.body.title"/> </p>
                        </div>
                        <hr class="divider my-4">

                        <ul class="nav nav-pills nav-justified" id="myTab">
                            <li class="nav-item">
                                <a class="nav-link <c:if test="${role == 'user'}">active</c:if>"
                                   id="user-tab" href="<c:url value="/profile/edit/user/email"/>"
                                   aria-controls="user" aria-selected="<c:choose><c:when test="${role == 'user'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.user"/> </a>
                            </li>

                            <c:choose>
                                <c:when test="${not empty patient}">
                                    <li class="nav-item">
                                        <a class="nav-link <c:if test="${role == 'patient'}">active</c:if>"
                                           id="patient-tab" href="<c:url value="/profile/edit/patient"/>"
                                           aria-controls="patient" aria-selected="<c:choose><c:when test="${role == 'patient'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.patient"/> </a>
                                    </li>
                                </c:when>

                                <c:when test="${not empty medic}">
                                    <li class="nav-item">
                                        <a class="nav-link <c:if test="${role == 'medic'}">active</c:if>"
                                           id="medic-tab" href="<c:url value="/profile/edit/medic"/>"
                                           aria-controls="medic" aria-selected="<c:choose><c:when test="${role == 'medic'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.medic"/> </a>
                                    </li>
                                </c:when>

                                <c:when test="${not empty clinic}">
                                    <li class="nav-item">
                                        <a class="nav-link <c:if test="${role == 'clinic'}">active</c:if>"
                                           id="clinic-tab" href="<c:url value="/profile/edit/clinic"/>"
                                           aria-controls="clinic" aria-selected="<c:choose><c:when test="${role == 'clinic'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.clinic"/> </a>
                                    </li>
                                </c:when>

                                <c:otherwise/>

                            </c:choose>
                        </ul>

                        <hr class="divider my-4">

                        <c:choose>
                            <c:when test="${role == 'user'}">
                                <c:choose>
                                    <c:when test="${not empty editUserEmailForm}">
                                        <jsp:include page="fragments/edit-user-form-email-fragment.jsp"/>
                                    </c:when>
                                    <c:when test="${not empty editUserPasswordForm}">
                                        <jsp:include page="fragments/edit-user-form-password-fragment.jsp"/>
                                    </c:when>
                                    <c:otherwise/>
                                </c:choose>
                            </c:when>
                            <c:when test="${role == 'patient'}">
                                <jsp:include page="fragments/edit-patient-form-fragment.jsp"/>
                            </c:when>
                            <c:when test="${role == 'medic'}">
                                <jsp:include page="fragments/edit-medic-form-fragment.jsp"/>
                            </c:when>
                            <c:when test="${role == 'clinic'}">
                                <jsp:include page="fragments/edit-clinic-form-fragment.jsp"/>
                            </c:when>
                        </c:choose>

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
