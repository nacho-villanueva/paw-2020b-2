package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.Collection;

public class Medic {
    private int id;
    private String name;
    private String email;
    private String telephone;
    private String licence_number;
    private Collection<MedicalField> medical_fields;

    public Medic(final int id, final String name, final String email, final String telephone, final String licence_number, final Collection<MedicalField> medical_fields) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.licence_number = licence_number;
        this.medical_fields = medical_fields;
    }

    public Medic(final int id, final String name, final String email, final String telephone, final String licence_number) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.licence_number = licence_number;
        this.medical_fields = new ArrayList<>();
    }

    public void setMedical_fields(Collection<MedicalField> medical_fields) {
        this.medical_fields = medical_fields;
    }

    public int getId() {
        return id;
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

    public Collection<MedicalField> getMedical_fields() {
        return medical_fields;
    }
}
