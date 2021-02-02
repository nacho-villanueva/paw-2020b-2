package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.annotations.Locale;
import ar.edu.itba.paw.webapp.dto.annotations.Password;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

public class UserPostDto {

    public static final String CONTENT_TYPE = "application/vnd.user.v1";
    @NotNull @Email
    private String email;

    @NotNull @Password
    private String password;

    @Locale
    private String locale;

    public UserPostDto() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
