package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.PasswordMatches;
import ar.edu.itba.paw.webapp.form.validators.UserNotExist;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterUserForm {

    @UserNotExist
    @Email
    private String email;

    @Valid
    @PasswordMatches
    private PasswordField passwordField;

    public RegisterUserForm(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordField password) {
        this.passwordField = password;
    }
}
