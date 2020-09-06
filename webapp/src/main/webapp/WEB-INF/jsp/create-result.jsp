<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>create result</title>
</head>
<body>
<form:form method="POST" action="/result-uploaded" enctype="multipart/form-data">
    <table>
        <h2>Create Result</h2>
        <tr>
            <td>
                <label>Select files to upload</label>
            </td>
            <td><input multiple required type="file" name="files"/></td>
        </tr>
        <tr>
            <td>
                <label>Select a sign file to upload</label>
            </td>
            <td><input required type="file" name="sign"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>