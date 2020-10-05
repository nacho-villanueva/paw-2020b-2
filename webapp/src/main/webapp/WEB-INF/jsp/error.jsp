<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/errorpage.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">

</head>
<body>
<c:url value="/" var="indexPath"/>
<c:set value="${empty errorNumber?500:errorNumber}" var="errorNumber"/>
<c:set value="${empty errorText?'Internal Error':errorText}" var="errorText"/>
<%@include file="fragments/navbar-fragment.jsp"%>
<div class="main-container">
    <div class="text-center">
        <div class="error mx-auto" data-text="<c:out value="${errorNumber}"/>"><c:out value="${errorNumber}"/></div>
        <p class="lead text-gray-800 mb-5"><c:out value="${errorText}"/></p>
        
        <a href="${indexPath}">&larr; Back to Safety</a>
    </div>
</div>>
</body>
</html>
