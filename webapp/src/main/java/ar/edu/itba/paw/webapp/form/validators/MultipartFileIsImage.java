package ar.edu.itba.paw.webapp.form.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MultipartFileIsImageValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartFileIsImage {

    String message() default "File is not Image";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
