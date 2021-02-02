package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.dto.constraintGroups.PatientPostGroup;
import ar.edu.itba.paw.webapp.dto.constraintGroups.PatientPutGroup;
import ar.edu.itba.paw.webapp.dto.validators.MedicPlanDtoIsCompleteAndValid;
import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class PatientDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.study-type.v1";
    private static final String REQUEST_PATH = "patients";

    // Variables
    private UserGetDto user;

    @NotBlank(message = "PatientDto.name.NotBlank", groups = {PatientPostGroup.class})
    private String name;

    @MedicPlanDtoIsCompleteAndValid(message = "PatientDto.medicPlan.MedicPlanDtoIsCompleteAndValid", groups = {PatientPostGroup.class, PatientPutGroup.class})
    private MedicPlanDto medicPlan;

    private String url;

    // Constructors
    public PatientDto() {
        // Use factory methods
    }

    public PatientDto(Patient patient, UriInfo uriInfo){
        this.user = UserGetDto.fromUser(patient.getUser(),uriInfo);
        this.name = patient.getName();
        this.medicPlan = new MedicPlanDto(patient.getMedicPlan(),patient.getMedicPlanNumber());
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(String.valueOf(patient.getUser().getId()));
        this.url = uriBuilder.build().toString();
    }

    // Getters&Setters
    public UserGetDto getUser() {
        return user;
    }

    public void setUser(UserGetDto user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MedicPlanDto getMedicPlan() {
        return medicPlan;
    }

    public void setMedicPlan(MedicPlanDto medicPlan) {
        this.medicPlan = medicPlan;
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
        PatientDto that = (PatientDto) o;
        return Objects.equals(user, that.user) && Objects.equals(name, that.name) && Objects.equals(medicPlan, that.medicPlan) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, name, medicPlan, url);
    }
}
