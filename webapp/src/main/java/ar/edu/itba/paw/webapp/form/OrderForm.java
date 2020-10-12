package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ClinicIsValid;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderForm {

    @Min(0)
    private Integer medicId;

    @Min(0)
    @NotNull
    @ClinicIsValid
    private Integer clinicId;

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

    public OrderForm(Integer medicId, Integer clinicId, Integer studyId, String description, String patient_insurance_plan, String patient_insurance_number, String patientEmail) {
        this.medicId = medicId;
        this.clinicId = clinicId;
        this.studyId = studyId;
        this.description = description;
        this.patient_insurance_plan = patient_insurance_plan;
        this.patient_insurance_number = patient_insurance_number;
        this.patientEmail = patientEmail;
    }

    public OrderForm(){ }

    public Integer getMedicId() {
        return medicId;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatient_insurance_plan() {
        return patient_insurance_plan;
    }

    public void setPatient_insurance_plan(String patient_insurance_plan) {
        this.patient_insurance_plan = patient_insurance_plan;
    }

    public String getPatient_insurance_number() {
        return patient_insurance_number;
    }

    public void setPatient_insurance_number(String patient_insurance_number) {
        this.patient_insurance_number = patient_insurance_number;
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
}
