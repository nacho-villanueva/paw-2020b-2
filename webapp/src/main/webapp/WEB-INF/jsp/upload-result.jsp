<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <%@ include file="fragments/include-header.jsp"%>

    <link rel="stylesheet" href="<c:url value="/resources/css/uploadresults.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">
</head>
<body>
<c:url value="/upload-result/${encodedId}" var="postPath"/>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<div id="wrapper" class="wrapper">
    <jsp:include page="fragments/sidebar-fragment.jsp" />
    <div id="content-wrapper" class="main-container d-flex flex-column">
    <div class="row justify-content-center pb-8 mt-4">
        <div class="card pb-8 mt-4" style="width: 40rem;">
            <div class="card-body">
                <div class="row">
                        <p class="card-title h4 mx-auto mt-3"><spring:message code="upload-result.body.title"/></p>
                </div>
                <hr class="mt-3 mb-4"/>
                <div class="row justify-content-start">
                    <div class="col type"><p class="type-title"><spring:message code="upload-result.body.order.id.label"/></p> <c:out value="${id}" /></div>
                    <div class="col type"><p class="type-title"><spring:message code="upload-result.body.order.patientName.label"/></p><c:out value="${order.patientName}" /></div>
                    <div class="w-100"></div>
                    <div class="col"><p class="type-title"><spring:message code="upload-result.body.order.patientInsurancePlan.label"/></p><c:out value="${order.patientInsurancePlan}" /></div>
                    <div class="col"><p class="type-title"><spring:message code="upload-result.body.order.patientInsuranceNumber.label"/></p><c:out value="${order.patientInsuranceNumber}" /></div>
                </div>
                <hr class="mt-2 mb-4"/>
                <form:form method="POST" action="${postPath}" enctype="multipart/form-data" modelAttribute="resultForm">
                    <fieldset class="mt-4 form-group">
                        <label for="resultFiles" class="select-label mb-0"><spring:message code="upload-result.body.form.resultFiles.label"/>
                        <form:input multiple="true" type="file" class="form-control-file" id="resultFiles" accept="image/png, image/jpeg" path="files" />
                        </label><br>
                        <small class="text-muted"><spring:message code="upload-result.body.form.resultFiles.placeholder"/></small><br>
                        <form:errors path="files" cssClass="text-danger"/>
                    </fieldset>
                    <hr class="mt-3 mb-4"/>
                    <div class="form-row">
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput1"><spring:message code="upload-result.body.form.responsibleName.label"/> </label>
                                <spring:message code="upload-result.body.form.responsibleName.placeholder" var="responsibleNamePlaceholder"/>
                                <form:input type="text" cssClass="form-control" id="responsibleInput1" placeholder="${responsibleNamePlaceholder}" path="responsibleName"/>
                                <form:errors path="responsibleName" cssClass="text-danger"/>
                            </div>
                        </div>
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput2"><spring:message code="upload-result.body.form.responsibleLicenceNumber.label"/> </label>
                                <spring:message code="upload-result.body.form.responsibleLicenceNumber.placeholder" var="responsible_numberPlaceholder"/>
                                <form:input type="text" class="form-control" id="responsibleInput2" placeholder="${responsible_numberPlaceholder}" path="responsibleLicenceNumber"/>
                                <form:errors path="responsibleLicenceNumber" cssClass="text-danger"/>
                            </div>
                        </div>
                    </div>
                    <fieldset class="mt-4 form-group">
                        <label for="exampleFormControlFile2" class="select-label mb-0"><spring:message code="upload-result.body.form.exampleFormControlFile2.label"/>
                        <form:input type="file" cssClass="form-control-file" id="exampleFormControlFile2" path="sign" accept="image/png, image/jpeg" /></label><br>
                        <small class="text-muted"><spring:message code="upload-result.body.form.exampleFormControlFile2.format"/></small><br>
                        <form:errors path="sign" cssClass="text-danger"/>
                    </fieldset>
                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button"><spring:message code="upload-result.body.form.cancel"/></a>
                    <input type="submit" class="btn submit-btn mt-4 mb-2 float-right" value="<spring:message code="upload-result.body.form.submit" />" />
                </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>
