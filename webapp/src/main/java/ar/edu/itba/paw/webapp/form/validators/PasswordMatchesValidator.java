package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.form.PasswordField;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordField> {


    @Override
    public void initialize(PasswordMatches userNotExist) {

    }

    @Override
    public boolean isValid(PasswordField passwords, ConstraintValidatorContext constraintValidatorContext) {
        return passwords.getPassword().equals(passwords.getConfirmPassword());
    }
}
