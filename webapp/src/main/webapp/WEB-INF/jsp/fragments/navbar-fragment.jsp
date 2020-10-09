<nav class="navbar-expand-lg fixed-top py-3 navbar bg-light" id="mainNav">
    <div class="container">
        <c:url value="/" var="homepage" />
        <a class="navbar-title text-primary" href="${homepage}"><i class="fas fa-laptop-medical fa-lg"></i>&nbsp;&nbsp;/&nbsp;&nbsp;<spring:message code="appname"/></a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="" id="navbarResponsive">
            <ul class="navbar-nav ml-auto my-2 my-lg-0">
                <li class="nav-item"><a class="navbar-options" href="#team"><spring:message code="fragments.navbar.team"/></a></li>
                <li class="nav-item"><a class="navbar-options" href="#help"><spring:message code="fragments.navbar.help"/></a></li>
                <li class="nav-item"><a class="navbar-options" href="#About Us"><spring:message code="fragments.navbar.aboutUs"/></a></li>
            </ul>
        </div>
    </div>
</nav>