package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.ClinicIdCollectionValidator;
import ar.edu.itba.paw.webapp.dto.validators.ClinicIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {ClinicIdValidator.class, ClinicIdCollectionValidator.class})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClinicId {

    String message() default "Selected Clinic doesn't exist";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
