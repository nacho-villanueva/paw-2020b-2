<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <%@ include file="fragments/include-header.jsp"%>
</head>
<body>

<div id="wrapper">

    <div id="content-wrapper" class="d-flex flex-column">
        <div class="p-5 m-5 col-auto card main-container mx-auto" >
            <div class="card-header">
                <h3><spring:message code="create-field.body.header"/></h3>
            </div>
            <c:if test="${success}"><div class="alert alert-success" role="alert">
                <spring:message code="create-field.body.form.medicalfield" var="successType"/>
                <spring:message code="create-field.body.form.successMessage" arguments='${successType}'/>
            </div></c:if>
            <c:url value="/create-field" var="createTypePost" />
            <f:form action="${createTypePost}" method="post" modelAttribute="createTypeForm">
                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                    <span class="justify-content-center align-self-center mr-3">
                    <spring:message code="create-field.body.form.createnew"/>
                    </span>
                </div>
                <fieldset class="form-group">
                    <label class="bmd-label-floating"><spring:message code="create-field.body.form.name.label"/></label>
                    <f:input type="text" path="name" cssClass="form-control" cssErrorClass="form-control is-invalid" />
                    <f:errors path="name" cssClass="invalid-feedback" element="small" />
                </fieldset>
                <br>
                <input type="submit"  class="btn" style="width:100%; color:var(--primary); border:solid var(--primary) 1px; border-radius: 10px;" value="<spring:message code="create-field.body.form.submit"/>">
            </f:form>
            <a href="<c:url value='/create-field-return' />"><input type="submit" class="btn" style="width:100%; color:var(--primary); border:solid var(--primary) 1px; border-radius: 10px;" value="<spring:message code="create-field.body.form.return"/>"></a>
        </div>
    </div>
</div>
</body>
<%@include file="fragments/include-scripts.jsp"%>
</html>