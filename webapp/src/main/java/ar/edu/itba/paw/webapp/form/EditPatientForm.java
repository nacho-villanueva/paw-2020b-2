package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditPatientForm {

    @NotNull
    @Size(min = 1, max = 100)
    private String full_name;

    @NotNull
    @Size(min = 1, max = 100)
    private String medical_plan;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "[a-z0-9A-Z]+")
    private String medical_plan_number;

    @PasswordIsCorrect
    @NotNull
    private String password;


    public EditPatientForm() {
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMedical_plan() {
        return medical_plan;
    }

    public void setMedical_plan(String medical_plan) {
        this.medical_plan = medical_plan;
    }

    public String getMedical_plan_number() {
        return medical_plan_number;
    }

    public void setMedical_plan_number(String medical_plan_number) {
        this.medical_plan_number = medical_plan_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
