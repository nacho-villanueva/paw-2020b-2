<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">

    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">


    <%@ include file="fragments/include-scripts.jsp"%>
    <!-- Bootstrap JS Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>


    <!-- Query: Get Clinic from study id -->
    <c:url var="getClinicByMedicalStudy" value="/api/data/clinic/get-clinics-by-medical-study"/>
    <script type="text/javascript">
        $(document).ready(function(){

            $('#medicalStudy').on('change', function(){
                let studyId = $(this).val();
                let clinicSelect = $('#clinic');

                if(studyId>=0){
                    $.getJSON('${getClinicByMedicalStudy}',{
                        study : studyId
                    }, function(response) {
                        let clinicList = '<option value="-1">Choose Clinic</option>';
                        let clinicLen = response.length;
                        for(let i =0; i<clinicLen;i++){
                            clinicList += '<option value="' + response[i].user_id + '">' + sanitize(response[i].name) + '</option>';
                        }
                        clinicList += '</option>';

                        clinicSelect.html(clinicList);
                        clinicSelect.attr('disabled',false);
                        clinicSelect.selectpicker('refresh');
                    });
                }else{
                    clinicSelect.html('<option value="-1">Choose Study first</option>');
                    clinicSelect.attr('disabled',true);
                    clinicSelect.selectpicker('refresh');
                }
            });
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
        }
    </script>

    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/createorder.css"/>">
    <title>Create Order</title>
</head>
<body>

<div class="main-container">
    <%@include file="fragments/navbar-alternative-fragment.jsp"%>
    <div class="row justify-content-center">
        <div class="card" style="width: 40em; max-height: 50em; margin-top: 2em;">
            <div class="card-body">
                <div class="row">
                    <p class="card-title h4 mx-auto mt-3">Create New Medical Order</p>
                </div>

                <c:url var="post_createorder"  value="/create-order"/>
                <f:form action="${post_createorder}" method="post" modelAttribute="orderForm" enctype="multipart/form-data">

                    <div class="row">
                        <div class="col">
                            <label>Medic</label>
                            <div>
                                <input type="text" disabled placeholder="<c:out value="${loggedMedic.name}"/>"/>
                            </div>
                        </div>
                        <div class="col">
                            <label>Study Type </label>
                            <f:select id="medicalStudy" cssClass="selectpicker" data-live-search="true" path="studyId" data-style="btn-primary">
                                <f:option value="-1" label="Choose Study"/>
                                <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                            </f:select>
                            <f:errors path="studyId" cssClass="error" />
                        </div>
                    </div>
                    <hr class="mt-3 mb-2"/>

                    <div class="row justify-content-center">
                        <div class="col">
                            <label>Patient's name</label>
                            <div class="input-group mb-3">
                                <f:input type="text" path="patientName" required="required"/>
                                <f:errors path="patientName" cssClass="error" />
                            </div>
                            <label>Patient's insurance plan</label>
                            <div class="input-group mb-3">
                                <f:input type="text" path="patient_insurance_plan" required="required"/>
                                <f:errors path="patient_insurance_plan" cssClass="error" />
                            </div>
                        </div>
                        <div class="col">
                            <label>Patient's email</label>
                            <div class="input-group mb-3">
                                <f:input type="email" path="patientEmail" required="required"/>
                                <f:errors path="patientEmail" cssClass="error" />
                            </div>
                            <label>Patient's insurance number</label>
                            <div class="input-group mb-3">
                                <f:input type="text" path="patient_insurance_number" required="required"/>
                                <f:errors path="patient_insurance_number" cssClass="error" />
                            </div>

                        </div>
                    </div>

                    <hr class="mt-3 mb-2"/>

                    <div class="row">
                        <div class="col">
                            <label>Medical Clinic </label>
                            <f:select id="clinic" cssClass="selectpicker" data-live-search="true" path="clinicId" disabled="true" data-style="btn-primary">
                                <f:option value="-1" label="Choose Study first"/>
                            </f:select>
                            <f:errors path="clinicId" cssClass="error" /><br>
                        </div>
                        <div class="col">
                            <label class="my-2"> Order Description <f:textarea rows="4" cols="50" path="description" class="mt-1"/> </label>
                            <f:errors path="description" cssClass="error" />
                        </div>
                    </div>

                    <a onclick="history.back(-1)" class="btn btn-secondary mt-4 mb-2 float-left" role="button">Cancel</a>
                    <button class="btn create-btn mt-4 mb-2 float-right" type="submit">Create Order</button>

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

</body>
</html>