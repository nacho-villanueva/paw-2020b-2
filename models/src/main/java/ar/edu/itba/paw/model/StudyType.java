package ar.edu.itba.paw.model;

public class StudyType {
    private int id;
    private String name;

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
}
