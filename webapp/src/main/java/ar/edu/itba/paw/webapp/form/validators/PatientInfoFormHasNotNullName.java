package ar.edu.itba.paw.webapp.form.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PatientInfoFormHasNotNullNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PatientInfoFormHasNotNullName {
    String message() default "Name field is empty";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
