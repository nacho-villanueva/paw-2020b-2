package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.validators.ClinicIdIsValid;

import javax.validation.constraints.Null;
import java.util.Objects;

public class OrderPutDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.order.v1";

    // Variables
    @ClinicIdIsValid(message = "OrderPostAndPutDto.clinicId.ClinicIdIsValid")
    private Integer clinicId;

    @Null(message = "OrderPostAndPutDto.patientEmail.Null")
    private String patientEmail;

    @Null(message = "OrderPostAndPutDto.patientName.Null")
    private String patientName;

    @Null(message = "OrderPostAndPutDto.studyTypeId.Null")
    private Integer studyTypeId;

    @Null(message = "OrderPostAndPutDto.description.Null")
    private String description;

    @Null(message = "OrderPostAndPutDto.medicPlan.Null")
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
        OrderPutDto that = (OrderPutDto) o;
        return Objects.equals(clinicId, that.clinicId) && Objects.equals(patientEmail, that.patientEmail) && Objects.equals(patientName, that.patientName) && Objects.equals(studyTypeId, that.studyTypeId) && Objects.equals(description, that.description) && Objects.equals(medicPlan, that.medicPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clinicId, patientEmail, patientName, studyTypeId, description, medicPlan);
    }
}
