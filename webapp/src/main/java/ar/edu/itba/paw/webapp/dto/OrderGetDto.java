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

    // Variables
    private MedicGetDto medic;

    private LocalDate date;

    private ClinicGetDto clinic;

    private String patientName;

    private String patientEmail;

    private StudyTypeDto studyType;

    private String description;

    private String identification;

    private MedicPlanDto medicPlan;

    private String sharedWith;

    private String results;

    private String url;

    // Constructors
    public OrderGetDto() {
        // Use factory methods
    }

    public OrderGetDto(Order order, String encodedUrlPath, UriInfo uriInfo) {
        this.medic = new MedicGetDto(order.getMedic(), uriInfo);
        this.date = order.getDate();
        this.clinic = new ClinicGetDto(order.getClinic(), uriInfo);
        this.patientName = order.getPatientName();
        this.patientEmail = order.getPatientEmail();
        this.studyType = new StudyTypeDto(order.getStudy(), uriInfo);
        this.description = order.getDescription();
        this.medicPlan = new MedicPlanDto(order.getPatientInsurancePlan(),order.getPatientInsuranceNumber());

        this.identification = getUriBuilder(encodedUrlPath,uriInfo).path(IDENTIFICATION_PATH).build().toString();
        this.sharedWith = getUriBuilder(encodedUrlPath,uriInfo).path(SHARED_WITH_PATH).build().toString();
        this.results = getUriBuilder(encodedUrlPath,uriInfo).path(RESULTS_PATH).build().toString();
        this.url = getUriBuilder(encodedUrlPath,uriInfo).build().toString();
    }

    private UriBuilder getUriBuilder(String path ,UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(path);
    }

    // Getters&Setters
    public MedicGetDto getMedic() {
        return medic;
    }

    public void setMedic(MedicGetDto medic) {
        this.medic = medic;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ClinicGetDto getClinic() {
        return clinic;
    }

    public void setClinic(ClinicGetDto clinic) {
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

    public StudyTypeDto getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyTypeDto studyType) {
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

    public MedicPlanDto getMedicPlan() {
        return medicPlan;
    }

    public void setMedicPlan(MedicPlanDto medicPlan) {
        this.medicPlan = medicPlan;
    }

    public String getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(String sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderGetDto that = (OrderGetDto) o;
        return Objects.equals(medic, that.medic) && Objects.equals(date, that.date) && Objects.equals(clinic, that.clinic) && Objects.equals(patientName, that.patientName) && Objects.equals(patientEmail, that.patientEmail) && Objects.equals(studyType, that.studyType) && Objects.equals(description, that.description) && Objects.equals(identification, that.identification) && Objects.equals(medicPlan, that.medicPlan) && Objects.equals(sharedWith, that.sharedWith) && Objects.equals(results, that.results) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medic, date, clinic, patientName, patientEmail, studyType, description, identification, medicPlan, sharedWith, results, url);
    }
}
