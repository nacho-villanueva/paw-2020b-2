package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.model.ClinicHours;
import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;
import ar.edu.itba.paw.webapp.form.validators.StudyTypesAreValid;
import ar.edu.itba.paw.webapp.form.validators.ValidDays;
import ar.edu.itba.paw.webapp.form.validators.ValidOpeningClosingHours;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class EditClinicForm {

    private static final int DAYS_OF_WEEK = 7;

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

    @Valid
    @ValidOpeningClosingHours
    private ClinicHoursForm clinicHoursForm;

    private String accepted_plans;

    public EditClinicForm(){
        clinicHoursForm = new ClinicHoursForm();
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

    public String getAccepted_plans() {
        return accepted_plans;
    }

    public void setAccepted_plans(String accepted_plans) {
        this.accepted_plans = accepted_plans;
    }

    public void setAccepted_plans(String[] accepted_plans) {
        this.accepted_plans = String.join(",", accepted_plans);
    }

    public String[] getAccepted_plans_List(){
        return this.accepted_plans.split(",");
    }

    public ClinicHoursForm getClinicHoursForm() {
        return clinicHoursForm;
    }

    public void setClinicHoursForm(ClinicHoursForm clinicHoursForm) {
        this.clinicHoursForm = clinicHoursForm;
    }
}
