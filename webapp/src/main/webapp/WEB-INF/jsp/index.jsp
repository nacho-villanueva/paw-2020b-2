<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/include-header.jsp"%>
    <link rel="stylesheet" href="<c:url value="/resources/css/homepage.css"/>">
</head>
<body>
<div class="main-container">
    <nav class="navbar-expand-lg fixed-top py-3 navbar bg-light" id="mainNav">
        <div class="container">
            <a class="navbar-title text-primary"><i class="fas fa-laptop-medical fa-lg"></i>&nbsp;&nbsp;/&nbsp;&nbsp;MedTransfer</a>
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
            <div class="" id="navbarResponsive">
                <ul class="navbar-nav ml-auto my-2 my-lg-0">
                    <li class="nav-item"><a class="navbar-options" href="#team">Team</a></li>
                    <li class="nav-item"><a class="navbar-options" href="#help">Help</a></li>
                    <li class="nav-item"><a class="navbar-options" href="#About Us">About Us</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="card main-card bg-light">
        <div class="card-body">
            <h1 class="text-center text-highlight">Your medical records</h1>
            <h4 class="text-center text-highlight">Anywhere. Anytime. Always.</h4>
            <hr class="divider my-4">
            <ul class="nav nav-pills nav-justified" id="myTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-item nav-link active" id="login-tab" data-toggle="tab" href="#login" role="tab" aria-controls="login" aria-selected="true">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="register-tab" data-toggle="tab" href="#register" role="tab" aria-controls="register" aria-selected="false">Register</a>
                </li>
            </ul>

            <hr class="divider my-4">

            <div class="tab-content">
                <div id="login" class="tab-pane fade in show active">

                    <c:url var="login"  value="/login"/>
                    <f:form action="${login}" method="post" class="form-signin" >
                        <fieldset class="form-group">
                            <label for="loginEmail" class="bmd-label-static">Email Address</label>
                            <input type="email" class="form-control" id="loginEmail" placeholder="Email Address" path="TODO:placeholder1" />
                        </fieldset>
                        <fieldset class="form-group">
                            <label for="loginPassword" class="bmd-label-static">Password</label>
                            <input type="password" class="form-control" id="loginPassword" placeholder="Password" path="TODO:placeholder2"/>
                        </fieldset>

                        <div class="checkbox">
                            <label>
                                <input type="checkbox" path="TODO:placeholder3"/> Stay Logged In
                            </label>
                        </div>
                        <input type="submit" class="row btn btn-lg btn-primary btn-block" value="Sign in">
                    </f:form>
                </div>



                <div id="register" class="tab-pane fade">
                    <form class="form-signin">
                        <div class="row">
                            <div class="col-2 m-0 p-0">
                                <p>I am a:</p>
                            </div>
                            <div class="mb-0 toggle_radio text-center">
                                <input type="radio" class="toggle_option" id="first_toggle" name="toggle_option">
                                <input type="radio" checked class="toggle_option" id="second_toggle" name="toggle_option">
                                <input type="radio" class="toggle_option" id="third_toggle" name="toggle_option">
                                <label for="first_toggle">Patient</label>
                                <label for="second_toggle">Doctor</label>
                                <label for="third_toggle">Clinic</label>
                                <div class="toggle_option_slider">
                                </div>
                            </div>
                        </div>

                        <fieldset class="form-group">
                            <label for="registerEmail" class="bmd-label-static">Email Address</label>
                            <input type="email" class="form-control" id="registerEmail" placeholder="Email Address">
                        </fieldset>

                        <fieldset class="form-group">
                            <label for="registerPassword" class="bmd-label-static">Password</label>
                            <input type="password" class="form-control" id="registerPassword" placeholder="Password">
                        </fieldset>
                        <fieldset class="form-group">
                            <label for="registerPasswordConfirm" class="bmd-label-static">Confirm Password</label>
                            <input type="password" class="form-control" id="registerPasswordConfirm" placeholder="Confirm Password">
                        </fieldset>
                        <input type="submit" class="row btn btn-lg btn-primary btn-block" value="Register">
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

<%@ include file="fragments/include-scripts.jsp"%>

</body>
</html>