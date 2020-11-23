<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">

<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/access-requests.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">
    <title><spring:message code="appname"/></title>
</head>

<body id="page-top">

<c:url value="/create-order" var="createPath"/>
<c:url value="/view-study/" var="studyPath"/>
<c:url value="/logout" var="logoutPath"/>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
    <!-- Page Wrapper -->
    <div id="wrapper" class="wrapper">

        <jsp:include page="fragments/sidebar-fragment.jsp" >
            <jsp:param name="current" value="access-requests"/>
        </jsp:include>
        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column main-container">
            <div class="row justify-content-center">
                <c:choose>
                    <c:when test="${requestsList.size() <= 0}">
                        <div class="align-items-end result-not">
                            <h1 class="text-center mt-5 py-5 anim-content"><spring:message code="access-requests.body.noRequests"/></h1>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card requests-card">
                            <div class="card-body">
                                <p class="card-title h4"><spring:message code="access-requests.body.card.requests.title"/></p>
                                <div class="list-group">
                                    <c:forEach items="${requestsList}" var="o">
                                        <a class="list-group-item list-group-item-action">
                                            <div class="row" style="width: 100%;">
                                                <div class="col float-left">
                                                    <h6 class="mb-1"><spring:message code="access-requests.body.card.requests.studyType" arguments="${o.studyType.name}" /></h6>
                                                    <br/>
                                                    <small><spring:message code="access-requests.body.card.requests.medic" arguments="${o.medic.name}" /></small>
                                                </div>
                                                <div class="col" style="margin-right: 0; float: right;">
                                                    <button class="row btn btn-lg action-btn" style="float:right; margin-right: 0;"
                                                            type="submit" name="submit" id="access-request" value="accept"
                                                    >
                                                        <spring:message code="access-requests.body.form.button.preview"/>
                                                    </button>
                                                </div>
                                            </div>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </div>

</body>
</html>
