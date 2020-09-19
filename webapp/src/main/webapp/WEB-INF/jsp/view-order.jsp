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
<c:url value="/assets/image/" var="imageAssets"/>
<body>
<div class="main-container">
    <%@include file="fragments/navbar-fragment.jsp"%>
    <div class="row justify-content-center">
        <div class="col-sm-6">
            <div class="card order-card bg-light">
                <div class="card-body">
                    <p class="card-title h4">Order number: ${id}</p>
                    <p class="card-subtitle text-muted lead">Date: ${order.date}</p>
                    <hr class="mt-3 mb-2"/>
                    <div class="row justify-content-start">
                        <div class="col type"><p class="type-title">Patient:</p>${order.patient.name}</div>
                        <div class="col type"><p class="type-title">Medical clinic:</p>${order.clinic.name}</div>
                        <div class="w-100"></div>
                        <div class="col type"><p class="type-title">Patient insurance plan:</p>${order.patient_insurance_plan}</div>
                        <div class="col type"><p class="type-title">Patient insurance number:</p>${order.patient_insurance_number}</div>
                    </div>
                    <hr class="mt-3 mb-4"/>
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
                        <img src="${imageAssets}/order/${encodedId}/identification" class="align-self-end ml-3" alt="the medic's signature" style="width: 5rem;">
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="card results-card bg-light">
                <div class="card-body">
                    <div class="row justify-content-center">
                        <a href="${uploadPath}" class="btn upload-btn mt-0 mb-3" role="button">Upload results</a>
                    </div>

                    <p class="card-title h4">Results</p>
                    <c:choose>
                        <c:when test="${results eq null}">
                            <div class="align-items-end result-not">
                                <h1 class="text-center mt-6 py-6">It seems there are no results to show yet!</h1>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row justify-content-center">
                                <div class="col-4">
                                    <div class="tab">
                                        <c:forEach items="${results}" var="result">
                                            <button class="tablinks" onclick="openResult(event, ${result.id})">Date:${result.date}</button>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="col-8">
                                    <c:forEach items="${results}" var="result">
                                        <div id="${result.id}" class="tabcontent">
                                            <img src="${imageAssets}/result/${encodedId}/${result.id}/result-data" class="align-self-end ml-3" alt="/assets/image/result/${id}/result-data" style="width: 30rem;">
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

</div>

<!--%@ include file="fragments/include-scripts.jsp"%-->

</body>
</html>
<script>
    function openResult(evt, resultId) {
        // Declare all variables
        var i, tabcontent, tablinks;

        // Get all elements with class="tabcontent" and hide them
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }

        // Get all elements with class="tablinks" and remove the class "active"
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }

        // Show the current tab, and add an "active" class to the link that opened the tab
        document.getElementById(resultId).style.display = "block";
        evt.currentTarget.className += " active";
    }
</script>
