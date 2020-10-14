<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">

<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/home.css"/>">
    <title><spring:message code="appname"/></title>
</head>

<body id="page-top">

<c:url value="/create-order" var="createPath"/>
<c:url value="/view-study/" var="studyPath"/>
<c:url value="/logout" var="logoutPath"/>

<!-- Page Wrapper -->
<div id="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="home"/>
    </jsp:include>
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

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
                            <p class="card-title h4"><spring:message code="home.body.card.studies.title"/></p>
                            <div class="list-group">
                                <c:forEach items="${orders}" var="o">
                                    <a href="${studyPath}${orders_encoded.get(o.order_id)}" class="list-group-item list-group-item-action">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h5 class="mb-1"><spring:message code="home.body.card.studies.studyType" arguments="${o.study.name}" /></h5>
                                            <small><spring:message code="home.body.card.studies.date" arguments="${o.date}" /></small>
                                        </div>
                                        <div class="d-flex w-100 justify-content-between">
                                            <p class="mb-1"><spring:message code="home.body.card.studies.clinic" arguments="${o.clinic.name}" /></p>
                                            <small><spring:message code="home.body.card.studies.medic" arguments="${o.medic.name}" /></small>
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

</body>
</html>
