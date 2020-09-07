package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.Collection;

public class Medic {
    private String name;
    private String email;
    private String telephone;
    private String licence_number;
    private Collection<String> medical_fields;

    public Medic(final String name, final String email, final String telephone, final String licence_number, final Collection<String> medical_fields) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.licence_number = licence_number;
        this.medical_fields = medical_fields;
    }

    public Medic(final String name, final String email, final String telephone, final String licence_number) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.licence_number = licence_number;
        this.medical_fields = new ArrayList<>();
    }

    public void setMedical_fields(Collection<String> medical_fields) {
        this.medical_fields = medical_fields;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return  email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getLicence_number() {
        return licence_number;
    }

    public Collection<String> getMedical_fields() {
        return medical_fields;
    }
}
