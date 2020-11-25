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
    private String[] availableStudies;

    private String acceptedPlans;

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

    public String[] getAvailableStudies() {
        return availableStudies;
    }

    public void setAvailableStudies(String[] availableStudies) {
        this.availableStudies = availableStudies;
    }

    public void setAcceptedPlans(String plans){
        this.acceptedPlans = plans;
    }

    public String getAcceptedPlans(){
        return this.acceptedPlans;
    }

    public String[] getAcceptedPlansList(){
        return this.acceptedPlans.split(",");
    }


    public ClinicHoursForm getClinicHoursForm() {
        return clinicHoursForm;
    }

    public void setClinicHoursForm(ClinicHoursForm clinicHoursForm) {
        this.clinicHoursForm = clinicHoursForm;
    }
}
