package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.LocaleValidator;
import ar.edu.itba.paw.webapp.dto.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocaleValidator.class)
@Documented
public @interface Locale {
    String message() default "Locale is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
