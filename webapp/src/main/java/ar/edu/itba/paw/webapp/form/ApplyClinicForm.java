package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.StudyType;

import java.util.Collection;

public class ApplyClinicForm {
    private String name, telephone;
    private Collection<StudyType> available_studies;

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

    public Collection<StudyType> getAvailable_studies() {
        return available_studies;
    }

    public void setAvailable_studies(Collection<StudyType> available_studies) {
        this.available_studies = available_studies;
    }
}
