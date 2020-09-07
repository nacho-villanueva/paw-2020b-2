package ar.edu.itba.paw.model;

public class OrderForm {
    private Long medicId;
    private Long clinicId;
    private String study;
    private String description;
    private String patient_insurance_plan;
    private String patient_insurance_number;
    private String patientEmail;

    public OrderForm(Long medicId, Long clinicId, String study, String description, String patient_insurance_plan, String patient_insurance_number, String patientEmail) {
        this.medicId = medicId;
        this.clinicId = clinicId;
        this.study = study;
        this.description = description;
        this.patient_insurance_plan = patient_insurance_plan;
        this.patient_insurance_number = patient_insurance_number;
        this.patientEmail = patientEmail;
    }

    public OrderForm(){ }

    public Long getMedicId() {
        return medicId;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public String getStudy() {
        return study;
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

    public void setMedicId(Long medicId) {
        this.medicId = medicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public void setStudy(String study) {
        this.study = study;
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
}
