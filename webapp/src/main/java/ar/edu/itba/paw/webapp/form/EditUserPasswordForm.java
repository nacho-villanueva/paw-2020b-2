package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.FieldMatch;
import ar.edu.itba.paw.webapp.form.validators.PasswordIsCorrect;
import ar.edu.itba.paw.webapp.form.validators.UserNotExist;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "newPasswordRepeat", second = "newPassword", message = "Passwords don't match")
public class EditUserPasswordForm {

    @NotNull
    @Size(min = 6, max = 100)
    private String newPassword;

    @NotNull
    private String newPasswordRepeat;

    @PasswordIsCorrect
    @NotNull
    private String password;

    public EditUserPasswordForm(){

    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRepeat() {
        return newPasswordRepeat;
    }

    public void setNewPasswordRepeat(String newPasswordRepeat) {
        this.newPasswordRepeat = newPasswordRepeat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
