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
        }

        this.property = property;
        this.message = message;
        this.code = code;
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
