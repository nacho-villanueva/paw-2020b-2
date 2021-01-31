package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.webapp.dto.ClinicHoursAvailabilityDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class TimeIntervalsInClinicHoursAvailabilityDtoAreValidValidator implements ConstraintValidator<TimeIntervalsAreValid, ClinicHoursAvailabilityDto> {

    @Override
    public void initialize(TimeIntervalsAreValid validAvailabilityHours) {

    }

    @Override
    public boolean isValid(ClinicHoursAvailabilityDto clinicHoursDto, ConstraintValidatorContext constraintValidatorContext) {

        int days  = ClinicHours.getDaysOfWeek();

        try{
            LocalTime[] fromValues = clinicHoursDto.getFromTimeAsLocalTime();
            LocalTime[] toValues = clinicHoursDto.getToTimeAsLocalTime();

            for(int i = 0; i < days; i++){
                LocalTime auxFrom = fromValues[i];
                LocalTime auxTo = toValues[i];

                if ( auxFrom!=null && auxTo!=null && auxFrom.compareTo(auxTo) >= 0)
                    return false;
            }
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
