package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.dto.annotations.StudyTypeId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class StudyTypeIdCollectionIsValidValidator implements ConstraintValidator<StudyTypeId, Collection<Integer>> {

    @Autowired
    private StudyTypeService studyTypeService;

    @Override
    public void initialize(StudyTypeId StudyTypeId) {

    }

    @Override
    public boolean isValid(Collection<Integer> ids, ConstraintValidatorContext constraintValidatorContext) {
        if(ids == null || ids.isEmpty())
            return true;

        for (Integer id:ids) {
            if(id==null) return false;
            if (!studyTypeService.findById(id).isPresent()) return false;
        }

        return true;
    }
}
