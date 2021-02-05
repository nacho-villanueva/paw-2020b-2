package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.IntegerSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IntegerSizeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerSize {

    String message() default "Input value is not within the acceptable range";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    int min() default 0;

    int max() default Integer.MAX_VALUE;
}
