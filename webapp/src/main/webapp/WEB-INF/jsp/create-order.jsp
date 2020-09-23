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

    <!-- Bootstrap JS Scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha384-ZvpUoO/+PpLXR1lu4jmpXWu80pZlYUAfxl5NsBMWOEPSjUn/6Z/hRTt8+pR6L4N2" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
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
                            clinicList += '<option value="' + response[i].user_id + '">' + response[i].name + '</option>';
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
    </script>

    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/createorder.css"/>">
    <title>Create Order</title>
</head>
<body>

<div class="main-container">
    <%@include file="fragments/navbar-fragment.jsp"%>
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
                                <input type="text" disabled placeholder="${loggedMedic.name}"/>
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
                                <f:option value="-1">Choose Study first</f:option>
                            </f:select>
                            <f:errors path="clinicId" cssClass="error" /><br>
                        </div>
                        <div class="col">
                            <label class="my-2"> Order Description <f:textarea rows="4" cols="50" path="description" class="mt-1"/> </label>
                            <f:errors path="description" cssClass="error" />
                        </div>
                    </div>

                    <label> Medic Identification</label>
                    <div>
                        <input required type="file" name="orderAttach" accept="image/png, image/jpeg"/>
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