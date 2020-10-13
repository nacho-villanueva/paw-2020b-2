package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ValidStudyTypeId;
import ar.edu.itba.paw.webapp.form.validators.ValidClinicId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class OrderForm {

    private Integer medicId;

    @ValidClinicId
    private Integer clinicId;

    @ValidStudyTypeId
    private Integer studyId;

    private String description;

    private String patient_insurance_plan;

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

    public Integer getMedicId() {
        return medicId;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }
}
