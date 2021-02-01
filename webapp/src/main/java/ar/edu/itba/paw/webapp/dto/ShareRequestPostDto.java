package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.validators.StudyTypeIdIsValid;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ShareRequestPostDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.share-request.v1";
    private static final String REQUEST_PATH = "share-requests";

    // Variables
    @NotBlank(message="ShareRequestPostDto.patientEmail.NotBlank")
    @Email(message="ShareRequestPostDto.patientEmail.Email")
    private String patientEmail;

    @NotNull(message="ShareRequestPostDto.studyTypeId.NotNull")
    @StudyTypeIdIsValid(message="ShareRequestPostDto.studyTypeId.StudyTypeIdIsValid")
    private Integer studyTypeId;

    // Constructors
    public ShareRequestPostDto() {
        // Use factory methods
    }

    // Getters&Setters
    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public Integer getStudyTypeId() {
        return studyTypeId;
    }

    public void setStudyTypeId(Integer studyTypeId) {
        this.studyTypeId = studyTypeId;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareRequestPostDto that = (ShareRequestPostDto) o;
        return Objects.equals(patientEmail, that.patientEmail) && Objects.equals(studyTypeId, that.studyTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientEmail, studyTypeId);
    }
}
