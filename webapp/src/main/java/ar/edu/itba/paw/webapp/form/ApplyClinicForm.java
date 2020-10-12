package ar.edu.itba.paw.webapp.form;


import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ApplyClinicForm {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "\\+?[0-9]+")
    private String telephone;

    @NotEmpty
    private Integer[] available_studies;

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
}
