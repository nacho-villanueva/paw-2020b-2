<!-- Edit User Form -->
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="post_edit_profile_user" value="/profile/edit/user/pass"/>
<spring:message code="profile-view.body.tab.user.password.data" var="placeholderPassword"/>
<f:form action="${post_edit_profile_user}" method="post" modelAttribute="editUserPasswordForm" enctype="application/x-www-form-urlencoded">

    <table class="table table-borderless table-responsive">
        <tbody>
        <tr>
            <td><spring:message code="profile-view.body.tab.user.email.label"/></td>
            <td class="output">
                <c:out value="${loggedUser.email}"/>
                <a class="btn btn-outline-primary" href="<c:url value="/profile/edit/user/email"/>"><spring:message code="profile-view.body.button.edit.user.email"/> </a>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-edit.form.user.newPassword"/></td>
            <td class="output">
                <fieldset>
                    <f:input type="password" cssClass="form-control" path="newPassword.password"
                             placeholder="${placeholderPassword}"/>
                    <f:errors path="newPassword.password" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-edit.form.user.newPasswordRepeat"/></td>
            <td class="output">
                <fieldset>
                    <f:input type="password" cssClass="form-control" path="newPassword.confirmPassword"
                             placeholder="${placeholderPassword}"/>
                    <f:errors path="newPassword.confirmPassword" cssClass="text-danger" element="small"/>
                    <f:errors path="newPassword" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        </tbody>
    </table>

    <hr class="divider my-4">
    <h5><spring:message code="profile-edit.form.insertPasswordMessage"/> </h5>

    <table class="table table-borderless table-responsive">
        <tbody>
        <tr>
            <td><spring:message code="profile-view.body.tab.user.password.label"/></td>
            <td class="output">
                <fieldset>
                    <f:input type="password" cssClass="form-control" path="password"
                             placeholder="${placeholderPassword}"/>
                    <f:errors path="password" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>

        </tbody>
    </table>


    <div class="row justify-content-center">
        <input type="submit" class="row btn btn-lg action-btn"
               value="<spring:message code="profile-edit.form.user.submit"/>"/>
    </div>
    <div class="row justify-content-center">
        <a class="btn btn-lg" href="<c:url value="/profile"/>"><spring:message code="profile-edit.form.cancel"/> </a>
    </div>
</f:form>
