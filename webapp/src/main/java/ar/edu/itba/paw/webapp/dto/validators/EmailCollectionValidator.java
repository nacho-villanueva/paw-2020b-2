package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.annotations.EmailCollection;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class EmailCollectionValidator implements ConstraintValidator<EmailCollection, Collection<String>> {

    @Override
    public void initialize(EmailCollection emailCollection) {

    }

    @Override
    public boolean isValid(Collection<String> emails, ConstraintValidatorContext constraintValidatorContext) {

        if(emails==null || emails.isEmpty())
            return true;

        EmailValidator emailValidator = new EmailValidator();

        for (String email:emails) {
            if(email==null) return false;
            if(!emailValidator.isValid(email,constraintValidatorContext)) return false;
        }

        return true;
    }
}
