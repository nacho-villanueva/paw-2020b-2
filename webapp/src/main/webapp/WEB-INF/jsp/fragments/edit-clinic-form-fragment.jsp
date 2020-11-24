<!-- Edit Patient Form -->
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="post_edit_profile_user" value="/profile/edit/clinic"/>
<script type="text/javascript">
    $(document).on('keyup keypress', '#newStudyType', function(e) {
        if(e.keyCode === 13) {
            e.preventDefault();
        }else if(e.keyCode === 27){
            $('#createStudyType').modal('hide');
        }
    });
</script>
<f:form action="${post_edit_profile_user}" method="post" id="editClinic" modelAttribute="editClinicForm" enctype="application/x-www-form-urlencoded">

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
        </tbody>
    </table>

    <!-- Open Day and Time Picker -->
    <div>
        <fieldset class="form-group">
            <label><spring:message code="profile-view.body.tab.clinic.open_days.label" /></label>
            <f:select path="clinicHoursForm.open_days" id="openDaysSelect" class="selectpicker" onchange="onDayUpdate()" data-style="text-primary" multiple="true">
                <f:option value="0"><spring:message code="days.day-0" /></f:option>
                <f:option value="1"><spring:message code="days.day-1" /></f:option>
                <f:option value="2"><spring:message code="days.day-2" /></f:option>
                <f:option value="3"><spring:message code="days.day-3" /></f:option>
                <f:option value="4"><spring:message code="days.day-4" /></f:option>
                <f:option value="5"><spring:message code="days.day-5" /></f:option>
                <f:option value="6"><spring:message code="days.day-6" /></f:option>
            </f:select>
            <br>
            <small class="text-muted"><spring:message code="profile-edit.form.clinic.selectdays.help" /></small><br>
            <f:errors path="clinicHoursForm" cssClass="text-danger" /> <br>
            <f:errors path="clinicHoursForm.open_days" cssClass="text-danger" />
        </fieldset>
        <table>
            <tbody id="daysHourList">
            </tbody>
        </table>
        <f:hidden id="OT_0" path="clinicHoursForm.opening_time[0]" />
        <f:hidden id="OT_1" path="clinicHoursForm.opening_time[1]" />
        <f:hidden id="OT_2" path="clinicHoursForm.opening_time[2]" />
        <f:hidden id="OT_3" path="clinicHoursForm.opening_time[3]" />
        <f:hidden id="OT_4" path="clinicHoursForm.opening_time[4]" />
        <f:hidden id="OT_5" path="clinicHoursForm.opening_time[5]" />
        <f:hidden id="OT_6" path="clinicHoursForm.opening_time[6]" />

        <f:hidden id="CT_0" path="clinicHoursForm.closing_time[0]" />
        <f:hidden id="CT_1" path="clinicHoursForm.closing_time[1]" />
        <f:hidden id="CT_2" path="clinicHoursForm.closing_time[2]" />
        <f:hidden id="CT_3" path="clinicHoursForm.closing_time[3]" />
        <f:hidden id="CT_4" path="clinicHoursForm.closing_time[4]" />
        <f:hidden id="CT_5" path="clinicHoursForm.closing_time[5]" />
        <f:hidden id="CT_6" path="clinicHoursForm.closing_time[6]" />
    </div>

    <hr class="divider my-4">

    <fieldset class="input-group pt-4">
        <label for="addPlanInput" class="bmd-label-static"><spring:message code="profile-edit.form.clinic.acceptedPlans.label" /></label>
        <input id="addPlanInput" type="text" class="form-control">
        <div class="input-group-append">
            <input class="btn btn-primary" id="enter" type="button" onclick="addPlanToList();" value="<spring:message code="profile-view.body.tab.clinic.acceptedPlans.add" />" />
        </div>
        <f:hidden id="plansInputList" path="acceptedPlans" value="" />
    </fieldset>
    <div id="plansList" class="mb-2 mt-2">
        <small class="text-muted"><spring:message code="profile-edit.form.clinic.acceptedPlans.confirmation" />: </small>
        <template id="planPillTemplate">
            <a class="mr-2 mb-1"><span class="badge-md badge-pill badge-primary"><i class="fa fa-times ml-1"></i></span></a>
        </template>
    </div>
    <fieldset>
        <label>
        <spring:message code="profile-view.body.tab.clinic.medical_studies.label"/>
        </label>
        <spring:message code="profile-edit.form.clinic.study_types.placeholder" var="study_typesPlaceholder"/>
        <f:select id="studyTypes" name="studyTypes" cssClass="selectpicker" title="${study_typesPlaceholderPlaceholder}" data-live-search="true" path="availableStudies" data-style="text-primary">
            <f:options items="${studiesList}" itemLabel="name" itemValue="name" />
        </f:select>
        <f:errors path="availableStudies" cssClass="text-danger" element="small"/>
        <!-- Button trigger modal -->
        <a href="#" data-toggle="modal" data-target="#createStudyType"><p><spring:message code="profile-edit.form.clinic.add_medical_study" /></p></a>
        <!-- Modal -->
        <div class="modal fade" id="createStudyType" tabindex="-1" role="dialog" aria-labelledby="studyTypeModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="studyTypeModalLabel"><spring:message code="create-type.body.header"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="alertBox">
                        </div>
                        <fieldset class="form-group">
                            <label for="newStudyType" class="bmd-label-floating"><spring:message code="create-type.body.form.name.label"/></label>
                            <input type="text" id="newStudyType" class="form-control" />
                        </fieldset>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="create-field.body.form.return"/></button>
                        <button type="button" class="btn btn-primary" onclick='addOptionValue("newStudyType","#studyTypes","#alertBox")'><spring:message code="create-type.body.form.submit"/></button>
                    </div>
                </div>
            </div>
        </div>


    </fieldset>

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
</f:form>

