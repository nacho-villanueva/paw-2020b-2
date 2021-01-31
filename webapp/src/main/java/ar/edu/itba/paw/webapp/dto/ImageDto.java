package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.constraintGroups.DefaultGroup;
import ar.edu.itba.paw.webapp.dto.validators.ByteArrayIsValid;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class ImageDto {

    // Constants
    public static final String CONTENT_TYPE = "image/*";

    // Variables
    @NotBlank(message="ImageDto.contentType.NotBlank",groups = {DefaultGroup.class})
    @Pattern(regexp="^image\\/[a-zA-Z+]*", message="ImageDto.contentType.Pattern",groups = {DefaultGroup.class})
    String contentType;

    @NotEmpty(message="ImageDto.image.NotEmpty",groups = {DefaultGroup.class})
    @ByteArrayIsValid(max=32000000, message="ImageDto.image.ByteArrayIsValid",groups = {DefaultGroup.class}) // 30mb aprox, based on limit set on Model
    String image;

    // Constructors
    public ImageDto() {
        // Use factory method
    }

    // Getters&Setters
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDto imageDto = (ImageDto) o;
        return Objects.equals(contentType, imageDto.contentType) && Objects.equals(image, imageDto.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, image);
    }

    // etc.
    public byte[] getImageAsByteArray(){

        byte[] array;
        try{
            array = Base64.getDecoder().decode(this.image.getBytes());
        }catch (Exception e){
            return new byte[0];
        }

        return array;
    }
}
