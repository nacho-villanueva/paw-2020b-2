package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.OrderFilterDto;
import ar.edu.itba.paw.webapp.dto.annotations.TimeIntervals;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class TimeIntervalsInOrderFilterDtoValidator implements ConstraintValidator<TimeIntervals, OrderFilterDto> {

    @Override
    public void initialize(TimeIntervals TimeIntervals) {

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
