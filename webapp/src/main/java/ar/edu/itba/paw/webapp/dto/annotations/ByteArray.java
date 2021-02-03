package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.ByteArrayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;

@Constraint(validatedBy = ByteArrayValidator.class)
@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ByteArray {

    String message() default "Input array is not a valid byte array with size less than {max}.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    int max() default Integer.MAX_VALUE;

}
