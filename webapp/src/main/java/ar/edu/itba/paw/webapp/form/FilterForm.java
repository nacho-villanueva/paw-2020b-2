package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;

public class FilterForm {

    @Email
    private String patient_email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    @Min(-1)
    private Integer study_id, clinic_id, medic_id;

    public FilterForm() {
    }

    public FilterForm(Integer study_id, Integer clinic_id, Integer medic_id, String patient_email, String date){
        this.date = date;
        this.patient_email = patient_email;
        this.medic_id = medic_id;
        this.clinic_id = clinic_id;
        this.study_id = study_id;
    }

    public String getPatient_email() { return patient_email; }
    public void setPatient_email(String s){
        patient_email = s;
    }

    public String getDate(){ return date; }
    public void setDate(String s){
        date = s;
    }

    public Integer getStudy_id() {
        return study_id;
    }

    public void setStudy_id(Integer study_id) {
        this.study_id = study_id;
    }

    public Integer getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(Integer clinic_id) {
        this.clinic_id = clinic_id;
    }

    public Integer getMedic_id() {
        return medic_id;
    }

    public void setMedic_id(Integer medic_id) {
        this.medic_id = medic_id;
    }
}
