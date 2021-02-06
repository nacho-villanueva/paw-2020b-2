package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.DaysValidator;
import ar.edu.itba.paw.webapp.dto.validators.NotRegisteredValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = NotRegisteredValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface NotRegistered {
    String message() default "Account already registered.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
