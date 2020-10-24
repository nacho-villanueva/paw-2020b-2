package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UserNotExistValidator implements ConstraintValidator<UserNotExist, String> {

    @Autowired
    private UserService us;

    @Override
    public void initialize(UserNotExist userNotExist) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());

        return (user.isPresent() && user.get().getEmail().equals(email)) || !us.findByEmail(email).isPresent();
    }
}
