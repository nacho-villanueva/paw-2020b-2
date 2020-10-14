package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.webapp.form.ClinicHoursForm;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidOpeningClosingHoursValidator implements ConstraintValidator<ValidOpeningClosingHours, ClinicHoursForm> {


    @Override
    public void initialize(ValidOpeningClosingHours constraintAnnotation) {

    }

    @Override
    public boolean isValid(ClinicHoursForm hours, ConstraintValidatorContext constraintValidatorContext) {
        String[] openingValues = hours.getOpening_time();
        String[] closingValues = hours.getClosing_time();
        Integer[] daysValues = hours.getOpen_days();

        for (Integer d: daysValues) {
            if(isEmpty(openingValues[d]) || isEmpty(closingValues[d]))
                return false;
        }

        if(openingValues.length != closingValues.length)
            return false;

        for(int i = 0; i < openingValues.length; i++){
            if(!isEmpty(openingValues[i]) && !isEmpty(closingValues[i])) {
                if (!isValidHour(openingValues[i]) || !isValidHour(closingValues[i]))
                    return false;
                if (compareTime(openingValues[i], closingValues[i]) <= 0)
                    return false;
            }
        }
        return true;
    }

    private boolean isValidHour(String hourStr){

        if(!hourStr.contains(":"))
            return false;

        String[] time = hourStr.split(":");

        if(time.length!=2)
            return false;

        int hour = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        return hour >= 0 && hour <= 23 && minutes >= 0 && minutes <= 59;
    }

    private int compareTime(String tA, String tB){
        String[] timeA = tA.split(":");
        int hourA = Integer.parseInt(timeA[0]);
        int minutesA = Integer.parseInt(timeA[1]);

        String[] timeB = tB.split(":");
        int hourB = Integer.parseInt(timeB[0]);
        int minutesB = Integer.parseInt(timeB[1]);

        if(hourB - hourA != 0){
            return hourB - hourA;
        } else {
            return minutesB - minutesA;
        }
    }

    private boolean isEmpty(String hour){
        return hour == null || hour.equals("");
    }
}
