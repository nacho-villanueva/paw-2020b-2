package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.MedicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MedicIdIsValidValidator implements ConstraintValidator<MedicIdIsValid, Integer> {

    @Autowired
    private MedicService medicService;

    @Override
    public void initialize(MedicIdIsValid medicIdIsValid) {

    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer==null)
            return false;

        return medicService.findByUserId(integer).isPresent();
    }
}
