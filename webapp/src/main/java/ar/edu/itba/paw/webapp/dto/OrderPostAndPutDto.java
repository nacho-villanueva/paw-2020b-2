package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.constraintGroups.OrderGroup;
import ar.edu.itba.paw.webapp.dto.constraintGroups.OrderPostGroup;
import ar.edu.itba.paw.webapp.dto.validators.ClinicIdIsValid;
import ar.edu.itba.paw.webapp.dto.validators.MedicPlanDtoIsCompleteAndValid;
import ar.edu.itba.paw.webapp.dto.validators.StudyTypeIdIsValid;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class OrderPostAndPutDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.order.v1";

    // Variables
    @NotNull(message = "OrderPostAndPutDto.clinicId.NotNull", groups = {OrderPostGroup.class})
    @ClinicIdIsValid(message = "OrderPostAndPutDto.clinicId.ClinicIdIsValid", groups = {OrderGroup.class})
    private Integer clinicId;

    @NotBlank(message = "OrderPostAndPutDto.patientEmail.NotBlank", groups = {OrderPostGroup.class})
    @Email(message = "OrderPostAndPutDto.patientEmail.Email", groups = {OrderPostGroup.class})
    private String patientEmail;

    @NotBlank(message = "OrderPostAndPutDto.patientName.NotBlank", groups = {OrderPostGroup.class})
    private String patientName;

    @NotNull(message = "OrderPostAndPutDto.studyTypeId.NotNull", groups = {OrderPostGroup.class})
    @StudyTypeIdIsValid(message = "OrderPostAndPutDto.studyTypeId.StudyTypeIdIsValid", groups = {OrderPostGroup.class})
    private Integer studyTypeId;

    private String description;

    @MedicPlanDtoIsCompleteAndValid(message = "OrderPostAndPutDto.medicPlan.MedicPlanDtoIsCompleteAndValid", groups = {OrderPostGroup.class})
    private MedicPlanDto medicPlan;

    // Getters&Setters
    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getStudyTypeId() {
        return studyTypeId;
    }

    public void setStudyTypeId(Integer studyTypeId) {
        this.studyTypeId = studyTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MedicPlanDto getMedicPlan() {
        return medicPlan;
    }

    public void setMedicPlan(MedicPlanDto medicPlan) {
        this.medicPlan = medicPlan;
    }


    // Equals&Hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPostAndPutDto that = (OrderPostAndPutDto) o;
        return Objects.equals(clinicId, that.clinicId) && Objects.equals(patientEmail, that.patientEmail) && Objects.equals(patientName, that.patientName) && Objects.equals(studyTypeId, that.studyTypeId) && Objects.equals(description, that.description) && Objects.equals(medicPlan, that.medicPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clinicId, patientEmail, patientName, studyTypeId, description, medicPlan);
    }
}
