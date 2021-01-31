package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.services.MedicService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Optional;

public class MedicIdCollectionIsValidValidator implements ConstraintValidator<MedicIdIsValid, Collection<Integer>> {

    @Autowired
    private MedicService medicService;

    @Override
    public void initialize(MedicIdIsValid medicIdIsValid) {

    }

    @Override
    public boolean isValid(Collection<Integer> ids, ConstraintValidatorContext constraintValidatorContext) {

        if(ids==null || ids.isEmpty())
            return true;

        MedicIdIsValidValidator medicIdIsValidValidator = new MedicIdIsValidValidator();

        for (Integer id:ids) {
            if(id==null) return false;
            Optional<Medic> medicOptional = medicService.findByUserId(id);
            if(!medicOptional.isPresent() || !medicOptional.get().isVerified()) return false;
        }

        return true;
    }
}
