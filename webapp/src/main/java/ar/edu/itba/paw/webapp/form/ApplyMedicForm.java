package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ApplyMedicForm {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\+?[0-9]+")
    private String telephone;

    @NotBlank
    @Pattern(regexp = "[0-9a-zA-Z]+")
    private String licenceNumber;

    @NotEmpty
    private String[] knownFields;

    @NotNull
    private MultipartFile identification;

    public ApplyMedicForm(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullname(){
        return firstName + " " + lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String[] getKnownFields() {
        return knownFields;
    }

    public void setKnownFields(String[] knownFields) {
        this.knownFields = knownFields;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public MultipartFile getIdentification() {
        return identification;
    }

    public void setIdentification(MultipartFile identification) {
        this.identification = identification;
    }
}
