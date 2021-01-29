package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.services.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ClinicIdIsValidValidator implements ConstraintValidator<ClinicIdIsValid, Integer> {

    @Autowired
    private ClinicService clinicService;

    @Override
    public void initialize(ClinicIdIsValid clinicIdIsValid) {

    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer==null)
            return true;

        Optional<Clinic> clinicOptional = clinicService.findByUserId(integer);

        return clinicOptional.isPresent() && clinicOptional.get().isVerified();
    }
}
