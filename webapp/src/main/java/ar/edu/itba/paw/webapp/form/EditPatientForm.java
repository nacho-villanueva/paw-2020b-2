package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditPatientForm {

    @NotNull
    @Size(min = 1, max = 100)
    private String fullName;

    @NotNull
    @Size(min = 1, max = 100)
    private String medicalPlan;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "[a-z0-9A-Z]+")
    private String medicalPlanNumber;

    @PasswordIsCorrect
    @NotNull
    private String password;


    public EditPatientForm() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMedicalPlan() {
        return medicalPlan;
    }

    public void setMedicalPlan(String medicalPlan) {
        this.medicalPlan = medicalPlan;
    }

    public String getMedicalPlanNumber() {
        return medicalPlanNumber;
    }

    public void setMedicalPlanNumber(String medicalPlanNumber) {
        this.medicalPlanNumber = medicalPlanNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
