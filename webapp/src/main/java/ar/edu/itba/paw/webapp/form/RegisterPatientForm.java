package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotBlank;

public class RegisterPatientForm {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String medicalInsurancePlan;

    private String medicalInsuranceNumber;


    public RegisterPatientForm() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMedicalInsurancePlan() {
        return medicalInsurancePlan;
    }

    public void setMedicalInsurancePlan(String medicalInsurancePlan) {
        this.medicalInsurancePlan = medicalInsurancePlan;
    }

    public String getMedicalInsuranceNumber() {
        return medicalInsuranceNumber;
    }

    public void setMedicalInsuranceNumber(String medicalInsuranceNumber) {
        this.medicalInsuranceNumber = medicalInsuranceNumber;
    }

    public String getFullname(){
        return getFirstName() + " " + getLastName();
    }
}
