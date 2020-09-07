package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.Collection;

public class Clinic {
    private String name;
    private String email;
    private String telephone;
    private Collection<String> medical_studies;

    public Clinic(final String name, final String email, final String telephone, final Collection<String> medical_studies) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.medical_studies = medical_studies;
    }

    public Clinic(final String name, final String email, final String telephone) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.medical_studies = new ArrayList<>();
    }

    public void setMedical_studies(Collection<String> medical_studies) {
        this.medical_studies = medical_studies;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Collection<String> getMedical_studies() {
        return medical_studies;
    }
}
