package ar.edu.itba.paw.model;

public class Patient {
    private String email;
    private String name;

    public Patient(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() { return name; }
}
