package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.MedicPlanDto;
import ar.edu.itba.paw.webapp.dto.annotations.MedicPlan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

public class MedicPlanValidator implements ConstraintValidator<MedicPlan, MedicPlanDto> {

    @Override
    public void initialize(MedicPlan medicPlan) {

    }

    @Override
    public boolean isValid(MedicPlanDto medicPlanDto, ConstraintValidatorContext constraintValidatorContext) {

        if(medicPlanDto == null)
            return true;

        boolean planIsEmpty = isEmpty(medicPlanDto.getPlan());
        boolean numberIsEmpty = isEmpty(medicPlanDto.getNumber());

        // return true when both are empty or both aren't empty (or the dto itself is null)
        return !(planIsEmpty && !numberIsEmpty);
    }
}
