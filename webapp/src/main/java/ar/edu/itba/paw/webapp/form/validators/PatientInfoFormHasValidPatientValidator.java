package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.services.PatientService;
import ar.edu.itba.paw.webapp.form.PatientInfoForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class PatientInfoFormHasValidPatientValidator implements ConstraintValidator<PatientInfoFormHasValidPatient, PatientInfoForm> {

    @Autowired
    private PatientService patientService;

    @Override
    public void initialize(PatientInfoFormHasValidPatient constraintAnnotation) {

    }

    @Override
    public boolean isValid(PatientInfoForm patientInfoForm, ConstraintValidatorContext constraintValidatorContext) {

        String email = patientInfoForm.getEmail();

        if(!patientInfoForm.getExistingPatient() || isEmpty(email))
            return true;

        // patient is present and mail is not null or empty

        Optional<Patient> patientOptional = patientService.findByEmail(email);

        return patientOptional.isPresent();
    }

    private boolean isEmpty(String email){
        return email == null || email.equals("");
    }
}
