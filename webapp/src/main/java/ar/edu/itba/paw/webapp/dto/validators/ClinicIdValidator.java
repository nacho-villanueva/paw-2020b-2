package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.webapp.dto.annotations.ClinicId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ClinicIdValidator implements ConstraintValidator<ClinicId, Integer> {

    @Autowired
    private ClinicService clinicService;

    @Override
    public void initialize(ClinicId clinicId) {

    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer==null)
            return true;

        final Optional<Clinic> clinicOptional = clinicService.findByUserId(integer);
        return clinicOptional.map(Clinic::isVerified).orElse(false);
    }
}
