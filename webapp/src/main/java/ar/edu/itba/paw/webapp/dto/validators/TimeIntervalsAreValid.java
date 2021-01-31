package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {TimeIntervalsInClinicHoursAvailabilityDtoAreValidValidator.class, TimeIntervalsInClinicDayHoursDtoAreValidValidator.class, TimeIntervalsInOrderFilterDtoAreValidValidator.class})
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeIntervalsAreValid {
    String message() default "Time intervals are not valid";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
