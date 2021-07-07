package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.dto.annotations.StudyTypeId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StudyTypeIdValidator implements ConstraintValidator<StudyTypeId, Integer> {

    @Autowired
    private StudyTypeService studyTypeService;

    @Override
    public void initialize(StudyTypeId studyTypeId) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null)
            return true;
        return studyTypeService.findById(id).isPresent();
    }
}
