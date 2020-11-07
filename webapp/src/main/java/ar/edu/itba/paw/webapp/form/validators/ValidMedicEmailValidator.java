package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ValidMedicEmailValidator implements ConstraintValidator<ValidMedicEmail, String> {

    @Autowired
    private UserService us;

    @Override
    public void initialize(ValidMedicEmail clinicIsValid) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        final Optional<User> optionalUser = us.findByEmail(email);
        return optionalUser.map(User::isMedic).orElse(false);
    }
}
