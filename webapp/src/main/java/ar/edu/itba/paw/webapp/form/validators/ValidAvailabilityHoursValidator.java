package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.webapp.form.ClinicHoursForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidAvailabilityHoursValidator implements ConstraintValidator<ValidAvailabilityHours, ClinicHoursForm> {

    @Override
    public void initialize(ValidAvailabilityHours validAvailabilityHours) {

    }

    @Override
    public boolean isValid(ClinicHoursForm hours, ConstraintValidatorContext constraintValidatorContext) {
        String[] fromValues = hours.getOpening_time();
        String[] toValues = hours.getClosing_time();

        if(fromValues.length!=ClinicHours.getDaysOfWeek() || toValues.length!=ClinicHours.getDaysOfWeek())
            return false;

        for(int i = 0; i < ClinicHours.getDaysOfWeek(); i++){
            String auxFrom = (fromValues[i].isEmpty())?"00:00":fromValues[i];
            String auxTo = (toValues[i].isEmpty())?"23:59":toValues[i];

            if (!isValidHour(auxFrom) || !isValidHour(auxTo))
                return false;
            if (compareTime(auxFrom, auxTo) <= 0)
                return false;
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
        return hour >= 0 && hour <= 24 && minutes >= 0 && minutes <= 59;
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
