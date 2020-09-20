<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/viewstudy.css"/>">
</head>
<c:url value="/upload-result/${encodedId}" var="uploadPath"/>
<c:url value="/api/image" var="imageAssets"/>
<body>
<div class="main-container">
    <%@include file="fragments/navbar-fragment.jsp"%>
    <div class="row justify-content-center">
        <div class="col-sm-6">
            <div class="card order-card bg-light float-right">
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                            <p class="card-title h4">Order number: ${id}</p>
                            <p class="card-subtitle text-muted lead">Date: ${order.date}</p>
                        </div>
                        <div class="col">
                            <div class="row justify-content-end">
                                <a href="${uploadPath}" class="btn upload-btn mt-0 mb-3 mr-4" role="button">Upload results</a>
                            </div>

                        </div>
                    </div>
                    <hr class="mt-5 mb-4"/>
                    <div class="row justify-content-start">
                        <div class="col type"><p class="type-title">Patient:</p>${order.patient.name}</div>
                        <div class="col type"><p class="type-title">Medical clinic:</p>${order.clinic.name}</div>
                        <div class="w-100"></div>
                        <div class="col type"><p class="type-title">Patient insurance plan:</p>${order.patient_insurance_plan}</div>
                        <div class="col type"><p class="type-title">Patient insurance number:</p>${order.patient_insurance_number}</div>
                    </div>
                    <hr class="mt-3 mb-5"/>
                    <p class="card-text text-center h5">Study type: ${order.study.name}</p>
                    <c:choose>
                        <c:when test="${order.description != null}">
                            <p class="card-text text-center">${order.description}</p>
                            <hr class="mt-5 mb-4"/>
                        </c:when>
                    </c:choose>
                    <div class="media">
                        <div class="media-body">
                            <h5 class="mt-0 mb-1 text-center">${order.medic.name}</h5>
                            <p class="text-center">M.N.:${order.medic.licence_number}</p>
                        </div>
                        <img src="${imageAssets}/study/${encodedId}?attr=identification" class="align-self-end ml-3" alt="the medic's signature" style="width: 5rem;">
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="card results-card bg-light float-left">
                <div class="card-body">
                    <p class="card-title h4">Results</p>
                    <c:choose>
                        <c:when test="${results.size() < 1}">
                            <div class="align-items-end result-not">
                                <h1 class="text-center mt-5 py-5">It seems there are no results to show yet!</h1>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <div class="col-4 float-left" style="overflow-y: scroll; overflow-x: hidden; height:32em;">
                                    <ul class="nav flex-column" id="myTab" role="tablist">
                                        <c:forEach items="${results}" var="result">
                                            <li class="nav-item">
                                                <a class="nav-link" id="res-${result.id}-tab" data-toggle="tab" href="#res-${result.id}" role="tab" aria-controls="res-${result.id}" aria-selected="false">
                                                    <div class="d-flex w-100 justify-content-between">
                                                        <p class="type-title">Date:</p> ${result.date}
                                                    </div>
                                                    <p>${result.responsible_name}</p>
                                                </a>
                                            </li>
                                            <br/>
                                        </c:forEach>
                                    </ul>
                                </div>
                                <div class="col-8 float-right">
                                    <div class="tab-content">
                                        <c:forEach items="${results}" var="result">
                                            <div id="res-${result.id}" class="tab-pane fade tab-result">
                                                <div class="card overflow-auto border-primary">
                                                    <div class="card-body">
                                                        <div class="row justify-content-start">
                                                            <div class="col type"><p class="type-title">Date:</p>${result.date}</div>
                                                            <div class="col type"><p class="type-title">Medical clinic:</p>${order.clinic.name}</div>
                                                        </div>
                                                        <hr class="mt-3 mb-4 text-center"/>
                                                        <img src="${imageAssets}/result/${encodedId}/${result.id}?attr=result-data" class="align-self-end ml-3" alt="We failed to retrieve image" style="max-height:14em; max-width:20em">
                                                        <hr class="mt-5 mb-4"/>
                                                        <div class="media">
                                                            <div class="media-body">
                                                                <h5 class="mt-0 mb-1 text-center">${result.responsible_name}</h5>
                                                                <p class="text-center">M.N.:${result.responsible_licence_number}</p>
                                                            </div>
                                                            <img src="${imageAssets}/result/${encodedId}/${result.id}?attr=identification" class="ml-1" alt="We failed to retrieve image" style="max-height: 5em;">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

</div>
<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>
