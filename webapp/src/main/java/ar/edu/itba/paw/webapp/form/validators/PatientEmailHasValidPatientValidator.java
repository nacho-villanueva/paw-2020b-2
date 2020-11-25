package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.services.PatientService;
import ar.edu.itba.paw.webapp.form.PatientInfoForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class PatientEmailHasValidPatientValidator implements ConstraintValidator<PatientEmailHasValidPatient, String> {

    @Autowired
    private PatientService patientService;

    @Override
    public void initialize(PatientEmailHasValidPatient constraintAnnotation) {

    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {

        if(isEmpty(string))
            return false;

        Optional<Patient> patientOptional = patientService.findByEmail(string);

        return patientOptional.isPresent();
    }

    private boolean isEmpty(String email){
        return email == null || email.equals("");
    }
}
