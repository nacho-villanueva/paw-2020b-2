package ar.edu.itba.paw.model;

public class Patient {
    private int id;
    private String email;
    private String name;

    public Patient(final int id, final String email, final String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Patient(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() { return name; }
}
