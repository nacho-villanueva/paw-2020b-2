package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ArrayAsStringIsValidValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayAsStringIsValid {

    String message() default "Input String is not a valid array with size less than {max}.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    int max() default Integer.MAX_VALUE;

}
