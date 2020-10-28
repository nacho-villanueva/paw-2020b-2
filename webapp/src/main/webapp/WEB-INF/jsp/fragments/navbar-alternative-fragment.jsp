<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<nav class="navbar-expand-lg fixed-top py-3 navbar bg-light" id="mainNav">
    <div class="container">
        <c:url value="/" var="homepage" />
        <a class="navbar-title text-primary" href="${homepage}"><i class="fas fa-laptop-medical fa-lg"></i>&nbsp;&nbsp;/&nbsp;&nbsp;<spring:message code="appname"/></a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="" id="navbarResponsive">
            <ul class="navbar-nav ml-auto my-2 my-lg-0">
                <li class="nav-item">
                    <a class="navbar-options" href="<c:url value='/profile' />">
                        <i class="fas fa-lg fa-user-circle icons"></i>
                        <spring:message code="fragments.sidebar.profile" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>