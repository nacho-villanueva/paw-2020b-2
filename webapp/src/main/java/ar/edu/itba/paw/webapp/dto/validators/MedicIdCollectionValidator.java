package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.webapp.dto.annotations.MedicId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Optional;

public class MedicIdCollectionValidator implements ConstraintValidator<MedicId, Collection<Integer>> {

    @Autowired
    private MedicService medicService;

    @Override
    public void initialize(MedicId medicId) {

    }

    @Override
    public boolean isValid(Collection<Integer> ids, ConstraintValidatorContext constraintValidatorContext) {

        if (ids == null || ids.isEmpty())
            return true;

        for (Integer id : ids) {
            if (id != null) {
                Optional<Medic> medicOptional = medicService.findByUserId(id);
                if (!medicOptional.isPresent() || !medicOptional.get().isVerified()) return false;
            }
        }

        return true;
    }
}
