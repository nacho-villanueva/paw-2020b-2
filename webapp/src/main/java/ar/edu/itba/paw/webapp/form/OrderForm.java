package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ValidStudyTypeId;
import ar.edu.itba.paw.webapp.form.validators.ValidClinicId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class OrderForm {

    @NotNull
    @ValidClinicId
    private Integer clinicId;

    @NotNull
    @ValidStudyTypeId
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

    public OrderForm(){ }

    public Integer getClinicId() {
        return clinicId;
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

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
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
