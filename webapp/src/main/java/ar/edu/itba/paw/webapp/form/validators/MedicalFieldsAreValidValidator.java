package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.MedicalFieldService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MedicalFieldsAreValidValidator implements ConstraintValidator<MedicalFieldsAreValid, Integer[]> {

    @Autowired
    private MedicalFieldService medicalFieldService;

    @Override
    public void initialize(MedicalFieldsAreValid userNotExist) {

    }

    @Override
    public boolean isValid(Integer[] integers, ConstraintValidatorContext constraintValidatorContext) {
        for (Integer i : integers) {
            if(!medicalFieldService.findById(i).isPresent())
                return false;
        }
        return true;
    }
}
