package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.webapp.form.PatientInfoForm;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatientInfoFormHasValidEmailValidator implements ConstraintValidator<PatientInfoFormHasValidEmail, PatientInfoForm> {


    @Override
    public void initialize(PatientInfoFormHasValidEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(PatientInfoForm patientInfoForm, ConstraintValidatorContext constraintValidatorContext) {

        String email = patientInfoForm.getEmail();

        if(email.isEmpty())
            return false;

        //get the email validator used for the other validations
        EmailValidator emailValidator = new EmailValidator();
        return emailValidator.isValid(patientInfoForm.getEmail(),constraintValidatorContext);
    }

    private boolean isEmpty(String email){
        return email == null || email.equals("");
    }
}
