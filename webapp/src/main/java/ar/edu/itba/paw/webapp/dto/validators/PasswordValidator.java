package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.annotations.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 100;

    @Override
    public void initialize(Password password) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        boolean isValid = true;
        if(isEmpty(s) || s.length() < PASSWORD_MIN_LENGTH || s.length() > PASSWORD_MAX_LENGTH) {
            isValid = false;
        }
        return isValid;
    }
}
