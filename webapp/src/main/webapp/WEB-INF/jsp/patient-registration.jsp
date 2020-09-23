<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="fragments/include-header.jsp"%>
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
    <form>
        <div class="row">
            <fieldset class="form-group">
                <label for="firstName" class="bmd-label-floating">First Name</label>
                <input type="text" class="form-control" id="firstName">
            </fieldset>
            <fieldset class="form-group">
                <label for="lastName" class="bmd-label-floating">Last Name</label>
                <input type="text" class="form-control" id="lastName">
            </fieldset>
        </div>
        <fieldset class="form-group row">
            <label for="medicalInsurancePlan" class="bmd-label-floating">Medical Insurance Plan</label>
            <input type="text" class="form-control" id="medicalInsurancePlan">
        </fieldset>
        <fieldset class="form-group row">
            <label for="medicalInsuranceNumber" class="bmd-label-floating">Medical Insurance Number</label>
            <input type="text" class="form-control" id="medicalInsuranceNumber">
        </fieldset>
        <input type="submit" value="Continue" class="row btn btn-lg btn-light  bg-primary btn-block">
    </form>
</div>


<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>