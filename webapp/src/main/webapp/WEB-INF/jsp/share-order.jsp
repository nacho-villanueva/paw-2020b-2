<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%@ include file="fragments/include-header.jsp"%>

    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">

    <style>
        .wrapper {
            padding-top: 4.5rem !important;
        }
    </style>
</head>
<body>

<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<div id="wrapper" class="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp">
        <jsp:param name="current" value="create-order"/>
    </jsp:include>

<div id="content-wrapper" class="d-flex flex-column main-container">
    <div class="row justify-content-center">
        <div class="card" style="width: 40em; margin-top: 2em; margin-bottom: 7rem;">
            <div class="card-header text-center justify-content-center">
                <h4 class="card-title h2 mx-auto mt-3"><spring:message code="share-order.body.title"/></h4>
                <h2 class="card-subtitle h4 mx-auto mt-3"><spring:message code="share-order.body.order-type" arguments="${orderType}"/></h2>
            </div>
            <div class="card-body">
                <c:url var="post_shareorder"  value="/share-order/${encodedId}"/>
                <f:form action="${post_shareorder}" method="post" modelAttribute="shareOrderForm">
                    <fieldset class="form-group col">
                        <label class="bmd-label-floating"><spring:message code="share-order.body.form.medic-email"/></label>
                        <f:input type="email" path="medicEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                        <f:errors path="medicEmail" cssClass="text-danger" element="small" />
                    </fieldset>
                    <input type="submit"  class="btn" style="width:100%; color:var(--primary); border:solid var(--primary) 1px; border-radius: 10px;" value="<spring:message code="share-order.body.form.submit"/>">
                </f:form>
                <a href="<c:url value='/view-study/${encodedId}' />"><input type="submit" class="btn btn-primary" style="width:100%;" value="<spring:message code="share-order.body.form.return"/>"></a>
            </div>
        </div>
    </div>
</div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>