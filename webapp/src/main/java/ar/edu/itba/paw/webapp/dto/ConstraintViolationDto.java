package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;
import java.util.Objects;

public class ConstraintViolationDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.ValidationError.v1";

    private String property;
    private String code;
    private String message;

    public ConstraintViolationDto() {
        // Use factory method
    }

    public ConstraintViolationDto(ConstraintViolation<?> violation, String message, String code) {
        String propertyPath = violation.getPropertyPath().toString();

        String property;
        if(propertyPath == null || propertyPath.trim().length()==0 || !propertyPath.contains(".")){
            property = null;
        }
        else{
            property = propertyPath
                    .replaceFirst("[a-zA-Z]*\\.","")
                    .replaceFirst("[a-zA-Z0-9]*\\.","");

            if(property.matches("^arg[0-9]+"))
                property = null;
        }

        this.property = property;
        this.code = code;

        final String SEPARATOR = "!!";
        if(property == null){
            if(message.contains(SEPARATOR)){
                String[] substrings = message.split(SEPARATOR,2);
                this.property = (substrings[0].equals(""))?null:substrings[0];
                this.message = (substrings[1].equals(""))?null:substrings[1];
            } else {
                this.message = message;
            }
        }else if(code.equals("other")){
            this.message = message;
        } else{
            this.message = null;
        }
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintViolationDto that = (ConstraintViolationDto) o;
        return Objects.equals(property, that.property) && Objects.equals(code, that.code) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, code, message);
    }
}
