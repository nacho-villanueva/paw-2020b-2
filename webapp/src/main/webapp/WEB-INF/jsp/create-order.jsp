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


    <title>Create Order</title>
</head>
<body>
<div class="container">
    <div class="row justify-content-center pb-8">
        <div class="card" style="width: 50rem">
            <div class="card-header">
                <h1 class="card-title"> Create New Medical Order</h1>
            </div>
            <div class="card-body">

                <c:url var="post_createorder"  value="/create-order"/>
                <f:form action="${post_createorder}" method="post" modelAttribute="orderForm" enctype="multipart/form-data">

                    <label>Medic's Name</label>
                    <f:select cssClass="selectpicker" data-live-search="true" path="medicId" >
                        <f:option value="-" label="Choose Medic"/>
                        <f:options items="${medicsList}"/>
                    </f:select><br>

                    <label>Medical Clinic </label>
                    <f:select cssClass="selectpicker" data-live-search="true" path="clinicId" >
                        <f:option value="-" label="Choose Clinic"/>
                        <f:options items="${clinicsList}"/>
                    </f:select><br>

                    <hr class="mt-3 mb-2"/>

                    <label> Patient Email <f:input type="email" path="patientEmail"/> </label> <br>
                    <label> Patient Insurance Plan <f:input type="text" path="patient_insurance_plan" /> </label> <br>
                    <label> Patient Insurance Number <f:input type="text" path="patient_insurance_number" /> </label> <br>

                    <label>Study Type </label>
                    <f:select cssClass="selectpicker" data-live-search="true" path="study" >
                        <f:option value="-" label="Choose Study"/>
                        <f:options items="${studiesList}"/>
                    </f:select><br>

                    <label> Order Description <f:textarea rows="4" cols="50" path="description" /> </label> <br>
                    <label> Medical Order Image <input required type="file" name="orderAttach" accept="image/png, image/jpeg"/> </label> <br>
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

    <!-- Bootstrap JS Scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
</body>
</html>