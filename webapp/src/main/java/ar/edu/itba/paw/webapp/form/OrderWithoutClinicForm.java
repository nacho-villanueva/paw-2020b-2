package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderWithoutClinicForm {

    @Min(0)
    private Integer medicId;

    @Min(1)
    private Integer studyId;

    private String description;

    @NotBlank
    private String patient_insurance_plan;
    @NotNull
    private String patient_insurance_number;

    @NotBlank
    @Email
    private String patientEmail;
    @NotBlank
    private String patientName;

    public OrderWithoutClinicForm(Integer medicId, Integer studyId, String description, String patient_insurance_plan, String patient_insurance_number, String patientEmail, String patientName) {
        this.medicId = medicId;
        this.studyId = studyId;
        this.description = description;
        this.patient_insurance_plan = patient_insurance_plan;
        this.patient_insurance_number = patient_insurance_number;
        this.patientEmail = patientEmail;
        this.patientName = patientName;
    }

    public OrderWithoutClinicForm(){ }

    public Integer getMedicId() {
        return medicId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public String getDescription() {
        return description;
    }

    public String getPatient_insurance_plan() {
        return patient_insurance_plan;
    }

    public String getPatient_insurance_number() {
        return patient_insurance_number;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPatient_insurance_plan(String patient_insurance_plan) {
        this.patient_insurance_plan = patient_insurance_plan;
    }

    public void setPatient_insurance_number(String patient_insurance_number) {
        this.patient_insurance_number = patient_insurance_number;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
