package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;
import ar.edu.itba.paw.webapp.form.validators.UserNotExist;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class EditUserEmailForm {

    @UserNotExist
    @Email
    @NotBlank
    private String email;

    @PasswordIsCorrect
    @Size(min = 6, max = 100)
    private String password;

    public EditUserEmailForm(){

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
