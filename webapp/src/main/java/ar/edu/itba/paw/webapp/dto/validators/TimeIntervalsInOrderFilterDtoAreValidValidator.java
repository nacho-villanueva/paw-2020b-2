package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.ClinicDayHoursDto;
import ar.edu.itba.paw.webapp.dto.OrderFilterDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeIntervalsInOrderFilterDtoAreValidValidator implements ConstraintValidator<TimeIntervalsAreValid, OrderFilterDto> {

    @Override
    public void initialize(TimeIntervalsAreValid validAvailabilityHours) {

    }

    @Override
    public boolean isValid(OrderFilterDto orderFilterDto, ConstraintValidatorContext constraintValidatorContext) {

        LocalDate beforeTime = orderFilterDto.getFromDate();
        LocalDate afterTime = orderFilterDto.getToDate();

        if(beforeTime==null || afterTime==null)
            return true;

        return beforeTime.compareTo(afterTime) < 0;
    }
}
