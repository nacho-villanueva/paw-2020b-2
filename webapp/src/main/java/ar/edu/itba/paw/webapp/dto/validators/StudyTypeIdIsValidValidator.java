package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.services.StudyTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StudyTypeIdIsValidValidator implements ConstraintValidator<StudyTypeIdIsValid, Integer> {

    @Autowired
    private StudyTypeService studyTypeService;

    @Override
    public void initialize(StudyTypeIdIsValid userNotExist) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null)
            return true;
        return studyTypeService.findById(id).isPresent();
    }
}
