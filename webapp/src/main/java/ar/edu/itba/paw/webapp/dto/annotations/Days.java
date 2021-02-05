package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.DaysValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DaysValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Days {

    String message() default "Invalid days.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
