package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.MedicalField;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public class ApplyMedicForm {
    private String first_name, last_name;
    private String email;
    private String telephone, licence_number;
    private Integer[] known_fields;
    private MultipartFile identification;

    public ApplyMedicForm(){

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFullname(){
        return first_name + " " + last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer[] getKnown_fields() {
        return known_fields;
    }

    public void setKnown_fields(Integer[] known_fields) {
        this.known_fields = known_fields;
    }

    public String getLicence_number() {
        return licence_number;
    }

    public void setLicence_number(String licence_number) {
        this.licence_number = licence_number;
    }

    public MultipartFile getIdentification() {
        return identification;
    }

    public void setIdentification(MultipartFile identification) {
        this.identification = identification;
    }
}
