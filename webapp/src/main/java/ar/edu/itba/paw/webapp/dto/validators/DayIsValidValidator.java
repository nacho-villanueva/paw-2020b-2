package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DayIsValidValidator implements ConstraintValidator<DayIsValid, Integer> {


    @Override
    public void initialize(DayIsValid daysAreValid) {

    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {

        if(integer==null)
            return true;

        return integer >= 0 && integer < 6;
    }
}
