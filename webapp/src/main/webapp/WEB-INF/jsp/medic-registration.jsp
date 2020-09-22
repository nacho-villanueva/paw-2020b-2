<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

    <%@ include file="fragments/include-header.jsp"%>


    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>

    <!-- (Optional) Latest compiled and minified JavaScript translation files -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/i18n/defaults-*.min.js"></script>


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
</head>
<body>

<div class="p-5 col-auto card main-container mx-auto" >
    <div class="row justify-content-center">
        <h2>Before we let you through...</h2>
    </div>
    <div class="row justify-content-center">
        <h3>We need a couple more details.</h3>
    </div>
    <c:url var="apply" value="/apply-as-medic"/>
    <f:form action="${apply}" class="form-signin" method="post" modelAttribute="applyMedicForm">
        <div class="row">
            <fieldset class="form-group">
                <label for="firstName" class="bmd-label-floating">First Name</label>
                <f:input type="text" cssClass="form-control" required="required" id="firstName"  path="first_name"/>
            </fieldset>
            <fieldset class="form-group">
                <label for="lastName" class="bmd-label-floating">Last Name</label>
                <f:input type="text" cssClass="form-control" required="required" id="lastName" path="last_name"/>
            </fieldset>
        </div>
        <fieldset class="form-group row">
            <label for="phoneNumber" class="bmd-label-floating">Phone Number</label>
            <f:input type="tel" cssClass="form-control" required="required" id="phoneNumber" pattern="\+?[0-9\-]+" path="telephone"/>
            <small class="text-muted">+[Country Code][Area Code][Phone Number]</small>
        </fieldset>
        <fieldset class="form-group row">
            <label for="licenseNumber" class="bmd-label-floating">License Number</label>
            <f:input type="text" cssClass="form-control" required="required" pattern="[a-zA-Z0-9]" id="licenseNumber" path="licence_number"/>
        </fieldset>
        <fieldset class="mt-4 form-group">
            <label for="identificationFile" class="bmd-label-floating">Seal and Signature</label>
            <input required type="file" name="orderAttach" accept="image/png, image/jpeg" id="identificationFile"/>
            <small class="text-muted">The seal and signature should be visible.</small>
        </fieldset>


        <label>Study Type </label>
        <f:select id="medicalFields" class="selectpicker"  data-live-search="true" path="known_fields" >
            <f:option value="-1" label="Choose your fields"/>
            <f:options items="${fieldsList}" itemLabel="name" itemValue="id"/>
        </f:select>


        <input type="submit" value="Continue" class="row btn btn-lg btn-light  bg-primary btn-block">
    </f:form>
</div>


<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>
