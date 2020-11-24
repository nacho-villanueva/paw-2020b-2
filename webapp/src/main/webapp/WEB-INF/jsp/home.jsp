<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">

<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/home.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">
    <title><spring:message code="appname"/></title>
</head>

<body id="page-top">

<c:url value="/create-order" var="createPath"/>
<c:url value="/view-study/" var="studyPath"/>
<c:url value="/logout" var="logoutPath"/>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<!-- Page Wrapper -->
<div id="wrapper" class="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="home"/>
    </jsp:include>
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column main-container">

        <div class="pl-5">
            <div class="my-4 py-5">
                <p class="h4 lead anim-content"><spring:message code="home.body.greeting" arguments="${loggedUser.email}"/></p>
            </div>
        </div>

        <c:if test="${loggedUser.isVerifying() eq true}">
            <div class="row justify-content-center mt-0 mb-4 anim-content">
                <div class="alert alert-info pb-3" role="alert">
                    <h4 class="alert-heading"><spring:message code="home.body.verifying.title"/></h4>
                    <p><spring:message code="home.body.verifying.text1"/><br/>
                        <spring:message code="home.body.verifying.text2"/></p>
                    <hr>
                    <p class="mb-0"><spring:message code="home.body.verifying.text3"/></p>
                </div>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${hasStudies != true}">
                <div class="align-items-end result-not">
                    <h1 class="text-center mt-5 py-5 anim-content"><spring:message code="home.body.noStudies"/></h1>
                </div>
            </c:when>
        <c:otherwise>
            <div class="row mx-2">
                <div class="col">
                    <div class="card bg-light anim-content" style="margin-bottom: 7rem;">
                        <div class="card-body">
                            <p class="card-title h4"><spring:message code="home.body.card.studies.title"/></p>
                            <div class="list-group">
                                <c:forEach items="${orders}" var="o">
                                    <a href="${studyPath}${ordersEncoded.get(o.orderId)}" class="list-group-item list-group-item-action">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h5 class="mb-1"><spring:message code="home.body.card.studies.studyType" arguments="${o.study.name}" /></h5>
                                            <small><spring:message code="home.body.card.studies.date" arguments="${o.date}" /></small>
                                        </div>
                                        <div class="d-flex w-100 justify-content-between">
                                            <c:choose>
                                                <c:when test="${loggedUser.isMedic() == true && loggedUser.isVerifying() == false}">
                                                    <p class="mb-1"><spring:message code="home.body.card.studies.clinic" arguments="${o.clinic.name}"/></p>
                                                    <small><spring:message code="home.body.card.studies.patient" arguments="${o.patientName}"/></small>
                                                </c:when>
                                                <c:when test="${loggedUser.isClinic() == true && loggedUser.isVerifying() == false}">
                                                    <p class="mb-1"><spring:message code="home.body.card.studies.patient" arguments="${o.patientName}"/></p>
                                                    <small><spring:message code="home.body.card.studies.medic" arguments="${o.medic.name}"/></small>
                                                </c:when>
                                                <c:when test="${loggedUser.isPatient() == true}">
                                                    <p class="mb-1"><spring:message code="home.body.card.studies.clinic" arguments="${o.clinic.name}"/></p>
                                                    <small><spring:message code="home.body.card.studies.medic" arguments="${o.medic.name}"/></small>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
    <!-- End of Content Wrapper -->
</div>
<!-- End of Page Wrapper -->

<%@ include file="fragments/include-scripts.jsp"%>

</body>
</html>
