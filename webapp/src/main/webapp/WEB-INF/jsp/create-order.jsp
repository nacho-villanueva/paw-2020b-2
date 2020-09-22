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
                            clinicList += '<option value="' + response[i].id + '">' + response[i].name + '</option>';
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

    <title>Create Order</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <a class="navbar-brand ml-4" href="<c:url value="/" />"> <i class="fas fa-laptop-medical fa-lg"></i> MedTransfer</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value="/" />">Home <span class="sr-only">(current)</span></a>
            </li    >
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/create-order" />">Create a new order</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">View studies (WIP)</a>
            </li>
        </ul>
        <span class="navbar-text">
            <!--space for future user profile icon-->
        </span>
    </div>
</nav>
<div class="container mt-5">
    <div class="row justify-content-center pb-8">
        <div class="card" style="width: 50rem">
            <div class="card-header">
                <h1 class="card-title"> Create New Medical Order</h1>
            </div>
            <div class="card-body">

                <c:url var="post_createorder"  value="/create-order"/>
                <f:form action="${post_createorder}" method="post" modelAttribute="orderForm" enctype="multipart/form-data">

                    <!--ya no se usa, el logged user es el medic -->
                    <label>Medic's Name</label>
                    <f:select cssClass="selectpicker" data-live-search="true" path="medicId">
                        <f:option value="-1" label="Choose Medic"/>
                        <f:options items="${medicsList}" itemLabel="name" itemValue="id"/>
                    </f:select>
                    <f:errors path="medicId" cssClass="error" /><br>

                    <label>Medical Clinic </label>
                    <f:select id="clinic" cssClass="selectpicker" data-live-search="true" path="clinicId" disabled="true">
                        <f:option value="-1">Choose Study first</f:option>
                    </f:select>
                    <f:errors path="clinicId" cssClass="error" /><br>

                    <hr class="mt-3 mb-2"/>

                    <label> Patient Name <f:input type="text" path="patientName" required="required"/> </label>
                    <f:errors path="patientName" cssClass="error" /><br>
                    <label> Patient Email <f:input type="email" path="patientEmail" required="required"/> </label>
                    <f:errors path="patientEmail" cssClass="error" /><br>
                    <label> Patient Insurance Plan <f:input type="text" path="patient_insurance_plan" required="required"/> </label>
                    <f:errors path="patient_insurance_plan" cssClass="error" /><br>
                    <label> Patient Insurance Number <f:input type="text" path="patient_insurance_number" required="required"/> </label>
                    <f:errors path="patient_insurance_number" cssClass="error" /><br>

                    <label>Study Type </label>
                    <f:select id="medicalStudy" cssClass="selectpicker" data-live-search="true" path="studyId" >
                        <f:option value="-1" label="Choose Study"/>
                        <f:options items="${studiesList}" itemLabel="name" itemValue="id"/>
                    </f:select>
                    <f:errors path="studyId" cssClass="error" /><br>

                    <label> Order Description <f:textarea rows="4" cols="50" path="description" /> </label>
                    <f:errors path="description" cssClass="error" /> <br>

                    <!--ya no va identificacion, se carga solo desde la cuenta -->
                    <label> Medic Identification <input required type="file" name="orderAttach" accept="image/png, image/jpeg"/> </label> <br>
                    <input type="submit" value="Create Order"/>

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