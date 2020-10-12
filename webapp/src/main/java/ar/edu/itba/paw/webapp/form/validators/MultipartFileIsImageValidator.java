package ar.edu.itba.paw.webapp.form.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class MultipartFileIsImageValidator implements ConstraintValidator<MultipartFileIsImage, MultipartFile> {


    @Override
    public void initialize(MultipartFileIsImage userNotExist) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if(multipartFile.isEmpty())
            return true;

        try {
            byte[] testIfCanRetrieveContent = multipartFile.getBytes();
        } catch (IOException e) {
            return false;
        }

        return multipartFile.getContentType().contains("image/");
    }
}
