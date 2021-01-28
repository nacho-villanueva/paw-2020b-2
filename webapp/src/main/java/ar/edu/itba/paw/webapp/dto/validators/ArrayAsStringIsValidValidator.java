package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArrayAsStringIsValidValidator implements ConstraintValidator<ArrayAsStringIsValid, String> {

    private Integer max;

    @Override
    public void initialize(ArrayAsStringIsValid arrayAsStringIsValid) {
        this.max = arrayAsStringIsValid.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s==null || s.length()==0)
            return true;

        String aux = s.trim();
        if(aux.length()==0)
            return true;

        if(max==null)
            return true;

        String[] strings = aux.split(",");

        return strings.length <= max;
    }
}
