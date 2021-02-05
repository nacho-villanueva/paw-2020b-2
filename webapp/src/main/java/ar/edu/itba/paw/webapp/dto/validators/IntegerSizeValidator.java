package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.annotations.IntegerSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerSizeValidator implements ConstraintValidator<IntegerSize, Integer> {

    int min = 0;
    int max = Integer.MAX_VALUE;

    @Override
    public void initialize(IntegerSize integerSize) {
        this.min = integerSize.min();
        this.max = integerSize.max();
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {

        if(integer==null)
            return true;

        return integer >= this.min && integer <= this.max;
    }
}
