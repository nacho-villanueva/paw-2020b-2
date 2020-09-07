package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.Collection;

public class Clinic {
    private int id;
    private String name;
    private String email;
    private String telephone;
    private Collection<StudyType> medical_studies;

    public Clinic(final int id, final String name, final String email, final String telephone, final Collection<StudyType> medical_studies) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.medical_studies = medical_studies;
    }

    public Clinic(final int id, final String name, final String email, final String telephone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.medical_studies = new ArrayList<>();
    }

    public void setMedical_studies(Collection<StudyType> medical_studies) {
        this.medical_studies = medical_studies;
    }

    public int getId() {
        return id;
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

    public Collection<StudyType> getMedical_studies() {
        return medical_studies;
    }
}
