<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/uploadresults.css"/>">

    <title>Upload Results</title>
</head>
<body>
<c:url value="/result-uploaded" var="postPath"/>
<div class="main-container">
    <%@include file="fragments/navbar-fragment.jsp"%>
    <div class="row justify-content-center pb-8 mt-4">
        <div class="card pb-8 mt-4" style="width: 40rem;">
            <div class="card-body">
                <div class="row">
                        <p class="card-title h4 mx-auto mt-3">Upload Result</p>
                </div>
                <hr class="mt-3 mb-4"/>
                <div class="row justify-content-start">
                    <div class="col type"><p class="type-title">Order Number:</p>${id}</div>
                    <div class="col type"><p class="type-title">Patient:</p>${order.patient.name}</div>
                    <div class="w-100"></div>
                    <div class="col"><p class="type-title">Patient Insurance Plan:</p>${order.patient_insurance_plan}</div>
                    <div class="col"><p class="type-title">Patient Insurance Number:</p>${order.patient_insurance_number}</div>
                </div>
                <hr class="mt-2 mb-4"/>
                <form:form method="POST" action="${postPath}" enctype="multipart/form-data" modelAttribute="resultForm">
                    <div class="form-inline">
                        <label for="resultFiles" class="select-label">Select files to upload</label>
                        <input multiple required type="file" class="form-control-file" id="resultFiles" name="files" accept="image/png, image/jpeg">
                    </div>
                    <hr class="mt-3 mb-4"/>
                    <div class="form-row">
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput1">Responsible medic:</label>
                                <form:input type="text" class="form-control" id="responsibleInput1" placeholder="Dr. Hipo Tenusa" path="responsible_name" required="required"/>
                                <form:errors path="responsible_name" cssClass="error"/>
                            </div>
                        </div>
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput2">Responsible medic's licence number:</label>
                                <form:input type="text" class="form-control" id="responsibleInput2" placeholder="000001" path="responsible_licence_number" required="required"/>
                                <form:errors path="responsible_licence_number" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-inline mt-3 signature">
                        <label for="exampleFormControlFile2">Upload signature</label>
                        <input required type="file" class="form-control-file" id="exampleFormControlFile2" name="sign" accept="image/png, image/jpeg">
                    </div>
                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button">Cancel</a>
                    <button class="btn submit-btn mt-4 mb-2 float-right" type="submit">Submit results</button>
                </form:form>

            </div>
        </div>
    </div>
</div>
<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>
