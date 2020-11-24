<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <!-- bootstrap-select CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="<c:url value="/resources/css/profile.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">


</head>
<body>
<c:url value="/api/image" var="imageAssets"/>
<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<script src="<c:url value="/resources/js/addOption.js" />"></script>
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha384-ZvpUoO/+PpLXR1lu4jmpXWu80pZlYUAfxl5NsBMWOEPSjUn/6Z/hRTt8+pR6L4N2" crossorigin="anonymous"></script>
<spring:message code="create-type-or-field.body.form.successMessage" var="successMessage"/>
<spring:message code="create-type-or-field.body.form.existsMessage" var="existsMessage"/>
<spring:message code="create-type-or-field.body.form.errorMessage" var="errorMessage"/>
<script type="text/javascript">
    function addOptionValue(optionValue,mySelectId,alerts) {

        let newOptionValue = document.getElementById(optionValue).value.trim();

        let alert = '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
            '${errorMessage}'.replace("{0}",newOptionValue) +
            '  <button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
            '    <span aria-hidden="true">&times;</span>' +
            '  </button>' +
            '</div>';


        if(!!newOptionValue){

            let options = ($(mySelectId).find("option"));
            let optionText=[];

            for(let i=0; i< options.length; i++){
                let option = options[i].value;
                if(option !== ""){
                    optionText.push(option.toLowerCase());
                }
            }

            if(!optionText.includes(newOptionValue.toLowerCase())){
                addOption(mySelectId,newOptionValue);
                alert = '<div class="alert alert-success alert-dismissible fade show" role="alert">' +
                    '${successMessage}'.replace("{0}",newOptionValue) +
                    '  <button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                    '    <span aria-hidden="true">&times;</span>' +
                    '  </button>' +
                    '</div>';
                $(mySelectId).selectpicker("refresh");
            }else{
                alert = '<div class="alert alert-warning alert-dismissible fade show" role="alert">' +
                    '${existsMessage}'.replace("{0}",newOptionValue) +
                    '  <button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                    '    <span aria-hidden="true">&times;</span>' +
                    '  </button>' +
                    '</div>';
            }
        }

        document.getElementById(optionValue).value = "";
        let alertBox = $(alerts);

        alertBox.html(alert);
    }
</script>
<div id="wrapper" class="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="profile"/>
    </jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">

        <div class="main-container">
            <div class="row justify-content-center" style="padding-bottom: 3rem !important;margin-bottom: 3rem !important;">
                <div class="card" style="width: 40em; margin-top: 2em;">
                    <div class="card-body">
                        <div class="row">
                            <p class="card-title h4 mx-auto mt-3"><spring:message code="profile-edit.body.title"/> </p>
                        </div>
                        <hr class="divider my-4">

                        <ul class="nav nav-pills nav-justified" id="myTab">
                            <li class="nav-item">
                                <a class="nav-link <c:if test="${role == 'user'}">active</c:if>"
                                   id="user-tab" href="<c:url value="/profile/edit/user/email"/>"
                                   aria-controls="user" aria-selected="<c:choose><c:when test="${role == 'user'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.user"/> </a>
                            </li>

                            <c:choose>
                                <c:when test="${not empty patient}">
                                    <li class="nav-item">
                                        <a class="nav-link <c:if test="${role == 'patient'}">active</c:if>"
                                           id="patient-tab" href="<c:url value="/profile/edit/patient"/>"
                                           aria-controls="patient" aria-selected="<c:choose><c:when test="${role == 'patient'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.patient"/> </a>
                                    </li>
                                </c:when>

                                <c:when test="${not empty medic}">
                                    <li class="nav-item">
                                        <a class="nav-link <c:if test="${role == 'medic'}">active</c:if>"
                                           id="medic-tab" href="<c:url value="/profile/edit/medic"/>"
                                           aria-controls="medic" aria-selected="<c:choose><c:when test="${role == 'medic'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.medic"/> </a>
                                    </li>
                                </c:when>

                                <c:when test="${not empty clinic}">
                                    <li class="nav-item">
                                        <a class="nav-link <c:if test="${role == 'clinic'}">active</c:if>"
                                           id="clinic-tab" href="<c:url value="/profile/edit/clinic"/>"
                                           aria-controls="clinic" aria-selected="<c:choose><c:when test="${role == 'clinic'}">true</c:when><c:otherwise>false</c:otherwise></c:choose>"><spring:message code="profile-view.body.pill.clinic"/> </a>
                                    </li>
                                </c:when>

                                <c:otherwise/>

                            </c:choose>
                        </ul>

                        <hr class="divider my-4">

                        <c:choose>
                            <c:when test="${role == 'user'}">
                                <c:choose>
                                    <c:when test="${not empty editUserEmailForm}">
                                        <jsp:include page="fragments/edit-user-form-email-fragment.jsp"/>
                                    </c:when>
                                    <c:when test="${not empty editUserPasswordForm}">
                                        <jsp:include page="fragments/edit-user-form-password-fragment.jsp"/>
                                    </c:when>
                                    <c:otherwise/>
                                </c:choose>
                            </c:when>
                            <c:when test="${role == 'patient'}">
                                <jsp:include page="fragments/edit-patient-form-fragment.jsp"/>
                            </c:when>
                            <c:when test="${role == 'medic'}">
                                <jsp:include page="fragments/edit-medic-form-fragment.jsp"/>
                            </c:when>
                            <c:when test="${role == 'clinic'}">
                                <jsp:include page="fragments/edit-clinic-form-fragment.jsp"/>
                            </c:when>
                        </c:choose>

                        <div class="row justify-content-center">
                            <c:if test="${not empty errorAlert}"><div class="alert alert-danger" role="alert"><c:out value="${errorAlert}"/></div></c:if>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<%@ include file="fragments/include-scripts.jsp"%>
<c:if test="${not empty clinic}">
    <script>
        const strings = {
            "openTime":"<spring:message code='openDaysPicker.openTime' javaScriptEscape='true' />",
            "closeTime":"<spring:message code='openDaysPicker.closeTime' javaScriptEscape='true' />"

        };

    </script>

    <script src="<c:url value="/resources/js/PlansAddList.js" />"></script>
    <script src="<c:url value="/resources/js/OpenDaysPicker.js" />"></script>
    <script>$("#editClinic").submit(beforeSubmit);</script>
</c:if>
<!-- bootstrap-select JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js" integrity="sha384-SfMwgGnc3UiUUZF50PsPetXLqH2HSl/FmkMW/Ja3N2WaJ/fHLbCHPUsXzzrM6aet" crossorigin="anonymous"></script>

</body>
</html>
