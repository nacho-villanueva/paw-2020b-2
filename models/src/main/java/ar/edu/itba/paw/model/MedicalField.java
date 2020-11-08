package ar.edu.itba.paw.model;

import java.io.Serializable;
import java.util.Objects;

public class MedicalField implements Serializable {
    private int id;
    private String name;

    public MedicalField() {

    }

    public MedicalField(final int id, final String name) {
        this.id = id;
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
