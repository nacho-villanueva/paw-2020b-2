package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ValidDaysValidator implements ConstraintValidator<ValidDays, Integer[]> {

    @Override
    public void initialize(ValidDays userNotExist) {

    }

    @Override
    public boolean isValid(Integer[] days, ConstraintValidatorContext constraintValidatorContext) {
        for (Integer d : days) {
            if(d < 0 || d > 6)
                return false;
        }
        return true;
    }
}
