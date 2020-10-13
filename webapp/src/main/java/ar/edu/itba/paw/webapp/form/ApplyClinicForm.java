package ar.edu.itba.paw.webapp.form;


import org.hibernate.validator.constraints.NotBlank;

import java.sql.Time;


public class ApplyClinicForm {

    @NotBlank
    private String name, telephone;
    private Integer[] available_studies;

    private String accepted_plans;

    private Integer[] open_days;

    private String[] opening_time;

    private String[] closing_time;


    public ApplyClinicForm() {

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

    public String[] getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String[] opening_time) {
        this.opening_time = opening_time;
    }

    public String[] getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String[] closing_time) {
        this.closing_time = closing_time;
    }
}
