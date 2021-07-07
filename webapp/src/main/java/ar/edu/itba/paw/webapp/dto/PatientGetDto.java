package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Patient;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class PatientGetDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.patient.v1";
    public static final String REQUEST_PATH = "patients";

    // Variables
    private String user;

    private String name;

    private String medicPlan;

    private String medicPlanNumber;

    private String url;

    // Constructors
    public PatientGetDto() {
        // Use factory methods
    }

    public PatientGetDto(String user) {
        this.user = user;
    }

    public PatientGetDto(Patient patient, UriInfo uriInfo) {
        UriBuilder userUrl = uriInfo.getBaseUriBuilder().path(UserGetDto.REQUEST_PATH).path(String.valueOf(patient.getUser().getId()));
        this.user = userUrl.build().toString();
        this.name = patient.getName();
        if (patient.getMedicPlan() != null)
            this.medicPlan = uriInfo.getBaseUriBuilder().path(MedicPlanDto.REQUEST_PATH).path(String.valueOf(patient.getMedicPlan().getId())).build().toString();
        if (patient.getMedicPlanNumber() != null)
            this.medicPlanNumber = patient.getMedicPlanNumber();
        UriBuilder patientUrl = uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(String.valueOf(patient.getUser().getId()));
        this.url = patientUrl.build().toString();
    }

    // Getters&Setters
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicPlan() {
        return medicPlan;
    }

    public void setMedicPlan(String medicPlan) {
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
        PatientGetDto that = (PatientGetDto) o;
        return Objects.equals(user, that.user) && Objects.equals(name, that.name) && Objects.equals(medicPlan, that.medicPlan) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, name, medicPlan, url);
    }

    public String getMedicPlanNumber() {
        return medicPlanNumber;
    }

    public void setMedicPlanNumber(String medicPlanNumber) {
        this.medicPlanNumber = medicPlanNumber;
    }
}
