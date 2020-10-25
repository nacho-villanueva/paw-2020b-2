package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "medical_studies")
public class StudyType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_studies_id_seq")
    @SequenceGenerator(sequenceName = "medical_studies_id_seq", name = "medical_studies_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "name",nullable = false)
    private String name;

    /* package */ StudyType() {
        //Just for hibernate
    }

    public StudyType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public StudyType(String name) {
        this.name = name;
    }

    public int getId() {
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
}
