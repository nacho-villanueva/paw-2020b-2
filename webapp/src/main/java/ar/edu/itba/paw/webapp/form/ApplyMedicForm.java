package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.MedicalField;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;

public class ApplyMedicForm {

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @NotBlank
    @Pattern(regexp = "\\+?[0-9]+")
    private String telephone;

    @NotBlank
    @Pattern(regexp = "[0-9a-zA-Z]+")
    private String licence_number;

    @NotEmpty
    private String[] known_fields;

    @NotNull
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String[] getKnown_fields() {
        return known_fields;
    }

    public void setKnown_fields(String[] known_fields) {
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
