package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.ClinicDayHoursDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DaysAreValidValidator implements ConstraintValidator<DaysAreValid, Collection<ClinicDayHoursDto>> {

    @Override
    public void initialize(DaysAreValid dayAreValid) {

    }

    @Override
    public boolean isValid(Collection<ClinicDayHoursDto> clinicDayHoursDtos, ConstraintValidatorContext constraintValidatorContext) {
        Set<Integer> days = new HashSet<>();

        if(clinicDayHoursDtos==null)
            return true;

        for (ClinicDayHoursDto c : clinicDayHoursDtos) {
            Integer day = c.getDay();

            if(days.contains(day))
                return false;
            days.add(day);
        }
        return true;
    }


}
