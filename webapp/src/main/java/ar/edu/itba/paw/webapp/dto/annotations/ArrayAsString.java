package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.ArrayAsStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ArrayAsStringValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayAsString {

    String message() default "Input String is not a valid array with size less than {max}.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    int max() default Integer.MAX_VALUE;

}
