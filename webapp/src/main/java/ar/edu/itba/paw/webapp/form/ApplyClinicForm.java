package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.webapp.form.validators.ValidDays;
import ar.edu.itba.paw.webapp.form.validators.ValidOpeningClosingHours;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ApplyClinicForm {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "\\+?[0-9]+")
    private String telephone;

    @NotEmpty
    private String[] available_studies;

    private String accepted_plans;

    @Valid
    @ValidOpeningClosingHours()
    private ClinicHoursForm clinicHoursForm;


    public ApplyClinicForm() {
        clinicHoursForm = new ClinicHoursForm();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String[] getAvailable_studies() {
        return available_studies;
    }

    public void setAvailable_studies(String[] available_studies) {
        this.available_studies = available_studies;
    }

    public void setAccepted_plans(String plans){
        this.accepted_plans = plans;
    }

    public String getAccepted_plans(){
        return this.accepted_plans;
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
