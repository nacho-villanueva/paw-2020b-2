package ar.edu.itba.paw.webapp.dto;


import ar.edu.itba.paw.webapp.dto.annotations.Password;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

public class UserCredentials {

    @NotNull(message = "Username cannot be empty.")
    @Email(message = "Username must be a valid email.")
    private String username;

    @NotNull(message = "Password cannot be empty.")
    @Password(message = "Password must be between 8 and 100 characters.")
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
