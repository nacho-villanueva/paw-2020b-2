<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%@ include file="fragments/include-header.jsp"%>

    <link rel="stylesheet" href="<c:url value="/resources/css/createorder.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar-alternative.css"/>">
</head>
<body>

<%@include file="fragments/navbar-alternative-fragment.jsp"%>
<div id="wrapper" class="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp">
        <jsp:param name="current" value="create-order"/>
    </jsp:include>

<div id="content-wrapper" class="d-flex flex-column main-container">
    <div class="row justify-content-center">
        <div class="card" style="width: 40em; margin-top: 2em; margin-bottom: 7rem;">
            <div class="card-body">
                <div class="row">
                    <p class="card-title h4 mx-auto mt-3"><spring:message code="create-order.body.form.title"/> </p>
                </div>

                <c:url var="post_createorder"  value="/create-order"/>
                <f:form action="${post_createorder}" method="post" modelAttribute="orderForm">
                    <div class="border-1 border-secondary mx-2 mt-3">
                        <label for="medicName" class="text-muted"><spring:message code="create-order.body.form.medicName.label"/> </label>
                        <p id="medicName" class="lead"><c:out value="${loggedMedic.name}"/> </p>
                        <f:input type="hidden" path="medicId"/>
                    </div>
                    <hr class="divider"/>
                    <div class="row mx-1">
                        <fieldset class="form-group col">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientMail.label"/> </label>
                            <f:input type="email" path="patientEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                            <f:errors path="patientEmail" cssClass="text-danger" element="small" />
                        </fieldset>
                    </div>
                    <div class="row mx-1">
                        <fieldset class="form-group col">
                            <button class="btn-block btn create-btn mt-4 mb-2 float-right" type="submit" name="submit" value="getExistingPatient"><spring:message code="create-order.body.form.existingPatient.label"/></button>
                        </fieldset>
                    </div>
                    <div class="row mx-1">
                        <fieldset class="form-group col">
                            <small><spring:message code="create-order.body.form.existingPatient.small"/></small>
                        </fieldset>
                    </div>
                    <div class="row mx-1">
                        <fieldset class="form-group col">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientName.label"/> </label>
                            <f:input type="text" path="patientName" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <f:errors path="patientName" cssClass="text-danger" element="small" />
                        </fieldset>
                    </div>
                    <div class="row mx-1">
                        <fieldset class="form-group col ">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientInsurancePlan.label"/></label>
                            <f:input type="text" path="patientInsurancePlan" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <f:errors path="patientInsurancePlan" cssClass="text-danger" element="small" />
                        </fieldset>
                        <fieldset class="form-group col">
                            <label class="bmd-label-floating"><spring:message code="create-order.body.form.patientInsuranceNumber.label"/> </label>
                            <f:input type="text" path="patientInsuranceNumber" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <f:errors path="patientInsuranceNumber" cssClass="text-danger" element="small" />
                        </fieldset>

                    </div>

                    <hr class="mt-3 mb-2"/>

                    <div class="row mx-1">
                        <div class="col">
                            <fieldset class="form-group">
                                <label class="bmd-label-static"><spring:message code="create-order.body.form.studyId.label"/></label>
                                <spring:message code="create-order.body.form.studyId.placeholder" var="studyIdPlaceholder"/>
                                <f:select title="${studyIdPlaceholder}" cssErrorClass="selectpicker is-invalid" id="medicalStudy" cssClass="selectpicker" data-live-search="true" path="studyId" data-style="text-primary">
                                    <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                                </f:select>
                                <f:errors path="studyId" cssClass="text-danger" element="small" />
                            </fieldset>
                        </div>

                    </div>
                    <div class="row form-group" style="margin-left: 1.25rem; margin-right:1.25rem;">
                        <label class="bmd-label-static"><spring:message code="create-order.body.form.description.label"/></label>
                        <f:textarea path="description" cssStyle="resize: none" cssClass="form-control" cssErrorClass="form-control is-invalid" rows="10"/>
                        <f:errors path="description" cssClass="text-danger" element="small" />
                    </div>


                    <hr class="mt-3 mb-2"/>

                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button"><spring:message code="create-order.body.form.button.cancel"/></a>
                    <button class="btn create-btn mt-4 mb-2 float-right" type="submit" name="submit" value="toClinicSelection"><spring:message code="create-order.body.form.button.next"/></button>

                    <datalist id="studiesList">
                        <c:forEach var = "i" items="${studiesList}">
                        <option value="<c:out value = "${i}"/>">
                            </c:forEach>
                    </datalist>
                </f:form>
            </div>
        </div>
    </div>
</div>
</div>>

<%@ include file="fragments/include-scripts.jsp"%>
</body>
</html>