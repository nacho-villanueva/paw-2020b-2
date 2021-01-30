package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.ClinicDayHoursDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class AvailabilityHoursInClinicDayHoursDtoAreValidValidator implements ConstraintValidator<AvailabilityHoursAreValid, ClinicDayHoursDto> {

    @Override
    public void initialize(AvailabilityHoursAreValid validAvailabilityHours) {

    }

    @Override
    public boolean isValid(ClinicDayHoursDto clinicDayHoursDto, ConstraintValidatorContext constraintValidatorContext) {

        LocalTime openTime = clinicDayHoursDto.getOpenTime();
        LocalTime closeTime = clinicDayHoursDto.getCloseTime();

        if(openTime==null || closeTime==null)
            return true;

        return openTime.compareTo(closeTime) < 0;
    }
}
