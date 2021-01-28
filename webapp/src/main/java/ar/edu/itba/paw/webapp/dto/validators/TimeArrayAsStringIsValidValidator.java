package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeArrayAsStringIsValidValidator implements ConstraintValidator<TimeArrayAsStringIsValid, String> {

    @Override
    public void initialize(TimeArrayAsStringIsValid arrayAsStringIsValid) {
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
            if(!isValidTime(string))
                return false;
        }

        return true;
    }

    private boolean isValidTime(String string){

        if(string == null || string.trim().length()==0)
            return true;

        String s = string.trim().toLowerCase();

        if(!s.contains(":"))
            return false;

        String[] time = s.split(":");

        if(time.length!=2)
            return false;

        int hour;
        int minutes;
        try {
            hour = Integer.parseInt(time[0]);
            minutes = Integer.parseInt(time[1]);
        }catch (Error e){
            return false;
        }
        return hour >= 0 && hour <= 24 && minutes >= 0 && minutes <= 59;
    }
}
