package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.NumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD,PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumberValidator.class)
@Documented
public @interface Number {
    String message() default "Not a number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
