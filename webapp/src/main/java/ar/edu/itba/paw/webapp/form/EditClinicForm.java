package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;
import ar.edu.itba.paw.webapp.form.validators.StudyTypesAreValid;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditClinicForm {

    @NotNull
    @Size(min = 1, max = 100)
    private String full_name;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "\\+?[0-9]+")
    private String telephone;

    @NotNull
    @StudyTypesAreValid
    private Integer[] available_studies;

    @PasswordIsCorrect
    @Size(min = 6, max = 100)
    private String password;

    public EditClinicForm(){

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer[] getAvailable_studies() {
        return available_studies;
    }

    public void setAvailable_studies(Integer[] available_studies) {
        this.available_studies = available_studies;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
