package ar.edu.itba.paw.webapp.form.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PatientInfoFormHasValidEmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PatientInfoFormHasValidEmail {
    String message() default "Email field has invalid email format";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
