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
        <jsp:param name="current" value="request-orders"/>
    </jsp:include>
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column main-container">
        <div class="row justify-content-center">
            <div class="card request-card">
                <div class="card-body">
                    <div class="row">
                        <p class="card-title h4 mx-auto mt-3"><spring:message code="request-studies.body.title" /></p>
                    </div>

                    <c:url var="post_requestorders"  value="/request-orders"/>
                    <f:form action="${post_requestorders}" method="post" modelAttribute="requestOrdersForm">
                        <hr class="divider"/>
                        <div class="row mx-1">
                            <fieldset class="form-group col">
                                <label class="bmd-label-static"><spring:message code="request-studies.body.form.patientMail.label"/> </label>
                                <f:input type="email" path="patientEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                <f:errors path="patientEmail" cssClass="text-danger" element="small" />
                            </fieldset>
                        </div>
                        <div class="row mx-1">
                            <fieldset class="form-group">
                                <label class="bmd-label-static"><spring:message code="request-order.body.form.studyId.label"/></label>
                                <spring:message code="create-order.body.form.studyId.placeholder" var="studyIdPlaceholder"/>
                                <f:select title="${studyIdPlaceholder}" cssErrorClass="selectpicker is-invalid" id="medicalStudy" cssClass="selectpicker" data-live-search="true" path="studyId" data-style="text-primary">
                                    <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                                </f:select>
                                <f:errors path="studyId" cssClass="text-danger" element="small" />
                            </fieldset>
                        </div>
                        <hr class="mt-4 mb-2"/>

                        <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button">
                            <spring:message code="request-order.body.form.button.cancel"/>
                        </a>
                        <button class="btn action-btn mt-4 mb-2 float-right"
                                type="submit" name="submit" value="sendRequest">
                            <spring:message code="request-order.body.form.button.send"/>
                        </button>

                    </f:form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>
