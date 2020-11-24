package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "medical_studies")
public class StudyType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "medical_studies_id_seq")
    @SequenceGenerator(sequenceName = "medical_studies_id_seq", name = "medical_studies_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "medicalStudies",fetch = FetchType.LAZY)
    private Collection<Clinic> clinics;

    protected StudyType() {
        //Just for hibernate
    }

    public StudyType(final int id, final String name) {
        this(name);
        this.id = id;
    }

    public StudyType(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyType studyType = (StudyType) o;
        return Objects.equals(name, studyType.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
