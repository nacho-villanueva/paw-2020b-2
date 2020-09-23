package ar.edu.itba.paw.webapp.form.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserNotExistValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNotExist {

    String message() default "Email is already registered";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
