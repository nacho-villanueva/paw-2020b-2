<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: matia
  Date: 05-Sep-20
  Time: 6:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en" style="position: relative; min-height: 100%">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">

    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">

    <title>View Study</title>
</head>
<c:url value="/upload-result/${encodedId}" var="uploadPath"/>
<c:url value="/assets/image/" var="imageAssets"/>
<body>
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
    <div class="row justify-content-center pb-8">
        <div class="card pb-8 mt-4" style="width: 40rem;">
            <div class="card-header text-center">
                Order
            </div>
            <div class="card-body">
                <h5 class="card-title">Order number: ${id}</h5>
                <hr class="mt-3 mb-2"/>
                <div class="row justify-content-start">
                    <div class="col">Date: ${order.date}</div>
                    <div class="col">Patient: ${order.patient.name}</div>
                    <div class="col">Medical clinic: ${order.clinic.name}</div>
                    <div class="w-100"></div>
                    <div class="col">Patient insurance plan: ${order.patient_insurance_plan}</div>
                    <div class="col">Patient insurance number: ${order.patient_insurance_number}</div>
                    <div class="col">Study type: ${order.study.name}</div>
                </div>
                <hr class="mt-3 mb-4"/>
                <p class="card-text text-center">${order.description}
                <hr class="mt-5 mb-4"/>
                <div class="media">
                    <div class="media-body">
                        <h5 class="mt-0 mb-1 text-center">${order.medic.name}</h5>
                        <p class="text-center">M.N.:${order.medic.licence_number}</p>
                    </div>
                    <img src="${imageAssets}/order/${encodedId}/identification" class="align-self-end ml-3" alt="the medic's signature" style="width: 5rem;">
                </div>
            </div>
        </div>
    </div>
    <div class="row justify-content-center align-items-end">
        <a href="${uploadPath}" class="btn btn-primary align-self-center align-items-end my-4" role="button">Upload results</a>
    </div>
    <c:choose>
        <c:when test="${results eq null}">
            <h1 class="text-center mt-6">It seems there are no results to show yet!</h1>
        </c:when>
        <c:otherwise>
            <c:forEach items="${results}" var="result">
                <div class="row justify-content-center pb-8">
                    <div class="card pb-8 mb-4" style="width: 45rem;">
                        <div class="card-header text-center">
                            Result
                        </div>
                        <div class="card-body">
                            <div class="row justify-content-start">
                                <div class="col">Date: ${result.date}</div>
                                <div class="col">Clinic: ${order.clinic.name}</div>
                            </div>
                            <hr class="mt-3 mb-4 text-center"/>
                            <img src="${imageAssets}/result/${encodedId}/${result.id}/result-data" class="align-self-end ml-3" alt="/assets/image/result/${id}/result-data" style="width: 30rem;">
                            <hr class="mt-5 mb-4"/>
                            <div class="media">
                                <div class="media-body">
                                    <h5 class="mt-0 mb-1 text-center">${result.responsible_name}</h5>
                                    <p class="text-center">M.N.:${result.responsible_licence_number}</p>
                                </div>
                                <img src="${imageAssets}/result/${encodedId}/${result.id}/identification" class="align-self-end ml-3" alt="/assets/image/result/${id}/identification" style="width: 5rem;">
                            </div>
                        </div>
                    </div>
                </div>

            </c:forEach>
        </c:otherwise>
    </c:choose>


</div>



<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
</body>
</html>
