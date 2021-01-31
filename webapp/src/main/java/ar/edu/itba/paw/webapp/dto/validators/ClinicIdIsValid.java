package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {ClinicIdIsValidValidator.class, ClinicIdCollectionIsValidValidator.class})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClinicIdIsValid {

    String message() default "Selected Clinic doesn't exist";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
