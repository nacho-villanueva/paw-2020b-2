package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.annotations.PatientPlan;
import ar.edu.itba.paw.webapp.dto.annotations.ClinicId;
import ar.edu.itba.paw.webapp.dto.annotations.StudyTypeId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class OrderPostDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.order.v1";

    // Variables
    @NotNull(message = "OrderPostAndPutDto.clinicId.NotNull")
    @ClinicId(message = "OrderPostAndPutDto.clinicId.ClinicIdIsValid")
    private Integer clinicId;

    @NotBlank(message = "OrderPostAndPutDto.patientEmail.NotBlank")
    @Email(message = "OrderPostAndPutDto.patientEmail.Email")
    private String patientEmail;

    @NotBlank(message = "OrderPostAndPutDto.patientName.NotBlank")
    private String patientName;

    @NotNull(message = "OrderPostAndPutDto.studyTypeId.NotNull")
    @StudyTypeId(message = "OrderPostAndPutDto.studyTypeId.StudyTypeIdIsValid")
    private Integer studyTypeId;

    private String description;

    private String patientMedicPlan;

    private String patientMedicPlanNumber;

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


    // Equals&Hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPostDto that = (OrderPostDto) o;
        return Objects.equals(clinicId, that.clinicId) && Objects.equals(patientEmail, that.patientEmail) && Objects.equals(patientName, that.patientName) && Objects.equals(studyTypeId, that.studyTypeId) && Objects.equals(description, that.description) && Objects.equals(patientMedicPlan, that.patientMedicPlan) && Objects.equals(patientMedicPlanNumber, that.patientMedicPlanNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clinicId, patientEmail, patientName, studyTypeId, description, patientMedicPlan, patientMedicPlanNumber);
    }

    public String getPatientMedicPlan() {
        return patientMedicPlan;
    }

    public void setPatientMedicPlan(String patientMedicPlan) {
        this.patientMedicPlan = patientMedicPlan;
    }

    public String getPatientMedicPlanNumber() {
        return patientMedicPlanNumber;
    }

    public void setPatientMedicPlanNumber(String patientMedicPlanNumber) {
        this.patientMedicPlanNumber = patientMedicPlanNumber;
    }
}
