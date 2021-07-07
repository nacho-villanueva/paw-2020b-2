package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.MedicIdCollectionValidator;
import ar.edu.itba.paw.webapp.dto.validators.MedicIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {MedicIdValidator.class, MedicIdCollectionValidator.class})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MedicId {

    String message() default "Must be a valid medic's email";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
