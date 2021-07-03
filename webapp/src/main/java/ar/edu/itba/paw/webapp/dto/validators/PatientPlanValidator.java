package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.MedicPlanDto;
import ar.edu.itba.paw.webapp.dto.PatientPlanDto;
import ar.edu.itba.paw.webapp.dto.annotations.PatientPlan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

public class PatientPlanValidator implements ConstraintValidator<PatientPlan, PatientPlanDto> {

    @Override
    public void initialize(PatientPlan medicPlan) {

    }

    @Override
    public boolean isValid(PatientPlanDto patientPlanDto, ConstraintValidatorContext constraintValidatorContext) {

        if(patientPlanDto == null)
            return true;

        MedicPlanDto planDto = patientPlanDto.getPlan();
        boolean planIsEmpty = planDto == null || planDto.getName() == null || planDto.getName().trim().length() <= 0;
        boolean numberIsEmpty = isEmpty(patientPlanDto.getNumber());

        // return true when both are empty or both aren't empty (or the dto itself is null)
        return !(planIsEmpty && !numberIsEmpty);
    }
}
