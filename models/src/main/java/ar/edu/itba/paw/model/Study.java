package ar.edu.itba.paw.model;

public class Study {
    private int id;
    private String name;

    public Study(final int id, final String name) {
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
