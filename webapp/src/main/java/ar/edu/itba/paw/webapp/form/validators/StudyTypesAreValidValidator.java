package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.StudyTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StudyTypesAreValidValidator implements ConstraintValidator<StudyTypesAreValid, Integer[]> {

    @Autowired
    private StudyTypeService studyTypeService;

    @Override
    public void initialize(StudyTypesAreValid userNotExist) {

    }

    @Override
    public boolean isValid(Integer[] integers, ConstraintValidatorContext constraintValidatorContext) {
        for (Integer i : integers) {
            if(!studyTypeService.findById(i).isPresent())
                return false;
        }
        return true;
    }
}
