<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Sidebar -->
<ul class="navbar-nav sidebar sidebar-dark bg-gradient-primary accordion" id="accordionSidebar">


    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Nav Item - Pages Collapse Menu -->
    <li class="nav-item <c:if test='${param.current == "home"}'>active</c:if>">
        <a class="nav-link" href="<c:url value='/home' />">
            <i class="fas fa-fw fa-clinic-medical"></i>
            <span><spring:message key="fragments.sidebar.home" /></span></a>
    </li>

    <!-- Heading -->
    <div class="sidebar-heading">
        Orders
    </div>

    <!-- Nav Item - Pages Collapse Menu -->
    <li class="nav-item <c:if test='${param.current == "orders"}'>active</c:if>">
        <a class="nav-link" href="<c:url value='/my-orders' />">
            <i class="fas fa-fw fa-table"></i>
            <span><spring:message key="fragments.sidebar.myorders" /></span></a>
    </li>

    <sec:authorize access="hasRole('ROLE_MEDIC') and hasRole('ROLE_VERIFIED')">
        <!-- Nav Item - Utilities Collapse Menu -->
        <li class="nav-item <c:if test='${param.current == "create-order"}'>active</c:if>">
            <a class="nav-link" href="<c:url value='/create-order' />">
                <i class="fas fa-fw fa-plus"></i>
                <span><spring:message key="fragments.sidebar.createorder" /></span></a>
        </li>
        <!-- Nav Item - Utilities Collapse Menu -->
        <li class="nav-item <c:if test='${param.current == "request-orders"}'>active</c:if>">
            <a class="nav-link" href="<c:url value='/request-orders' />">
                <i class="fas fa-file-medical"></i>
                <span>Request Share Orders </span></a>
        </li>
    </sec:authorize>

    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Nav Item -->
    <li class="nav-item <c:if test='${param.current == "search"}'>active</c:if>">
        <a class="nav-link" href="<c:url value='/advanced-search/clinic' />">
            <i class="fas fa-fw fa-search"></i>
            <span><spring:message key="fragments.sidebar.search" /></span></a>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider">



    <li class="nav-item">
        <a class="nav-link" href="<c:url value='/logout' />">
            <i class="fas fa fa-sign-out-alt "></i>
            <span><spring:message key="fragments.sidebar.logout" /></span></a>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider d-none d-md-block">
</ul>
