package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.constraintGroups.ResultGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class ResultPostDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.result.v1";
    private static final String REQUEST_PATH = "results";

    // Variables
    @Valid
    @NotNull(message = "ResultPostDto.file.NotNull", groups = {ResultGroup.class})
    private ImageDto file;

    @Valid
    @NotNull(message = "ResultPostDto.identification.NotNull", groups = {ResultGroup.class})
    private ImageDto identification;

    @NotBlank(message = "ResultPostDto.responsibleName.NotBlank", groups = {ResultGroup.class})
    private String responsibleName;

    @NotBlank(message = "ResultPostDto.responsibleLicence.NotBlank", groups = {ResultGroup.class})
    @Pattern(regexp = "[0-9a-zA-Z]*", message = "ResultPostDto.responsibleLicence.Pattern", groups = {ResultGroup.class})
    private String responsibleLicence;

    // Constructors
    public ResultPostDto() {
        // Use factory method
    }

    // Getters&Setters
    public ImageDto getFile() {
        return file;
    }

    public void setFile(ImageDto file) {
        this.file = file;
    }

    public ImageDto getIdentification() {
        return identification;
    }

    public void setIdentification(ImageDto identification) {
        this.identification = identification;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    public String getResponsibleLicence() {
        return responsibleLicence;
    }

    public void setResponsibleLicence(String responsibleLicence) {
        this.responsibleLicence = responsibleLicence;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultPostDto that = (ResultPostDto) o;
        return Objects.equals(file, that.file) && Objects.equals(identification, that.identification) && Objects.equals(responsibleName, that.responsibleName) && Objects.equals(responsibleLicence, that.responsibleLicence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, identification, responsibleName, responsibleLicence);
    }
}
