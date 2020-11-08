package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.MedicalFieldsAreValid;
import ar.edu.itba.paw.webapp.form.validators.MultipartFileIsImage;
import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditMedicForm {

    @NotNull
    @Size(min = 1, max = 100)
    private String full_name;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "\\+?[0-9]+")
    private String telephone;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]*")
    private String licence_number;

    @NotNull
    private String[] known_fields;

    @MultipartFileIsImage
    private MultipartFile identification;

    @PasswordIsCorrect
    @Size(min = 6, max = 100)
    private String password;

    public EditMedicForm(){

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

    public String getLicence_number() {
        return licence_number;
    }

    public void setLicence_number(String licence_number) {
        this.licence_number = licence_number;
    }

    public String[] getKnown_fields() {
        return known_fields;
    }

    public void setKnown_fields(String[] known_fields) {
        this.known_fields = known_fields;
    }

    public MultipartFile getIdentification() {
        return identification;
    }

    public void setIdentification(MultipartFile identification) {
        this.identification = identification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
