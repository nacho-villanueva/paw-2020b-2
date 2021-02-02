package ar.edu.itba.paw.webapp.dto.validators;

import ar.edu.itba.paw.webapp.dto.annotations.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LocaleValidator implements ConstraintValidator<Locale, String> {

    private static final Set<String> ISO_LANGUAGES = new HashSet<String>
            (Arrays.asList(java.util.Locale.getISOLanguages()));
    private static final Set<String> ISO_COUNTRIES = new HashSet<String>
            (Arrays.asList(java.util.Locale.getISOCountries()));
    @Override
    public void initialize(Locale locale) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return ISO_LANGUAGES.contains(s) || ISO_COUNTRIES.contains(s);
    }
}
