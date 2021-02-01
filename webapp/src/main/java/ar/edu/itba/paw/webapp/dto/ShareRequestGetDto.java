package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ShareRequest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class ShareRequestGetDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.share-request.v1";
    private static final String REQUEST_PATH = "share-requests";
    private static final String ORDERS_PATH = "orders";
    private static final String PATIENT_EMAIL_QUERY = "patient-email";
    private static final String MEDIC_QUERY = "medic";
    private static final String STUDY_TYPE_QUERY = "study-type";

    // Variables
    private String patientEmail;

    private MedicGetDto medic;

    private StudyTypeDto studyType;

    private String orders;

    private String url;

    // Constructors
    public ShareRequestGetDto() {
        // Use factory methods
    }

    public ShareRequestGetDto(ShareRequest shareRequest, UriInfo uriInfo){
        this.patientEmail = shareRequest.getPatientEmail();
        this.medic = new MedicGetDto(shareRequest.getMedic(), uriInfo);
        this.studyType = new StudyTypeDto(shareRequest.getStudyType(), uriInfo);

        this.orders = getOrdersUri(shareRequest, uriInfo);
        this.url = getUri(shareRequest, uriInfo);
    }

    private String getUri(ShareRequest shareRequest, UriInfo uriInfo){
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(REQUEST_PATH)
                .path(String.valueOf(shareRequest.getStudyType().getId()));

        return uriBuilder.build().toString();
    }

    private String getOrdersUri(ShareRequest shareRequest, UriInfo uriInfo){
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(ORDERS_PATH)
                .queryParam(PATIENT_EMAIL_QUERY, shareRequest.getPatientEmail())
                .queryParam(MEDIC_QUERY, String.valueOf(shareRequest.getMedic().getUser().getId()))
                .queryParam(STUDY_TYPE_QUERY, String.valueOf(shareRequest.getStudyType().getId()));

        return uriBuilder.build().toString();
    }

    // Getters&Setters
    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public MedicGetDto getMedic() {
        return medic;
    }

    public void setMedic(MedicGetDto medic) {
        this.medic = medic;
    }

    public StudyTypeDto getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyTypeDto studyType) {
        this.studyType = studyType;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
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
        ShareRequestGetDto that = (ShareRequestGetDto) o;
        return Objects.equals(patientEmail, that.patientEmail) && Objects.equals(medic, that.medic) && Objects.equals(studyType, that.studyType) && Objects.equals(orders, that.orders) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientEmail, medic, studyType, orders, url);
    }
}
