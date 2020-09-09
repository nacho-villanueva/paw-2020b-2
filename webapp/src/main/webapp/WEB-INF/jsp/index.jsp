<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">

    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">


    <title>Home</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <a class="navbar-brand ml-4" href="#"> <i class="fas fa-laptop-medical fa-lg"></i> MedTransfer</a>
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
<div class="container">
    <div class="jumbotron mt-4 mx-auto">
        <h2 class="display-4">Your medical records, in your hand.</h2>
        <h1>Anywhere. Anytime. Always.</h1>
        <p class="lead">Your medics can now fill in their orders or upload results to studies into MedTransfer, and you get it
         in your computer, phone or tablet.</p>
        <hr class="my-4">
        <p>Get notified and never lose another important file just because a piece of paper went missing.</p>
        <p class="lead align-items-center text-center mt-4 mb-0">
            <a class="btn btn-outline-primary btn-lg" href="<c:url value="/create-order" />" role="button"><i class="fas fa-file-medical-alt"></i> Create a new Order</a>
        </p>
    </div>
    <footer class="footer" style="position: absolute; left: 0; bottom: 0; width: 100%; background: #f5f5f5 ;">
        <div class="inner text-center my-3">
            <p class="text-muted">A <a href="<c:url value="/" />">Group2</a> project</p>
        </div>
    </footer>

</div>



<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
</body>
</html>