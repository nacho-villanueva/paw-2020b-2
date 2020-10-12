package ar.edu.itba.paw.webapp.form.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MedicalFieldsAreValidValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MedicalFieldsAreValid {

    String message() default "Invalid Medical Fields included";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
