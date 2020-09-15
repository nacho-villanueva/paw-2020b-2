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
<%@include file="fragments/navbar-fragment.jsp"%>
<div class="main-container">
    <div class="text-center">
        <div class="error mx-auto" data-text="404">404</div>
        <p class="lead text-gray-800 mb-5">Page Not Found</p>
        <p class="text-gray-500 mb-0">It looks like you found a glitch in the matrix...</p>
        <a href="${indexPath}">&larr; Back to Safety</a>
    </div>
</div>>
</body>
</html>
