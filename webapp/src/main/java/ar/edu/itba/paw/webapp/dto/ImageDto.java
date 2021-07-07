package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.annotations.ByteArray;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.Base64;
import java.util.Objects;

public class ImageDto {

    // Constants
    public static final String CONTENT_TYPE = "image/*";

    // Variables
    @NotBlank(message="Please provide a content type for the image.")
    @Pattern(regexp="^image\\/[a-zA-Z+]*", message="Content type must match " + CONTENT_TYPE + ".")
    String contentType;

    @NotEmpty(message="Please provide an image.")
    @ByteArray(max=32000000, message="Please provide a valid byte array for the image.") // 30mb aprox, based on limit set on Model/Database
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
