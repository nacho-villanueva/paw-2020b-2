package ar.edu.itba.paw.webapp.form;

import javax.jws.soap.SOAPBinding;

public class RegisterUserForm {

    private Integer userType;
    private String email;
    private String password;
    private String confirmPassword;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
