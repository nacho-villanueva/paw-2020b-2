package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.StudyType;
import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class StudyTypeDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.study-type.v1";
    public static final String REQUEST_PATH = "study-types";
    private static final String CLINICS_PATH = "clinics";

    // Variables
    private Integer id;

    @NotBlank(message = "StudyTypeDto.name.NotBlank")
    private String name;

    private String url;

    private String clinics;

    // Constructors
    public StudyTypeDto() {
        // Use factory method
    }

    public StudyTypeDto(final StudyType studyType, final UriInfo uriInfo){
        this.id = studyType.getId();
        this.name = studyType.getName();

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(studyType.getId().toString());
        this.url = uriBuilder.build().toString();
        this.clinics = uriBuilder.path(CLINICS_PATH).build().toString();
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

    public String getClinics() {
        return clinics;
    }

    public void setClinics(String clinics) {
        this.clinics = clinics;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyTypeDto that = (StudyTypeDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(url, that.url) && Objects.equals(clinics, that.clinics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, clinics);
    }
}
