package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.annotations.Locale;
import ar.edu.itba.paw.webapp.dto.annotations.NotRegistered;
import ar.edu.itba.paw.webapp.dto.annotations.Password;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class UserPutDto {

    public static final String CONTENT_TYPE = "application/vnd.user.v1";
    @Email(message = "Please provide a valid email account.")
    @NotRegistered(message = "Email already used.")
    @NotNull
    private String email;

    @Password(message = "Password must be between 8 and 100 characters long.")
    @NotNull(message = "Please provide previous password")
    private String oldPassword;

    @Password(message = "Password must be between 8 and 100 characters long.")
    private String password;

    @Locale(message = "Please provide a Locale in the xx-XX format. For example, en-US")
    private String locale;

    public UserPutDto() {

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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
