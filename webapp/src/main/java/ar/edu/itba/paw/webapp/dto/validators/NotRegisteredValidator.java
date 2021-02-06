package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.annotations.NotRegistered;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

public class NotRegisteredValidator implements ConstraintValidator<NotRegistered, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(NotRegistered notRegistered) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(isEmpty(s)) {
            return true;
        }

        Optional<User> maybeUser = userService.findByEmail(s);

        return !maybeUser.isPresent();
    }
}
