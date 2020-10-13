package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidClinicIdValidator implements ConstraintValidator<ValidClinicId, Integer> {

    @Autowired
    private ClinicService cs;

    @Override
    public void initialize(ValidClinicId userNotExist) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null)
            return false;
        return cs.findByUserId(id).isPresent();
    }
}
