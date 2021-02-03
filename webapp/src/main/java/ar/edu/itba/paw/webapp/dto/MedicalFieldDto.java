package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.MedicalField;
import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class MedicalFieldDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.medical-field.v1";
    private static final String REQUEST_PATH = "medical-fields";

    // Variables
    private Integer id;

    @NotBlank(message = "Please provide a name for the medical field.")
    private String name;

    private String url;

    // Constructors
    public MedicalFieldDto() {
        // Use factory method
    }

    public MedicalFieldDto(final MedicalField medicalField, final UriInfo uriInfo){
        this.id = medicalField.getId();
        this.name = medicalField.getName();

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(medicalField.getId().toString());
        this.url = uriBuilder.build().toString();
    }

    // Getters&Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalFieldDto that = (MedicalFieldDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url);
    }
}
