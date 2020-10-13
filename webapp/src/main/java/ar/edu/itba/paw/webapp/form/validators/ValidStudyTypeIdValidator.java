package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.StudyTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidStudyTypeIdValidator implements ConstraintValidator<ValidStudyTypeId, Integer> {

    @Autowired
    private StudyTypeService sts;

    @Override
    public void initialize(ValidStudyTypeId userNotExist) {

    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null)
            return false;
        return sts.findById(id).isPresent();
    }
}
