package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.MedicPlanDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MedicPlanDtoIsCompleteAndValidValidator implements ConstraintValidator<MedicPlanDtoIsCompleteAndValid, MedicPlanDto> {

    @Override
    public void initialize(MedicPlanDtoIsCompleteAndValid medicPlanDtoIsCompleteAndValid) {

    }

    @Override
    public boolean isValid(MedicPlanDto medicPlanDtos, ConstraintValidatorContext constraintValidatorContext) {

        if(medicPlanDtos == null)
            return true;

        boolean planIsEmpty = isEmpty(medicPlanDtos.getPlan());
        boolean numberIsEmpty = isEmpty(medicPlanDtos.getNumber());

        // return true when both are empty or both aren't empty (or the dto itself is null)
        return (planIsEmpty && numberIsEmpty) || (!planIsEmpty && !numberIsEmpty);
    }

    private boolean isEmpty(String s){
        return s==null || s.trim().isEmpty();
    }
}
