package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "medical_fields")
public class MedicalField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "medical_fields_id_seq")
    @SequenceGenerator(sequenceName = "medical_fields_id_seq", name = "medical_fields_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    protected MedicalField() {
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
