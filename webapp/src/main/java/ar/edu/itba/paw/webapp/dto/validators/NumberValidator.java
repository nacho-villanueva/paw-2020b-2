package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.annotations.Number;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.ws.rs.core.Response;

public class NumberValidator implements ConstraintValidator<Number,String> {
    @Override
    public void initialize(Number number) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Integer.parseInt(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
