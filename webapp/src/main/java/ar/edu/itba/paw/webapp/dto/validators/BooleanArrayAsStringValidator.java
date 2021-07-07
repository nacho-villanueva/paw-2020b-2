package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.annotations.BooleanArrayAsString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BooleanArrayAsStringValidator implements ConstraintValidator<BooleanArrayAsString, String> {

    @Override
    public void initialize(BooleanArrayAsString arrayAsStringIsValid) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s==null || s.length()==0)
            return true;

        String aux = s.trim();
        if(aux.length()==0)
            return true;

        String[] strings = aux.split(",");

        for(String string : strings){
            if(!isTrue(string) && !isFalse(string))
                return false;
        }

        return true;
    }

    private boolean isTrue(String string){

        if(string == null || string.trim().length()==0)
            return false;

        String s = string.trim().toLowerCase();
        return s.equals("true") || s.equals("1");
    }

    private boolean isFalse(String string){

        if(string == null || string.trim().length()==0)
            return true;

        String s = string.trim().toLowerCase();
        return s.equals("false") || s.equals("0");
    }
}
