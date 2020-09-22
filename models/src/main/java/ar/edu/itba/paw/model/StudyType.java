package ar.edu.itba.paw.model;

public class StudyType {
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
}
