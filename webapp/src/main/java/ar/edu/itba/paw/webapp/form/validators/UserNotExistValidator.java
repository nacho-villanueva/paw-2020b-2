package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNotExistValidator implements ConstraintValidator<UserNotExist, String> {

    @Autowired
    private UserService us;

    @Override
    public void initialize(UserNotExist userNotExist) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !us.findByEmail(email).isPresent();
    }
}
