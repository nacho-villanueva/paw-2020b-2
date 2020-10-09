<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/uploadresults.css"/>">
</head>
<body>
<c:url value="/result-uploaded" var="postPath"/>
<div class="main-container">
    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
    <div class="row justify-content-center pb-8 mt-4">
        <div class="card pb-8 mt-4" style="width: 40rem;">
            <div class="card-body">
                <div class="row">
                        <p class="card-title h4 mx-auto mt-3"><spring:message code="upload-result.body.order.id.label"/></p>
                </div>
                <hr class="mt-3 mb-4"/>
                <div class="row justify-content-start">
                    <div class="col type"><p class="type-title"><spring:message code="upload-result.body.order.id.label"/></p> <c:out value="${id}" /></div>
                    <div class="col type"><p class="type-title"><spring:message code="upload-result.body.order.patient_name.label"/></p><c:out value="${order.patient_name}" /></div>
                    <div class="w-100"></div>
                    <div class="col"><p class="type-title"><spring:message code="upload-result.body.order.patient_insurance_plan.label"/></p><c:out value="${order.patient_insurance_plan}" /></div>
                    <div class="col"><p class="type-title"><spring:message code="upload-result.body.order.patient_insurance_number.label"/></p><c:out value="${order.patient_insurance_number}" /></div>
                </div>
                <hr class="mt-2 mb-4"/>
                <form:form method="POST" action="${postPath}" enctype="multipart/form-data" modelAttribute="resultForm">
                    <div class="form-inline">
                        <label for="resultFiles" class="select-label"><spring:message code="upload-result.body.form.resultFiles.label"/></label>
                        <input multiple required type="file" class="form-control-file mt-2" id="resultFiles" name="files" accept="image/png, image/jpeg">
                        <small class="text-muted"><spring:message code="upload-result.body.form.resultFiles.placeholder"/></small>
                    </div>
                    <hr class="mt-3 mb-4"/>
                    <div class="form-row">
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput1"><spring:message code="upload-result.body.form.responsible_name.label"/> </label>
                                <spring:message code="upload-result.body.form.responsible_name.placeholder" var="responsible_namePlaceholder"/>
                                <form:input type="text" class="form-control" id="responsibleInput1" placeholder="${responsible_namePlaceholder}" path="responsible_name" required="required"/>
                                <form:errors path="responsible_name" cssClass="error"/>
                            </div>
                        </div>
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput2"><spring:message code="upload-result.body.form.responsible_licence_number.label"/> </label>
                                <spring:message code="upload-result.body.form.responsible_licence_number.placeholder" var="responsible_numberPlaceholder"/>
                                <form:input type="text" class="form-control" id="responsibleInput2" placeholder="${responsible_numberPlaceholder}" path="responsible_licence_number" required="required"/>
                                <form:errors path="responsible_licence_number" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-inline mt-3 signature">
                        <label for="exampleFormControlFile2" class="select-label"><spring:message code="upload-result.body.form.exampleFormControlFile2.label"/></label>
                        <input required type="file" class="form-control-file mt-2" id="exampleFormControlFile2" name="sign" accept="image/png, image/jpeg">
                        <small class="text-muted"><spring:message code="upload-result.body.form.exampleFormControlFile2.format"/></small>
                    </div>
                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button"><spring:message code="upload-result.body.form.cancel"/></a>
                    <button class="btn submit-btn mt-4 mb-2 float-right" type="submit"><spring:message code="upload-result.body.form.submit"/> </button>
                </form:form>

            </div>
        </div>
    </div>
</div>
<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>
