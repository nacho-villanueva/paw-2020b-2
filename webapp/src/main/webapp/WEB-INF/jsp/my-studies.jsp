<%--
  Created by IntelliJ IDEA.
  User: matia
  Date: 05-Oct-20
  Time: 12:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="fragments/include-header.jsp"%>

    <!-- bootstrap-select CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">

    <link rel="stylesheet" href="<c:url value="/resources/css/registration.css"/>">

    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/mystudies.css"/>">
    <title>MedTransfer</title>
</head>
<c:url value="/view-study/" var="studyPath"/>
<body>
<div class="main-container">
    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
    <div class="row justify-content-center">
        <div class="col-sm-7">
            <div class="card results-card bg-light anim-content float-right">
                <div class="card-body">
                    <p class="card-title h4">Results</p>
                    <div id="results" class="list-group" style="overflow-y: scroll; overflow-x: hidden; height: 90%;">
                        <c:if test="${ordersList.size() eq 0}">
                            <h3 class="text-center py-5 lead">It seems there are no studies matching with the filter criteria.</h3>
                        </c:if>
                        <c:forEach items="${ordersList}" var="order">
                            <a href="${studyPath}${encodedList.get(order.order_id)}" class="list-group-item list-group-item-action">
                                <div class="d-flex w-100 justify-content-between">
                                    <h5 class="mb-1">Study type: <c:out value="${order.study.name}" /></h5>
                                    <small>Date: <c:out value="${order.date}" /></small>
                                </div>
                                <div class="d-flex w-100 justify-content-between">
                                    <p class="mb-1">Clinic: <c:out value="${order.clinic.name}" /></p>
                                    <small>Medic: <c:out value="${order.medic.name}" /></small>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-5">
            <div class="card filters-card bg-light float-left">
                <div class="card-body">
                    <p class="card-title h4">Filters</p>
                    <form>
                        <fieldset class="row form-group">
                            <label class="bmd-label-floating" for="clinic">Medical Clinic </label>
                            <select id="clinic" cssClass="selectpicker" cssErrorClass="selectpicker is-invalid" data-live-search="true" path="clinicId" disabled="true" data-style="btn-primary">
                                <option value="-1" label="Any medical clinic"></option>
                                <options items="${clinicsList}" itemLabel="name" itemValue="id"/>
                            </select>
                            <errors path="clinicId" cssClass="invalid-feedback" element="small" />
                        </fieldset>
                        <fieldset class="row form-group">
                            <label class="bmd-label-floating" for="medicalStudy">Study Type </label>
                            <select cssErrorClass="selectpicker form-control is-invalid" id="medicalStudy" cssClass="selectpicker form-control" data-live-search="true" path="studyId" data-style="btn-primary">
                                <option value="-1" label="Any medical study"></option>
                                <options items="${studiesList}" itemLabel="name" itemValue="id"/>
                            </select>
                            <errors path="studyId" cssClass="invalid-feedback" element="small" />
                        </fieldset>
                        <fieldset class="row form-group">
                            <label class="bmd-label-floating" for="medic">Medic </label>
                            <select cssErrorClass="selectpicker form-control is-invalid" id="medic" cssClass="selectpicker form-control" data-live-search="true" path="medicId" data-style="btn-primary">
                                <option value="-1" label="Any medic"></option>
                                <options items="${medicsList}" itemLabel="name" itemValue="id"/>
                            </select>
                            <errors path="medicId" cssClass="invalid-feedback" element="small" />
                        </fieldset>
                        <fieldset class="row form-group">
                            <label class="bmd-label-floating" for="patient">Patient </label>
                            <div class="input-group mb-3">
                                <input type="text" class="form-control" id="patient">
                            </div>
                        </fieldset>
                        <fieldset class="row form-group">
                            <label class="bmd-label-floating" for="date">Date</label>
                            <div class="input-group date">
                                <input type="text" class="form-control" id="date"><span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
                            </div>
                        </fieldset>
                        <div class="row justify-content-center">
                            <input type="submit" id="search" class="row btn btn-lg action-btn" value="Search">
                        </div>

                    </form>
                </div>
            </div>
        </div>


    </div>
</div>
<%@ include file="fragments/include-scripts.jsp"%>
<!-- bootstrap-select JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js" integrity="sha384-SfMwgGnc3UiUUZF50PsPetXLqH2HSl/FmkMW/Ja3N2WaJ/fHLbCHPUsXzzrM6aet" crossorigin="anonymous"></script>

</body>
<!-- Query: Get Orders from filters -->
<c:url value="/my-studies" var="filterLink"/>
<script type="text/javascript">
    $(document).ready(function(){

        let resultsContainer = $('#results');
        $('#search').on('click', function (){

            let clinicId = $('#clinic').val();
            let medicId = $('#medic').val();
            let patientId = $('#patient').val();
            let studyId = $('#medicalStudy').val();
            let date = $('#date').val();
            if(clinicId >= -1 && medicId >= -1 && patientId >= -1 && studyId >= -1) {
                //let out = '?';
                //if(!date.isEmpty())
            }
    });

    // temporal string sanitizer, from https://stackoverflow.com/questions/2794137/sanitizing-user-input-before-adding-it-to-the-dom-in-javascript
    // based on https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html
    function sanitize(string) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#x27;',
            "/": '&#x2F;',
        };
        const reg = /[&<>"'/]/ig;
        return string.replace(reg, (match)=>(map[match]));
    };

    //we need to check how the date picker writes out dates (and if its necessary to parse them so that
        // the query can be understandable by SQL)
    function filterCleaner(clinicId, medicId, patientId, studyId, date) {
        let out;
        if(!date.isEmpty()){
            out.date = date;
        }
        if(clinicId != -1){
            out.clinic = clinicId;
        }
        if(medicId != -1){
            out.medic = medicId;
        }
        if(studyId != -1){
            out.study = studyId;
        }
        if(patientId != -1){
            out.patient = patientId;
        }
        return out;

    }
</script>
</html>