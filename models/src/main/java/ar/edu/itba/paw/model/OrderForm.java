package ar.edu.itba.paw.model;

public class OrderForm {
    private Integer medicId;
    private Integer clinicId;
    private Integer studyId;
    private String description;
    private String patient_insurance_plan;
    private String patient_insurance_number;
    private String patientEmail;
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

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
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
