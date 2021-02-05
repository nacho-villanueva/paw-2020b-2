package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.webapp.dto.annotations.ClinicId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Optional;

public class ClinicIdCollectionValidator implements ConstraintValidator<ClinicId, Collection<Integer>> {

    @Autowired
    private ClinicService clinicService;

    @Override
    public void initialize(ClinicId clinicId) {

    }

    @Override
    public boolean isValid(Collection<Integer> ids, ConstraintValidatorContext constraintValidatorContext) {
        if(ids==null || ids.isEmpty())
            return true;

        for (Integer id:ids) {
            if(id==null) return false;
            Optional<Clinic> clinicOptional = clinicService.findByUserId(id);
            if(!clinicOptional.isPresent() || !clinicOptional.get().isVerified()) return false;
        }

        return true;
    }
}
