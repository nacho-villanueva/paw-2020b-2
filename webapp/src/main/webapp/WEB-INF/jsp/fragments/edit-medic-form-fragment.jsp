<!-- Edit Patient Form -->
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="post_edit_profile_user" value="/profile/edit/medic"/>
<script type="text/javascript">
    $(document).on('keyup keypress', '#newMedicalField', function(e) {
        if(e.keyCode === 13) {
            e.preventDefault();
        }else if(e.keyCode === 27){
            $('#createMedicalField').modal('hide');
        }
    });
</script>
<f:form action="${post_edit_profile_user}" method="post" modelAttribute="editMedicForm" enctype="multipart/form-data">

    <table class="table table-borderless table-responsive" style="overflow: hidden;">
        <tbody>

        <tr>
            <td><spring:message code="profile-view.body.tab.medic.name.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.medic.name.label" var="fullNamePlaceholder"/>
                    <f:input type="text" cssClass="form-control" path="fullName" placeholder="${fullNamePlaceholder}"/>
                    <f:errors path="fullName" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.medic.telephone.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.medic.telephone.label" var="telephonePlaceholder"/>
                    <f:input type="tel" cssClass="form-control" id="phoneNumberMedic" pattern="\+?[0-9\-]+" path="telephone" placeholder="${telephonePlaceholder}"/>
                    <f:errors path="telephone" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.medic.identification.label"/></td>
            <td class="output">
                <fieldset>
                    <f:input type="file" path="identification" id="identificationFile"/><br/>
                    <small class="text-muted"><spring:message code="profile-edit.form.medic.identification.format"/></small>
                    <f:errors path="identification" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.medic.licenceNumber.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-view.body.tab.medic.licenceNumber.label" var="licenceNumberPlaceholder"/>
                    <f:input type="text" cssClass="form-control" pattern="[a-zA-Z0-9]*" id="licenseNumber" path="licenceNumber" placeholder="${licenceNumberPlaceholder}"/>
                    <small class="text-muted"><spring:message code="profile-edit.form.medic.licenceNumber.format"/></small>
                    <f:errors path="licenceNumber" cssClass="text-danger" element="small"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td><spring:message code="profile-view.body.tab.medic.medicalFields.label"/></td>
            <td class="output">
                <fieldset>
                    <spring:message code="profile-edit.form.medic.medicalFields.placeholder" var="medicalFieldsPlaceholder"/>
                    <f:select id="medicalFields" cssClass="selectpicker" title="${medicalFieldsPlaceholder}" data-live-search="true" path="knownFields" data-style="btn-custom" data-container="body">
                        <f:options items="${fieldsList}" itemLabel="name" itemValue="name" />
                    </f:select>
                    <f:errors path="knownFields" cssClass="text-danger" element="small"/>
                    <!-- Button trigger modal -->
                    <a href="#" data-toggle="modal" data-target="#createMedicalField"><p><spring:message code="profile-edit.form.medic.addMedicalField" /></p></a>
                    <!-- Modal -->
                    <div class="modal fade" id="createMedicalField" tabindex="-1" role="dialog" aria-labelledby="medicalFieldModal" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="medicalFieldModalLabel"><spring:message code="create-field.body.header"/></h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <div id="alertBox">
                                    </div>
                                    <fieldset class="form-group">
                                        <label for="newMedicalField" class="bmd-label-floating"><spring:message code="create-field.body.form.name.label"/></label>
                                        <input type="text" id="newMedicalField" class="form-control" />
                                    </fieldset>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="create-field.body.form.return"/></button>
                                    <button type="button" class="btn btn-primary" onclick='addOptionValue("newMedicalField","#medicalFields","#alertBox")'><spring:message code="create-field.body.form.submit"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
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
               value="<spring:message code="profile-edit.form.medic.submit"/>"/>
    </div>
    <div class="row justify-content-center">
        <a class="btn btn-lg" href="<c:url value="/profile"/>"><spring:message code="profile-edit.form.cancel"/> </a>
    </div>
</f:form>

