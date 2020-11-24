<!-- Edit Patient Form -->
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="post_edit_profile_user" value="/profile/edit/patient"/>
<f:form action="${post_edit_profile_user}" method="post" modelAttribute="editPatientForm" enctype="application/x-www-form-urlencoded">

    <table class="table table-borderless table-responsive">
        <tbody>

        <tr>
            <td><spring:message code="profile-view.body.tab.patient.name.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.patient.name.label" var="full_namePlaceholder"/>
                    <f:input type="text" cssClass="form-control" path="full_name" placeholder="${full_namePlaceholder}"/>
                    <f:errors path="full_name" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.patient.medicPlan.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.patient.medicPlan.label" var="medical_planPlaceholder"/>
                    <f:input type="text" cssClass="form-control" path="medical_plan" placeholder="${medical_planPlaceholder}"/>
                    <f:errors path="medicalPlan" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.patient.medicPlanNumber.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.patient.medicPlanNumber.label" var="medicPlanNumberPlaceholder"/>
                    <f:input type="text" cssClass="form-control" path="medical_plan_number" placeholder="${medicPlanNumberPlaceholder}"/>
                    <f:errors path="medical_plan_number" cssClass="text-danger" element="small"/>
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
                    <spring:message code="profile-view.body.tab.user.password.data" var="placeholderPassword"/>
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
               value="<spring:message code="profile-edit.form.patient.submit"/>"/>
    </div>
    <div class="row justify-content-center">
        <a class="btn btn-lg" href="<c:url value="/profile"/>"><spring:message code="profile-edit.form.cancel"/> </a>
    </div>
</f:form>

