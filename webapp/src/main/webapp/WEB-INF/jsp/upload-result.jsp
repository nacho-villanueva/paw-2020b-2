<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">

    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">

    <title>Upload Results</title>
</head>
<body>
<c:url value="/result-uploaded" var="postPath"/>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <a class="navbar-brand ml-4" href="<c:url value="/" />"> <i class="fas fa-laptop-medical fa-lg"></i> MedTransfer</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/" />">Home </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/create-order" />">Create a new order</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="#">View studies (WIP) <span class="sr-only">(current)</span></a>
            </li>
        </ul>
        <span class="navbar-text">
            <!--space for future user profile icon-->
        </span>
    </div>
</nav>
<div class="container">
    <div class="row justify-content-center pb-8 mt-4">
        <div class="card pb-8 mt-4" style="width: 40rem;">
            <div class="card-header text-center">
                Upload result
            </div>
            <div class="card-body">
                <div class="row justify-content-start">
                    <div class="col">Order Number: ${id}</div>
                    <div class="col">Patient: ${order.patient.name}</div>
                    <div class="w-100"></div>
                    <div class="col">Patient Insurance Plan: ${order.patient_insurance_plan}</div>
                    <div class="col">Patient Insurance Number: ${order.patient_insurance_number}</div>
                </div>
                <hr class="mt-2 mb-3"/>
                <form:form method="POST" action="${postPath}" enctype="multipart/form-data" modelAttribute="resultForm">
                    <div class="form-inline">
                        <label for="resultFiles">Select files to upload</label>
                        <input multiple required type="file" class="form-control-file" id="resultFiles" name="files" accept="image/png, image/jpeg">
                    </div>
                    <hr class="mt-3 mb-4"/>
                    <div class="form-row">
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput1">Responsible medic:</label>
                                <form:input type="text" class="form-control" id="responsibleInput1" placeholder="Dr. Hipo Tenusa" path="responsible_name"/>
                                <form:errors path="responsible_name" cssClass="error"/>
                            </div>
                        </div>
                        <div class="col">
                            <div class="form-group">
                                <label for="responsibleInput2">Responsible medic's licence number:</label>
                                <form:input type="text" class="form-control" id="responsibleInput2" placeholder="000001" path="responsible_licence_number"/>
                                <form:errors path="responsible_licence_number" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-inline mt-2">
                        <label for="exampleFormControlFile2">Upload signature</label>
                        <input type="file" class="form-control-file" id="exampleFormControlFile2" name="sign" accept="image/png, image/jpeg">
                    </div>
                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button">Cancel</a>
                    <button class="btn btn-primary mt-4 mb-2 float-right" type="submit">Submit results</button>
                </form:form>

            </div>
        </div>
    </div>
</div>


<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
</body>
</html>
