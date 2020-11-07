package ar.edu.itba.paw.webapp.form.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
