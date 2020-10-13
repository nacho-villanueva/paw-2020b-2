package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.model.ClinicHours;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Time;

public class ClinicHoursIsValidValidator implements ConstraintValidator<ClinicHoursIsValid, Object> {

    private int day;
    private String isOpen;
    private String openTime;
    private String closeTime;
    private String message;

    @Override
    public void initialize(final ClinicHoursIsValid constraintAnnotation) {
        this.day = constraintAnnotation.day();
        this.isOpen = constraintAnnotation.isOpen();
        this.openTime = constraintAnnotation.openTime();
        this.closeTime = constraintAnnotation.closeTime();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = false;
        try
        {

            final String isOpenObj = BeanUtils.getProperty(value, isOpen);
            final String openTimeObj = BeanUtils.getProperty(value, openTime);
            final String closeTimeObj = BeanUtils.getProperty(value, closeTime);

            // valid when closed (!isOpen) and when it is a valid Input
            valid = isOpenObj.equals("false") || ClinicHours.validInput(day, Time.valueOf(((!openTimeObj.equals(""))?openTimeObj:"00:00")+":00"),Time.valueOf(((!closeTimeObj.equals(""))?closeTimeObj:"24:00")+":00"));
        }
        catch (final Exception notValid)
        {
            // do nothing
        }

        if (!valid){
            context.disableDefaultConstraintViolation();
            //In the initialiaze method you get the errorMessage: constraintAnnotation.message();
            context.buildConstraintViolationWithTemplate(message).addNode(openTime).addConstraintViolation();
        }

        return valid;
    }
}
