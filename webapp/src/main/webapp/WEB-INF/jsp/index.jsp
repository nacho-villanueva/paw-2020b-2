<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/homepage.css"/>">
</head>
<body>
<div class="main-container">
    <%@include file="fragments/navbar-fragment.jsp"%>

    <div class="card main-card bg-light">
        <div class="card-body">
            <h1 class="text-center text-highlight"><spring:message code="index.body.card.title1"/></h1>
            <h4 class="text-center text-highlight"><spring:message code="index.body.card.title2"/></h4>
            <hr class="divider my-4">
            <ul class="nav nav-pills nav-justified" id="myTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-item nav-link <c:if test="${not registration}">active</c:if>"
                       id="login-tab" data-toggle="tab" href="<c:url value="#login"/>" role="tab"
                       aria-controls="login" aria-selected="<c:choose><c:when test="${registration}">false</c:when><c:otherwise>true</c:otherwise></c:choose>"><spring:message code="index.body.card.option.login"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <c:if test="${registration}">active</c:if>"
                       id="register-tab" data-toggle="tab" href="<c:url value="#register"/>"
                       role="tab" aria-controls="register" aria-selected="<c:choose><c:when test="${registration}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="index.body.card.option.register"/></a>
                </li>
            </ul>

            <hr class="divider my-4">

            <div class="tab-content">
                <div id="login" class="tab-pane fade in <c:if test="${not registration}">show active</c:if>">

                    <c:url value="/login" var="loginUrl" />
                    <form action="${loginUrl}" method="post" class="form-signin" enctype="application/x-www-form-urlencoded" >
                        <fieldset class="form-group">
                            <label for="loginEmail" class="bmd-label-static"><spring:message code="index.body.card.loginForm.label.email"/></label>
                            <input name="login_email" type="email" class="form-control" id="loginEmail" placeholder="<spring:message code="index.body.card.loginForm.label.email"/>"  />
                        </fieldset>
                        <fieldset class="form-group">
                            <label for="loginPassword" class="bmd-label-static"><spring:message code="index.body.card.loginForm.label.password"/></label>
                            <input name="login_password" type="password" class="form-control" id="loginPassword" placeholder="<spring:message code="index.body.card.loginForm.label.password"/>"/>
                        </fieldset>

                        <div class="checkbox">
                            <label>
                                <input name="rememberme" type="checkbox"/><spring:message code="index.body.card.loginForm.label.rememberMe"/>
                            </label>
                        </div>
                        <div>
                            <c:if test="${loginError}"><div class="alert alert-danger" role="alert"><spring:message code="index.body.card.loginForm.errorLogin"/></div></c:if>
                            <c:if test="${registrationSuccess}"><div class="alert alert-success" role="alert"><spring:message code="index.body.card.loginForm.successRegister"/></div></c:if>
                        </div>
                        <div class="row justify-content-center">
                            <input type="submit" class="row btn btn-lg action-btn" value="<spring:message code="index.body.card.loginForm.submit"/>">
                        </div>
                    </form>
                </div>



                <div id="register" class="tab-pane fade<c:if test="${registration}">show active</c:if>">
                    <c:url var="register"  value="/register"/>
                    <f:form action="${register}" class="form-signin" modelAttribute="registerUserForm" method="post">
                        <fieldset class="form-group">
                            <label for="registerEmail" class="bmd-label-static"><spring:message code="index.body.card.registerForm.label.email"/></label>
                            <spring:message code="index.body.card.registerForm.label.email" var="registerEmail"/>
                            <f:input type="email" path="email" cssClass="form-control" cssErrorClass="form-control is-invalid" id="registerEmail" required="required" placeholder="${registerEmail}" />
                            <f:errors path="email" cssClass="invalid-feedback" element="small"/>
                        </fieldset>

                        <fieldset class="form-group">
                            <label for="registerPassword" class="bmd-label-static"><spring:message code="index.body.card.registerForm.label.password"/></label>
                            <spring:message code="index.body.card.registerForm.label.password" var="registerPassword"/>
                            <f:input type="password" path="password" cssClass="form-control" cssErrorClass="form-control is-invalid" id="registerPassword" required="required" placeholder="${registerPassword}" />
                            <f:errors path="password" cssClass="invalid-feedback" element="small"/>
                        </fieldset>
                        <fieldset class="form-group">
                            <label for="registerPasswordConfirm" class="bmd-label-static"><spring:message code="index.body.card.registerForm.label.repeatPassword"/></label>
                            <spring:message code="index.body.card.registerForm.label.repeatPassword" var="registerRepeatPassword"/>
                            <input type="password" class="form-control" required id="registerPasswordConfirm" placeholder="${registerRepeatPassword}" />
                        </fieldset>
                        <div class="row justify-content-center">
                            <input type="submit" class="row btn btn-lg action-btn" value="<spring:message code="index.body.card.registerForm.submit"/>"/>
                        </div>
                    </f:form>
                </div>
            </div>
        </div>
    </div>

</div>

<%@ include file="fragments/include-scripts.jsp"%>
<script src="<c:url value="/resources/js/LoginFormValidations.js"/>"></script>>
</body>
</html>