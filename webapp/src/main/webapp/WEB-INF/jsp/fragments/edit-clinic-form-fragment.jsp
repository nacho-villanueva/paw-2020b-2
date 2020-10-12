<!-- Edit Patient Form -->
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="post_edit_profile_user" value="/profile/edit/clinic"/>
<f:form action="${post_edit_profile_user}" method="post" modelAttribute="editClinicForm" enctype="application/x-www-form-urlencoded">

    <table class="table table-borderless table-responsive" style="overflow:hidden;">
        <tbody>

        <tr>
            <td><spring:message code="profile-view.body.tab.clinic.name.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.clinic.name.label" var="full_namePlaceholder"/>
                    <f:input type="text" cssClass="form-control" path="full_name" placeholder="${full_namePlaceholder}"/>
                    <f:errors path="full_name" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.clinic.telephone.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.clinic.telephone.label" var="telephonePlaceholder"/>
                    <f:input type="tel" cssClass="form-control" id="phoneNumberClinic" pattern="\+?[0-9\-]+" path="telephone" placeholder="${telephonePlaceholder}"/>
                    <f:errors path="telephone" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.clinic.medical_studies.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-edit.form.clinic.study_types.placeholder" var="study_typesPlaceholder"/>
                    <f:select id="studyTypes" cssClass="selectpicker" title="${study_typesPlaceholderPlaceholder}" data-live-search="true" path="available_studies" data-style="btn-custom" data-container="body">
                        <f:options items="${studiesList}" itemLabel="name" itemValue="id" />
                    </f:select>
                    <f:errors path="available_studies" cssClass="text-danger" element="small"/>
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
               value="<spring:message code="profile-edit.form.clinic.submit"/>"/>
    </div>
    <div class="row justify-content-center">
        <a class="btn btn-lg" href="<c:url value="/profile"/>"><spring:message code="profile-edit.form.cancel"/> </a>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha384-ZvpUoO/+PpLXR1lu4jmpXWu80pZlYUAfxl5NsBMWOEPSjUn/6Z/hRTt8+pR6L4N2" crossorigin="anonymous"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $('#studyTypes').selectpicker('val',${selectedStudies});
        });
    </script>
</f:form>

