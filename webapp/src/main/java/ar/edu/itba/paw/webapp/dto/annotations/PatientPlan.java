package ar.edu.itba.paw.webapp.dto.annotations;

import ar.edu.itba.paw.webapp.dto.validators.PatientPlanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatientPlanValidator.class)
@Documented
public @interface PatientPlan {

    String message() default "Invalid Patient Medic Plan.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
