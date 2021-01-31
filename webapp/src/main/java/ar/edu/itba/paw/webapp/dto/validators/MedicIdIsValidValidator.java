package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.services.MedicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class MedicIdIsValidValidator implements ConstraintValidator<MedicIdIsValid, Integer> {

    @Autowired
    private MedicService medicService;

    @Override
    public void initialize(MedicIdIsValid medicIdIsValid) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {

        if(id==null)
            return true;

        final Optional<Medic>  medicOptional = medicService.findByUserId(id);
        return medicOptional.map(Medic::isVerified).orElse(false);
    }
}
