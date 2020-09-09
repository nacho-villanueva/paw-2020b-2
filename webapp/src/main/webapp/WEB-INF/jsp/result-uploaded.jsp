<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>study uploaded</title>
</head>
<body>
<h2>Check uploaded files</h2>
<table>
    <c:forEach items="${files}" var="file">
        <tr>
            <td>OriginalFileName:</td>
            <td>${file.originalFilename}</td>
        </tr>
        <tr>
            <td>Type:</td>
            <td>${file.contentType}</td>
        </tr>
    </c:forEach>
    <tr>
        <td>Sign file:</td>
        <td>${sign.originalFilename}</td>
    </tr>
    <tr>
        <td>Type:</td>
        <td>${sign.contentType}</td>
    </tr>
</table>
</body>
</html>
