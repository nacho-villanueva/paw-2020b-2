package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.ClinicDayHours;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClinicGetDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.clinic.v1";
    private static final String REQUEST_PATH = "clinics";
    private static final String AVAILABLE_STUDIES_PATH = "available-studies";
    private static final String ACCEPTED_PLANS_PATH = "accepted-plans";

    // Variables
    private UserGetDto user;

    private String name;

    private String telephone;

    private String availableStudies;

    private String acceptedPlans;

    private Collection<ClinicDayHoursDto> hours;

    private boolean verified;

    private String url;

    // Constructors
    public ClinicGetDto() {
        // Use factory method
    }

    public ClinicGetDto(Clinic clinic, UriInfo uriInfo){
        this.user = new UserGetDto(clinic.getUser(),uriInfo);
        this.name = clinic.getName();
        this.telephone = clinic.getTelephone();
        this.verified = clinic.isVerified();

        this.availableStudies = getUriBuilder(clinic,uriInfo).path(AVAILABLE_STUDIES_PATH).build().toString();
        this.acceptedPlans = getUriBuilder(clinic,uriInfo).path(ACCEPTED_PLANS_PATH).build().toString();

        Collection<ClinicDayHours> clinicDayHours = clinic.getHours().createClinicDayHoursCollection();
        this.hours = (clinicDayHours.stream().map(ClinicDayHoursDto::new).collect(Collectors.toList()));

        this.url = getUriBuilder(clinic,uriInfo).build().toString();
    }

    private UriBuilder getUriBuilder(Clinic clinic, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(clinic.getUser().getId().toString());
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAvailableStudies() {
        return availableStudies;
    }

    public void setAvailableStudies(String availableStudies) {
        this.availableStudies = availableStudies;
    }

    public String getAcceptedPlans() {
        return acceptedPlans;
    }

    public void setAcceptedPlans(String acceptedPlans) {
        this.acceptedPlans = acceptedPlans;
    }

    public Collection<ClinicDayHoursDto> getHours() {
        return hours;
    }

    public void setHours(Collection<ClinicDayHoursDto> hours) {
        this.hours = hours;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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
        ClinicGetDto that = (ClinicGetDto) o;
        return verified == that.verified && Objects.equals(user, that.user) && Objects.equals(name, that.name) && Objects.equals(telephone, that.telephone) && Objects.equals(availableStudies, that.availableStudies) && Objects.equals(acceptedPlans, that.acceptedPlans) && Objects.equals(hours, that.hours) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, name, telephone, availableStudies, acceptedPlans, hours, verified, url);
    }
}
