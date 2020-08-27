package ar.edu.itba.paw.model;

public class User {
    private long id;
    private String username;

    public User(final long id, final String username) {
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
}
