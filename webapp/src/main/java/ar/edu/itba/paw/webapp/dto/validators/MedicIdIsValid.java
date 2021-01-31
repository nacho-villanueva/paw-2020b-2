package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {MedicIdIsValidValidator.class, MedicIdCollectionIsValidValidator.class})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MedicIdIsValid {

    String message() default "Must be a valid medic's email";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
