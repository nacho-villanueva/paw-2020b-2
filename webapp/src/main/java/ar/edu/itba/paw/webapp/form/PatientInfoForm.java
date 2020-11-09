package ar.edu.itba.paw.webapp.form;

public class PatientInfoForm {

    private Boolean existingPatient;
    private String insurancePlan;
    private String insuranceNumber;
    private String email;
    private String name;

    public PatientInfoForm() {
        this.existingPatient = false;
    }

    public PatientInfoForm(Boolean existingPatient, String insurancePlan, String insuranceNumber, String email, String name) {
        this.existingPatient = existingPatient;
        this.insurancePlan = insurancePlan;
        this.insuranceNumber = insuranceNumber;
        this.email = email;
        this.name = name;
    }

    public Boolean getExistingPatient() {
        return existingPatient;
    }

    public void setExistingPatient(Boolean existingPatient) {
        this.existingPatient = existingPatient;
    }

    public String getInsurancePlan() {
        return insurancePlan;
    }

    public void setInsurancePlan(String insurancePlan) {
        this.insurancePlan = insurancePlan;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
