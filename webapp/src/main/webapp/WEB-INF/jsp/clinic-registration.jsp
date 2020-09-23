<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

    <%@ include file="fragments/include-header.jsp"%>
    <!-- bootstrap-select CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">

    <style>
        body{
            display: flex;
            justify-content: center;
            background: rgba(240, 239, 239, 1)
            linear-gradient(to bottom, var(--primary) 0%, var(--primary) 30%, rgba(240, 239, 239, 1) 30%, rgba(240, 239, 239, 1) 100%) no-repeat;
        }
        .main-container {
            position: relative;
            margin-top: 5%;
        }
    </style>
    <link rel="stylesheet" href="<c:url value="/resources/css/registration.css"/>">
</head>
<body>

<div class="p-5 col-auto card main-container mx-auto" >
    <div class="row justify-content-center">
        <h2>Before we let you through...</h2>
    </div>
    <div class="row justify-content-center">
        <h3>We need a couple more details.</h3>
    </div>

    <c:url var="apply" value="/apply-as-clinic"/>
    <f:form action="${apply}" method="post" modelAttribute="applyClinicForm">
        <fieldset class="form-group">
            <label for="clinicName" class="bmd-label-floating">Clinic's Name</label>
            <f:input type="text" class="form-control" id="clinicName" path="name"/>
        </fieldset>
        <fieldset class="form-group">
            <label for="clinicEmail" class="bmd-label-floating">Clinic's Email</label>
            <f:input type="text" class="form-control" id="clinicEmail" path="email"/>
        </fieldset>
        <fieldset class="form-group row">
            <label for="phoneNumber" class="bmd-label-floating">Phone Number</label>
            <f:input type="tel" class="form-control" id="phoneNumber" pattern="\+?[0-9\-]+" path="telephone"/>
            <small class="text-muted">+[Country Code][Area Code][Phone Number]</small>
        </fieldset>

        <label>Study Types </label>
        <f:select id="studyFields" class="selectpicker"  data-live-search="true" path="available_studies" data-style="btn-custom">
            <f:option value="-1" label="Choose your fields"/>
            <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
        </f:select>
        <input type="submit" value="Continue" class="row btn btn-lg btn-light  bg-primary btn-block">
    </f:form>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
<!-- bootstrap-select JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js" integrity="sha384-SfMwgGnc3UiUUZF50PsPetXLqH2HSl/FmkMW/Ja3N2WaJ/fHLbCHPUsXzzrM6aet" crossorigin="anonymous"></script>
</body>
</html>
