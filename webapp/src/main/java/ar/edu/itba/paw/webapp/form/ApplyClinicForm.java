package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.webapp.form.validators.ValidDays;
import ar.edu.itba.paw.webapp.form.validators.ValidOpeningClosingHours;
import org.hibernate.validator.constraints.NotBlank;


public class ApplyClinicForm {

    @NotBlank
    private String name, telephone;
    private Integer[] available_studies;

    private String accepted_plans;

    @ValidDays
    private Integer[] open_days;

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

    public Integer[] getAvailable_studies() {
        return available_studies;
    }

    public void setAvailable_studies(Integer[] available_studies) {
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

    public Integer[] getOpen_days() {
        return open_days;
    }

    public void setOpen_days(Integer[] open_days) {
        this.open_days = open_days;
    }


    public ClinicHoursForm getClinicHoursForm() {
        return clinicHoursForm;
    }

    public void setClinicHoursForm(ClinicHoursForm clinicHoursForm) {
        this.clinicHoursForm = clinicHoursForm;
    }
}
