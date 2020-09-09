package ar.edu.itba.paw.model;

public class MedicalField {
    private int id;
    private String name;

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
}
