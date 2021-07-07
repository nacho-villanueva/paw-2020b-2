package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.TimeIntervalsInClinicDayHoursDtoValidator;
import ar.edu.itba.paw.webapp.dto.validators.TimeIntervalsInClinicHoursAvailabilityDtoValidator;
import ar.edu.itba.paw.webapp.dto.validators.TimeIntervalsInOrderFilterDtoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {TimeIntervalsInClinicHoursAvailabilityDtoValidator.class, TimeIntervalsInClinicDayHoursDtoValidator.class, TimeIntervalsInOrderFilterDtoValidator.class})
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeIntervals {
    String message() default "Time intervals are not valid";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
