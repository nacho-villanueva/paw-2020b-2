<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/request-studies.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">
    <title><spring:message code="appname"/></title>
</head>

<body>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<!-- Page Wrapper -->
<div id="wrapper" class="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="access-requests"/>
    </jsp:include>
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column main-container">
        <div class="row justify-content-center">
            <div class="card request-card">
                <div class="card-body">
                    <div class="row">
                        <p class="card-title h4 mx-auto mt-3"><spring:message code="access-request.body.title" /></p>
                    </div>
                    <c:if test="${success}"><div class="alert alert-success" role="alert">
                        <spring:message code="request-order.body.form.successMessage"/>
                    </div></c:if>
                    <c:url var="post_requestorders"  value="/access-request/complete"/>
                    <f:form action="${post_requestorders}" method="post" modelAttribute="requestOrdersForm">

                        <hr class="divider"/>
                        <div class="row mx-1">
                            <fieldset class="form-group col">
                                <label class="bmd-label-static"><spring:message code="access-request.body.form.medic.label"/> </label>
                                <p id="medicName" class="lead"><c:out value="${medic.name}"/> </p>
                            </fieldset>
                        </div>
                        <div class="row mx-1">
                            <fieldset class="form-group">
                                <label class="bmd-label-static"><spring:message code="access-request.body.form.studyId.label"/></label>
                                <p id="studyType" class="lead"><c:out value="${studyType.name}"/> </p>
                            </fieldset>
                        </div>
                        <hr class="mt-4 mb-2"/>

                        <f:input type="hidden" path="medicId" id="medicId"/>
                        <f:input type="hidden" path="patientEmail" id="patientEmail"/>
                        <f:input type="hidden" path="studyTypeId" id="studyTypeId"/>

                        <button class="btn btn-secondary mt-4 mb-2 float-left"
                                type="submit" name="submit" value="false">
                            <spring:message code="access-request.body.form.button.deny"/>
                        </button>
                        <button class="btn action-btn mt-4 mb-2 float-right"
                                type="submit" name="submit" value="true">
                            <spring:message code="access-request.body.form.button.accept"/>
                        </button>

                    </f:form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
</body>
