<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/viewstudy.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">
</head>
<c:url value="/upload-result/${encodedId}" var="uploadPath"/>
<c:url value="/api/image" var="imageAssets"/>
<c:url value="/share-order/${encodedId}" var="shareOrderPath"/>
<body>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<div id="wrapper" class="wrapper">
    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="orders"/>
    </jsp:include>

<div id="content-wrapper" class="main-container d-flex flex-column">
    <div class="row justify-content-center">
        <div class="col-sm-5">
            <div class="card order-card bg-light float-right">
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                            <p class="card-title ml-3 h4"><spring:message code="view-study.body.card.order.title" arguments="${id}"/> </p>
                        </div>
                        <c:if test="${loggedUser.isPatient()}">
                            <div class="col">
                                <div class="row justify-content-end">
                                    <a href="${shareOrderPath}" class="btn upload-btn mt-0 mb-3 mr-4" role="button"><spring:message code="view-study.body.card.order.share" /></a>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="row">
                        <div class="col">
                            <p class="card-subtitle ml-3 text-muted lead"><spring:message code="view-study.body.card.order.date" arguments="${order.getLegacyDate()}"/></p>
                        </div>
                    </div>
                    <hr class="mt-3 mb-4"/>
                    <div class="row justify-content-start">
                        <div class="col type"><p class="type-title"><spring:message code="view-study.body.card.order.patientName.label"/> </p> <c:out value="${order.patientName}"/></div>
                        <div class="col type"><p class="type-title"><spring:message code="view-study.body.card.order.clinic.label"/></p> <c:out value="${order.clinic.name}"/></div>
                        <div class="w-100"></div>
                        <div class="col type"><p class="type-title"><spring:message code="view-study.body.card.order.patientInsurancePlan.label"/></p><c:out value="${order.patientInsurancePlan}"/></div>
                        <div class="col type"><p class="type-title"><spring:message code="view-study.body.card.order.patientInsuranceNumber.label"/></p><c:out value="${order.patientInsuranceNumber}"/></div>
                    </div>
                    <hr class="mt-3 mb-5"/>
                    <p class="card-text text-center h5"><spring:message code="view-study.body.card.order.study.label"/><c:out value="${order.study.name}"/></p>
                    <c:choose>
                        <c:when test="${order.description != null}">
                            <p class="card-text text-center"> <c:out value="${order.description}"/></p>
                            <hr class="mt-5 mb-4"/>
                        </c:when>
                    </c:choose>
                    <div class="media">
                        <div class="media-body">
                            <h5 class="mt-0 mb-1 text-center"><c:out value="${order.medic.name}"/></h5>
                            <p class="text-center"><spring:message code="view-study.body.card.order.licenceNumber.prefix" arguments="${order.medic.licenceNumber}"/></p>
                        </div>
                        <img src="${imageAssets}/study/${encodedId}?attr=identification" class="align-self-end ml-3" alt="<spring:message code="view-study.body.card.order.signature.alt"/>" style="width: 5rem; max-height: 5em;">
                    </div>
                </div>
                <c:if test="${successMedicName != ''}">
                    <div class="alert alert-success">
                        <spring:message code="view-study.body.card.share.success" arguments="${successMedicName}"/>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="card results-card bg-light float-left">
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                            <p class="card-title h4"><spring:message code="view-study.body.card.results.title"/> </p>
                        </div>
                        <div class="col">
                            <div class="row justify-content-end">
                                <c:if test="${loggedUser.isClinic() eq true && loggedUser.isVerifying() eq false}">
                                    <a href="${uploadPath}" class="btn upload-btn mt-0 mb-3 mr-4" role="button"><spring:message code="view-study.body.card.order.button.uploadResults"/></a>
                                </c:if>

                            </div>

                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${results.size() < 1}">
                            <div class="align-items-end result-not">
                                <h1 class="text-center mt-5 py-5"><spring:message code="view-study.body.card.results.noResults"/></h1>
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
                                                        <p class="type-title"><spring:message code="view-study.body.card.results.result.date.label" arguments="${result.getLegacyDate()}"/>
                                                    </div>
                                                    <p><c:out value="${result.responsibleName}"/></p>
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
                                                            <div class="col type"><p><spring:message code="view-study.body.card.results.result.date.label" arguments="${result.getLegacyDate()}"/></p></div>
                                                            <div class="col type"><p><spring:message code="view-study.body.card.results.result.clinic.label" arguments="${order.clinic.name}"/></p></div>
                                                        </div>
                                                        <hr class="mt-3 mb-4 text-center"/>
                                                        <img src="${imageAssets}/result/${encodedId}/${result.id}?attr=result-data" class="align-self-end ml-3" alt="<spring:message code="view-study.body.card.result.result.alt"/>" style="max-height:14em; max-width:20em">
                                                        <hr class="mt-5 mb-4"/>
                                                        <div class="media">
                                                            <div class="media-body">
                                                                <h5 class="mt-0 mb-1 text-center"><c:out value="${result.responsibleName}"/></h5>
                                                                <p class="text-center"><spring:message code="view-study.body.card.results.result.responsibleLicenceNumber.prefix" arguments="${result.responsibleLicenceNumber}"/></p>
                                                            </div>
                                                            <img src="${imageAssets}/result/${encodedId}/${result.id}?attr=identification" class="ml-1" alt="<spring:message code="view-study.body.card.result.signature.alt"/>" style="max-height: 5em;">
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
    <div id="snackbar"></div>
</div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>
