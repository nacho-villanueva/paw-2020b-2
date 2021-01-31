package ar.edu.itba.paw.webapp.dto;


import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

public class UserCredentials {

    @NotNull @Email
    private String username;
    @NotNull
    private String password;

    public UserCredentials() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
