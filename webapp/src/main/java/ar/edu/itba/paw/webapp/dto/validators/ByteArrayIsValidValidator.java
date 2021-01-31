package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.Charset;
import java.util.Base64;

public class ByteArrayIsValidValidator implements ConstraintValidator<ByteArrayIsValid, String> {

    private Integer max;

    @Override
    public void initialize(ByteArrayIsValid byteArrayIsValid) {
        this.max = byteArrayIsValid.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s==null || s.length()==0)
            return true;

        String aux = s.trim();
        if(aux.length()==0)
            return true;

        byte[] array;
        try{
            array = Base64.getDecoder().decode(aux.getBytes());
        }catch (Exception e){
            return false;
        }

        if(max==null)
            return true;

        return array.length <= max;
    }
}
