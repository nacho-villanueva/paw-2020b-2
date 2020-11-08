package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "medical_fields")
public class MedicalField implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "medical_fields_id_seq")
    @SequenceGenerator(sequenceName = "medical_fields_id_seq", name = "medical_fields_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    protected MedicalField() {
        //Just for hibernate
    }

    public MedicalField(final int id, final String name) {
        this(name);
        this.id = id;
    }

    public MedicalField(final String name) {
        this.name = name;
    }

    public Integer getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalField that = (MedicalField) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
