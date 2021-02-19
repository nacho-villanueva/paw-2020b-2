package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Order;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.Objects;

public class OrderGetDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.order.v1";
    private static final String REQUEST_PATH = "orders";
    private static final String IDENTIFICATION_PATH = "identification";
    private static final String SHARED_WITH_PATH = "shared-with";
    private static final String RESULTS_PATH = "results";
    private static final String MEDIC_PATH = "medics";
    private static final String CLINIC_PATH = "clinics";
    private static final String STUDY_TYPE_PATH = "study-types";

    // Variables
    private String medic;

    private LocalDate date;

    private String clinic;

    private String patientName;

    private String patientEmail;

    private String studyType;

    private String description;

    private String identification;

    private String medicPlan;

    private String medicPlanNumber;

    private String sharedWith;

    private String results;

    private String url;

    // Constructors
    public OrderGetDto() {
        // Use factory methods
    }

    public OrderGetDto(Order order, String encodedUrlPath, UriInfo uriInfo) {
        this.date = order.getDate();
        this.patientName = order.getPatientName();
        this.patientEmail = order.getPatientEmail();
        this.description = order.getDescription();
        this.medicPlan = order.getPatientInsurancePlan();
        this.medicPlanNumber = order.getPatientInsuranceNumber();

        this.medic = uriInfo.getBaseUriBuilder().path(MEDIC_PATH).path(String.valueOf(order.getMedic().getUser().getId())).build().toString();
        this.clinic = uriInfo.getBaseUriBuilder().path(CLINIC_PATH).path(String.valueOf(order.getClinic().getUser().getId())).build().toString();
        this.studyType = uriInfo.getBaseUriBuilder().path(STUDY_TYPE_PATH).path(String.valueOf(order.getStudy().getId())).build().toString();

        this.identification = getUriBuilder(encodedUrlPath,uriInfo).path(IDENTIFICATION_PATH).build().toString();
        this.sharedWith = getUriBuilder(encodedUrlPath,uriInfo).path(SHARED_WITH_PATH).build().toString();
        this.results = getUriBuilder(encodedUrlPath,uriInfo).path(RESULTS_PATH).build().toString();
        this.url = getUriBuilder(encodedUrlPath,uriInfo).build().toString();
    }

    private UriBuilder getUriBuilder(String path ,UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(path);
    }

    // Getters&Setters
    public String getMedic() {
        return medic;
    }

    public void setMedic(String medic) {
        this.medic = medic;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getMedicPlan() {
        return medicPlan;
    }

    public void setMedicPlan(String medicPlan) {
        this.medicPlan = medicPlan;
    }

    public String getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(String sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
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
        OrderGetDto that = (OrderGetDto) o;
        return Objects.equals(medic, that.medic) && Objects.equals(date, that.date) && Objects.equals(clinic, that.clinic) && Objects.equals(patientName, that.patientName) && Objects.equals(patientEmail, that.patientEmail) && Objects.equals(studyType, that.studyType) && Objects.equals(description, that.description) && Objects.equals(identification, that.identification) && Objects.equals(medicPlan, that.medicPlan) && Objects.equals(medicPlanNumber, that.medicPlanNumber) && Objects.equals(sharedWith, that.sharedWith) && Objects.equals(results, that.results) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medic, date, clinic, patientName, patientEmail, studyType, description, identification, medicPlan, medicPlanNumber, sharedWith, results, url);
    }

    public String getMedicPlanNumber() {
        return medicPlanNumber;
    }

    public void setMedicPlanNumber(String medicPlanNumber) {
        this.medicPlanNumber = medicPlanNumber;
    }
}
