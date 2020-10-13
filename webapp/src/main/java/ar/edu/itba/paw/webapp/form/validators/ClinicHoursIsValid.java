package ar.edu.itba.paw.webapp.form.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ClinicHoursIsValidValidator.class)
@Documented
public @interface ClinicHoursIsValid
{
    String message() default "Invalid time values";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int day();
    String isOpen();
    String openTime();
    String closeTime();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        ClinicHoursIsValid[] value();
    }
}
