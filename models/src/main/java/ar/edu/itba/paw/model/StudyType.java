package ar.edu.itba.paw.model;

import java.io.Serializable;
import java.util.Objects;

public class StudyType implements Serializable {
    private int id;
    private String name;

    public StudyType() {

    }

    public StudyType(final int id, final String name) {
        this.id = id;
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
