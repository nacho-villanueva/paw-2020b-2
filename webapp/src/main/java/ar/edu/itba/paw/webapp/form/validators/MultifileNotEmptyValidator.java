package ar.edu.itba.paw.webapp.form.validators;

import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultifileNotEmptyValidator implements ConstraintValidator<MultifileNotEmpty, MultipartFile[]> {

    @Override
    public void initialize(MultifileNotEmpty userNotExist) {

    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext constraintValidatorContext) {
        if(files.length > 0){
            for (MultipartFile f: files) {
                if(f.getSize() != 0)
                    return true;
            }
        }
        return false;

    }
}
