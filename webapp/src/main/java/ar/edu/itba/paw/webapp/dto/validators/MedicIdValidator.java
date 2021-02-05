package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.webapp.dto.annotations.MedicId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class MedicIdValidator implements ConstraintValidator<MedicId, Integer> {

    @Autowired
    private MedicService medicService;

    @Override
    public void initialize(MedicId medicId) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {

        if(id==null)
            return true;

        final Optional<Medic>  medicOptional = medicService.findByUserId(id);
        return medicOptional.map(Medic::isVerified).orElse(false);
    }
}
