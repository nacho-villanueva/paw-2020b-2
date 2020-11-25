package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatientMailIsValidValidator implements ConstraintValidator<PatientMailIsValid, String> {

    @Autowired
    private PatientService patientService;

    @Override
    public void initialize(PatientMailIsValid patientMailIsValid) {

    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if(string==null)
            return false;

        return patientService.findByEmail(string).isPresent();
    }
}
