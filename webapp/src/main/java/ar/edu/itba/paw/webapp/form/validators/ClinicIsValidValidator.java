package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClinicIsValidValidator implements ConstraintValidator<ClinicIsValid, Integer> {

    @Autowired
    private ClinicService clinicService;

    @Override
    public void initialize(ClinicIsValid clinicIsValid) {

    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer==null)
            return false;

        return clinicService.findByUserId(integer).isPresent();
    }
}
