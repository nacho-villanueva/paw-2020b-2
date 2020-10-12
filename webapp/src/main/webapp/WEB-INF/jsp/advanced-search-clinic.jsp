<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="<c:url value="/resources/css/advanced-search-clinic.css"/>">
    <%@ include file="fragments/include-header.jsp"%>

    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
</head>
<body>

<!-- Page Wrapper -->
<div id="wrapper">

    <jsp:include page="fragments/sidebar-fragment.jsp" >
        <jsp:param name="current" value="orders"/>
    </jsp:include>
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <div class="row justify-content-center">
            <div class="col-sm-5">
                <div class="card filters-card bg-light anim-content float-right">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty orderForm}"><c:url var="formSubmitUrl" value="/create-order"/></c:when>
                            <c:otherwise><c:url var="formSubmitUrl" value="/advanced-search/clinic"/></c:otherwise>
                        </c:choose>
                        <f:form acceptCharset="utf-8" action="${formSubmitUrl}" method="get" modelAttribute="advancedSearchClinicForm">
                            <p class="card-title h4"><spring:message code="advanced-search-clinic.body.title"/></p>
                            <hr/>

                            <c:if test="${not empty orderForm}">
                                <f:input path="medicId" type="hidden" id="medicId"/>
                                <f:input path="studyId" type="hidden" id="studyId"/>
                                <f:input path="description" type="hidden" id="description"/>
                                <f:input path="patient_insurance_plan" type="hidden" id="patient_insurance_plan"/>
                                <f:input path="patient_insurance_number" type="hidden" id="patient_insurance_number"/>
                                <f:input path="patientEmail" type="hidden" id="patientEmail"/>
                                <f:input path="patientName" type="hidden" id="studyId"/>
                            </c:if>

                            <fieldset class="form-group">
                                <label class="bmd-label-floating" for="clinic_name"><spring:message code="advanced-search-clinic.form.clinic_name.label"/></label>
                                <div class="input-group">
                                    <f:input type="text" class="form-control" id="clinic_name" path="clinic_name"/>
                                    <f:errors path="clinic_name"/>
                                </div>
                            </fieldset>

                            <fieldset class="form-group">
                                <label class="bmd-label-floating" for="medical_study"><spring:message code="advanced-search-clinic.form.medical_study.label"/></label>
                                <div class="input-group">
                                    <f:input type="text" class="form-control" id="medical_study" path="medical_study"/>
                                    <f:errors path="medical_study"/>
                                </div>
                            </fieldset>

                            <fieldset class="form-group">
                                <label class="bmd-label-floating" for="medical_plan"><spring:message code="advanced-search-clinic.form.medical_plan.label"/></label>
                                <div class="input-group">
                                    <f:input type="text" class="form-control" id="medical_plan" path="medical_plan"/>
                                    <f:errors path="medical_plan"/>
                                </div>
                            </fieldset>

                            <fieldset>
                                <spring:message code="advanced-search-clinic.form.openTime.placeholder" var="placeholderOT"/>
                                <spring:message code="advanced-search-clinic.form.closeTime.placeholder" var="placeholderCT"/>

                                <a class="btn btn-outline" data-toggle="collapse" href="#collapseHours" role="button" aria-expanded="false" aria-controls="collapseHours">
                                    <spring:message code="advanced-search-clinic.form.available_hours.label"/>
                                </a>

                                <div class="collapse" id="collapseHours">
                                    <table class="table table-scrollable">
                                        <thead>
                                        <tr>
                                            <th></th>
                                            <th><spring:message code="advanced-search-clinic.form.opens.label"/></th>
                                            <th><spring:message code="advanced-search-clinic.form.opening_time.label"/></th>
                                            <th><spring:message code="advanced-search-clinic.form.closing_time.label"/></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <th><spring:message code="advanced-search-clinic.form.day.sunday"/> </th>
                                            <th><f:checkbox id="sunday" path="sundayOpens"/></th>
                                            <th><f:input id="sundayOT" type="text" class="form-control" placeholder="${placeholderOT}" path="sundayOpenTime" cssClass="time-input"/></th>
                                            <th><f:input id="sundayCT" type="text" class="form-control" placeholder="${placeholderCT}" path="sundayCloseTime" cssClass="time-input"/></th>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="advanced-search-clinic.form.day.monday"/> </th>
                                            <th><f:checkbox id="monday" path="mondayOpens"/></th>
                                            <th><f:input id="mondayOT" type="text" class="form-control" placeholder="${placeholderOT}" path="mondayOpenTime" cssClass="time-input"/></th>
                                            <th><f:input id="mondayCT" type="text" class="form-control" placeholder="${placeholderCT}" path="mondayCloseTime" cssClass="time-input"/></th>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="advanced-search-clinic.form.day.tuesday"/> </th>
                                            <th><f:checkbox id="tuesday" path="tuesdayOpens"/></th>
                                            <th><f:input id="tuesdayOT" type="text" class="form-control" placeholder="${placeholderOT}" path="tuesdayOpenTime" cssClass="time-input"/></th>
                                            <th><f:input id="tuesdayCT" type="text" class="form-control" placeholder="${placeholderCT}" path="tuesdayCloseTime" cssClass="time-input"/></th>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="advanced-search-clinic.form.day.wednesday"/> </th>
                                            <th><f:checkbox id="wednesday" path="wednesdayOpens"/></th>
                                            <th><f:input id="wednesdayOT" type="text" class="form-control" placeholder="${placeholderOT}" path="wednesdayOpenTime" cssClass="time-input"/></th>
                                            <th><f:input id="wednesdayCT" type="text" class="form-control" placeholder="${placeholderCT}" path="wednesdayCloseTime" cssClass="time-input"/></th>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="advanced-search-clinic.form.day.thursday"/> </th>
                                            <th><f:checkbox id="thursday" path="thursdayOpens"/></th>
                                            <th><f:input id="thursdayOT" type="text" class="form-control" placeholder="${placeholderOT}" path="thursdayOpenTime" cssClass="time-input"/></th>
                                            <th><f:input id="thursdayCT" type="text" class="form-control" placeholder="${placeholderCT}" path="thursdayCloseTime" cssClass="time-input"/></th>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="advanced-search-clinic.form.day.friday"/> </th>
                                            <th><f:checkbox id="friday" path="fridayOpens"/></th>
                                            <th><f:input id="fridayOT" type="text" class="form-control" placeholder="${placeholderOT}" path="fridayOpenTime" cssClass="time-input"/></th>
                                            <th><f:input id="fridayCT" type="text" class="form-control" placeholder="${placeholderCT}" path="fridayCloseTime" cssClass="time-input"/></th>
                                        </tr>
                                        <tr>
                                            <th><spring:message code="advanced-search-clinic.form.day.saturday"/> </th>
                                            <th><f:checkbox id="saturday" path="saturdayOpens"/></th>
                                            <th><f:input id="saturdayOT" type="text" class="form-control" placeholder="${placeholderOT}" path="saturdayOpenTime" cssClass="time-input"/></th>
                                            <th><f:input id="saturdayCT" type="text" class="form-control" placeholder="${placeholderCT}" path="saturdayCloseTime" cssClass="time-input"/></th>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </fieldset>

                            <div class="row justify-content-center">
                                <f:button type="submit" name="submit" value="search" id="searchButton" class="row btn btn-lg action-btn">
                                    <spring:message code="advanced-search-clinic.form.button.search"/>
                                </f:button>
                            </div>
                            <div class="row justify-content-center">
                                <f:button type="submit" name="submit" value="reset" id="resetButton" class="row btn btn-outline-secondary">
                                    <spring:message code="advanced-search-clinic.form.button.resetFilters"/>
                                </f:button>
                            </div>
                            <c:if test="${errorAlert}"><div class="alert alert-danger" role="alert"><spring:message code="advanced-search-clinic.form.errorAlert"/></div></c:if>

                        </f:form>
                    </div>
                </div>
            </div>
            <div class="col-sm-7">
                <div class="card results-card bg-light float-left">
                    <div class="card-body">
                        <p class="card-title h4"><spring:message code="advanced-search-clinic.results.title"/></p>
                        <hr/>
                        <h4><spring:message code="advanced-search-clinic.list.title"/> </h4>
                        <div class="d-flex flex-row">
                            <div id="results" class="list-group result-section" style="overflow-y: scroll; overflow-x: hidden; height: 90%;">
                                <c:if test="${clinicsList.size() eq 0}">
                                    <h3 class="text-center py-5 lead"><spring:message code="advanced-search.clinic.results.noResults"/></h3>
                                </c:if>
                                <ul class="nav flex-column" id="myTab" role="tablist">
                                    <c:forEach items="${clinicsList}" var="clinic">
                                        <li class="nav-item">
                                            <a id="${clinic.user_id}" onclick="selectClinic(this);return false;" class="list-group-item list-group-item-action" data-toggle="tab" href="#clinic_${clinic.user_id}" role="tab" aria-controls="clinic_${clinic.user_id}" aria-selected="false">
                                                <div class="justify-content-between">
                                                    <h5 class="mb-1"><c:out value="${clinic.name}"/></h5>
                                                    <small><spring:message code="advanced-search-clinic.results.email" arguments="${clinic.email}"/></small>
                                                </div>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                            <div id="data" class="data-section">

                                <h4><spring:message code="create-order.body.form.clinic.title"/> </h4>

                                <div id="noClinic">
                                    <h5><spring:message code="advanced-search.clinic.selectedClinic.noClinic"/> </h5>
                                </div>

                                <div id="clinicSelected"class="tab-content">
                                    <c:forEach items="${clinicsList}" var="clinic">
                                        <div id="clinic_${clinic.user_id}" class="tab-pane fade tab-result">
                                            <h5><c:out value="${clinic.name}"/></h5>
                                            <table class="table table-borderless">
                                                <tbody>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.clinic.telephone.label"/></td>
                                                    <td class="output"><c:out value="${clinic.telephone}"/></td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.clinic.medical_studies.label"/></td>
                                                    <td class="output"><c:forEach items="${clinic.medical_studies}" var="medicalStudy"><p><c:out value="${medicalStudy.name}"/></p></c:forEach></td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="profile-view.body.tab.clinic.verified.label"/></td>
                                                    <td class="output"><i class="fas <c:choose><c:when test="${clinic.verified}">fa-check</c:when><c:otherwise>fa-times</c:otherwise></c:choose> fa-lg"></i></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:forEach>
                                </div>


                                <c:if test="${not empty orderForm}">
                                    <f:form action="${formSubmitUrl}" method="post" modelAttribute="orderForm">

                                        <f:input path="medicId" type="hidden" id="medicId"/>
                                        <f:input path="clinicId" type="hidden" id="clinicId"/>
                                        <f:input path="studyId" type="hidden" id="studyId"/>
                                        <f:input path="description" type="hidden" id="description"/>
                                        <f:input path="patient_insurance_plan" type="hidden" id="patient_insurance_plan"/>
                                        <f:input path="patient_insurance_number" type="hidden" id="patient_insurance_number"/>
                                        <f:input path="patientEmail" type="hidden" id="patientEmail"/>
                                        <f:input path="patientName" type="hidden" id="studyId"/>

                                        <div class="row justify-content-center">
                                            <f:button class="btn create-btn mt-4 mb-2 float-right" type="submit" name="submit" value="create-order"><spring:message code="create-order.body.form.button.submit"/></f:button>
                                        </div>
                                        <div class="row justify-content-center">
                                            <f:button type="submit" name="submit" formmethod="get" value="back" id="backButton" class="row btn btn-outline-secondary">
                                                <spring:message code="create-order.body.form.button.back"/>
                                            </f:button>
                                        </div>
                                        <c:if test="${clinicUnselected}"><div class="alert alert-danger" role="alert"><spring:message code="profile-edit.form.clinic.clinicUnselected"/></div></c:if>
                                    </f:form>
                                </c:if>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<%@ include file="fragments/include-scripts.jsp"%>
<script type="text/javascript">
    function selectClinic(listItem) {

        document.getElementById("noClinic").style.display = "none";

        let clinic = document.getElementById("clinicId");
        if(clinic !== null){
            clinic.setAttribute("value",listItem.id);
        }


    }
</script>

</body>
</html>
