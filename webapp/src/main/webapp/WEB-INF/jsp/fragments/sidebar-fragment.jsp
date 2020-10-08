<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Sidebar -->
<ul class="navbar-nav sidebar sidebar-dark bg-gradient-primary accordion" id="accordionSidebar">

    <!-- Sidebar - Brand -->
    <a class="sidebar-brand d-flex align-items-center justify-content-center" href="<c:url value='/home' />">
        <div class="sidebar-brand-icon">
            <i class="fas fa-laptop-medical"></i>
        </div>
        <div class="mx-3">/MedTransfer</div>
    </a>

    <!-- Divider -->
    <hr class="sidebar-divider my-0">

    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Heading -->
    <div class="sidebar-heading">
        Orders
    </div>

    <!-- Nav Item - Pages Collapse Menu -->
    <li class="nav-item <c:if test='${param.current == "orders"}'>active</c:if>">
        <a class="nav-link" href="<c:url value='/home' />">
            <i class="fas fa-fw fa-table"></i>
            <span>My Orders</span></a>
    </li>

    <sec:authorize access="hasRole('ROLE_MEDIC')">
    <!-- Nav Item - Utilities Collapse Menu -->
    <li class="nav-item <c:if test='${param.current == "create-order"}'>active</c:if>">
        <a class="nav-link" href="<c:url value='/create-order' />">
            <i class="fas fa-fw fa-plus"></i>
            <span>Create Order</span></a>
    </li>
    </sec:authorize>

    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Heading -->
    <div class="sidebar-heading">
        Settings
    </div>

    <li class="nav-item <c:if test='${param.current == "modify-information"}'>active</c:if>">
        <a class="nav-link" href="<c:url value='/modify-information' />">
            <i class="fas fa-fw fa-sliders-h"></i>
            <span>Modify Information</span></a>
    </li>

    <li class="nav-item">
        <a class="nav-link" href="<c:url value='/logout' />">
            <i class="fas fa fa-sign-out "></i>
            <span>Logout</span></a>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider d-none d-md-block">
</ul>
