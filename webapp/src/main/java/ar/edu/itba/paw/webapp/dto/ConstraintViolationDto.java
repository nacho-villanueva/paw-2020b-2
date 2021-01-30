package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;
import java.util.Objects;

public class ConstraintViolationDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.ValidationError.v1";

    private String property;
    private String message;

    public ConstraintViolationDto() {
        // Use factory method
    }
    
    public ConstraintViolationDto(ConstraintViolation<?> violation, String message){
        String auxProperty = violation.getPropertyPath().toString();
        if(auxProperty == null || auxProperty.trim().length()==0)
            auxProperty = null;

        this.property = auxProperty;
        this.message = message;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintViolationDto that = (ConstraintViolationDto) o;
        return Objects.equals(property, that.property) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, message);
    }
}
