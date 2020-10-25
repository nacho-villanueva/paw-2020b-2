package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "medical_fields")
public class MedicalField {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    /* package */ MedicalField() {
        //Just for hibernate
    }

    public MedicalField(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MedicalField(final String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
