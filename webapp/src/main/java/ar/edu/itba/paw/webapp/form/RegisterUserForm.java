package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.UserNotExist;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterUserForm {

    private Integer userType;

    @UserNotExist
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    public RegisterUserForm(){

    }

    public Integer getUserType() {
        return userType;
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

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
