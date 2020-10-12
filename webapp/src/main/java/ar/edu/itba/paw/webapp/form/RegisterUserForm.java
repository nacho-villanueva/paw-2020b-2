package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.UserNotExist;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterUserForm {

    @UserNotExist
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    public RegisterUserForm(){

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
}
