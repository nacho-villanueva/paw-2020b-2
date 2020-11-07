package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class PasswordIsCorrectValidator implements ConstraintValidator<PasswordIsCorrect, String> {

    @Autowired
    private UserService us;

    @Override
    public void initialize(PasswordIsCorrect passwordIsCorrect) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());

        return user.filter(user1 -> us.checkPassword(user1.getId(), s)).isPresent();

    }
}
