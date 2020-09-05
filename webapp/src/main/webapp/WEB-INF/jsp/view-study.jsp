<%--
  Created by IntelliJ IDEA.
  User: matia
  Date: 05-Sep-20
  Time: 6:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">

    <title>Hello, world!</title>
</head>
<body>
<h1>Hello, world!</h1>
<h2>${order.id}</h2>
<div class="container">
    <div class="row justify-content-center">
        <div class="card text-center" style="width: 50rem;">
            <div class="card-header">
                Order
            </div>
            <div class="card-body">
                <h5 class="card-title">Order Number: ${order.id}</h5>
                <hr class="mt-3 mb-2"/>
                <div class="row justify-content-start">
                    <div class="col">Date: 01-10-2021</div>
                    <div class="col">Patient: Calipsto, Posadas A.</div>
                    <div class="w-100"></div>
                    <div class="col">Medic Plan: Galera Azul</div>
                    <div class="col">Medic Plan Number: 01-841230/8</div>
                </div>
                <hr class="mt-3 mb-4"/>
                <p class="card-text">SOME UNREADABLE CALIGRAPHY<br>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                    <br>SOMETHING SOMETHING LUPUS</p>
                <hr class="mt-5 mb-4"/>
                <div class="media">
                    <div class="media-body">
                        <h5 class="mt-0 mb-1">Dr. Cesar Aritmetica Jr.</h5>
                        M.N.: 241351
                    </div>
                    <img src="../IMG_20190605_210209.jpg" class="align-self-end ml-3" alt="the medic's signature">
                </div>
            </div>
        </div>
    </div>
    <a href="#" class="btn btn-primary align-self-center">Go somewhere</a>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
</body>
</html>
