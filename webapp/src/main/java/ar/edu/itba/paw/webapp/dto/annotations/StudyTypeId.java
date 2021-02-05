package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.StudyTypeIdCollectionIsValidValidator;
import ar.edu.itba.paw.webapp.dto.validators.StudyTypeIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {StudyTypeIdValidator.class, StudyTypeIdCollectionIsValidValidator.class})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StudyTypeId {

    String message() default "Must be a valid study type id";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
