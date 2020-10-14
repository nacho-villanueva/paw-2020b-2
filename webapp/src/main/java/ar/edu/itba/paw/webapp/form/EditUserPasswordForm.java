package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;
import ar.edu.itba.paw.webapp.form.validators.PasswordMatches;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;

public class EditUserPasswordForm {

    @Valid
    @PasswordMatches
    private PasswordField newPassword;

    @PasswordIsCorrect
    @NotBlank
    private String password;

    public EditUserPasswordForm(){
        newPassword = new PasswordField();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PasswordField getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(PasswordField newPassword) {
        this.newPassword = newPassword;
    }
}
