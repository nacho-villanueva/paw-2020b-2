package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.webapp.form.PatientInfoForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatientInfoFormHasNotNullNameValidator implements ConstraintValidator<PatientInfoFormHasNotNullName, PatientInfoForm> {


    @Override
    public void initialize(PatientInfoFormHasNotNullName constraintAnnotation) {

    }

    @Override
    public boolean isValid(PatientInfoForm patientInfoForm, ConstraintValidatorContext constraintValidatorContext) {
        // returns true if:
        //  - patientInfoForm has exisitng Patient as true
        //  - patientInfoForm has not empty name
        return patientInfoForm.getExistingPatient() || !patientInfoForm.getName().isEmpty();
    }

    private boolean isEmpty(String name){
        return name == null || name.equals("");
    }
}
