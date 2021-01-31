package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Medic;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class MedicGetDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.medic.v1";
    private static final String REQUEST_PATH = "medics";
    private static final String IDENTIFICATION_PATH = "identification";
    private static final String MEDICAL_FIELDS__PATH = "medical-fields";

    // Variables
    private Integer id;

    private String name;

    private String telephone;

    private String identification;

    private String licenceNumber;

    private String medicalFields;

    private boolean verified;

    private String url;

    // Constructors
    public MedicGetDto() {
        // Use factory method
    }

    public MedicGetDto(Medic medic, UriInfo uriInfo){
        this.id = medic.getUser().getId();
        this.name = medic.getName();
        this.telephone = medic.getTelephone();
        this.licenceNumber = medic.getLicenceNumber();
        this.verified = medic.isVerified();

        this.identification = getUriBuilder(medic,uriInfo).path(IDENTIFICATION_PATH).build().toString();
        this.medicalFields = getUriBuilder(medic, uriInfo).path(MEDICAL_FIELDS__PATH).build().toString();
        this.url = getUriBuilder(medic, uriInfo).build().toString();
    }

    private UriBuilder getUriBuilder(Medic medic, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(medic.getUser().getId().toString());
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getMedicalFields() {
        return medicalFields;
    }

    public void setMedicalFields(String medicalFields) {
        this.medicalFields = medicalFields;
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
        MedicGetDto that = (MedicGetDto) o;
        return verified == that.verified && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(telephone, that.telephone) && Objects.equals(identification, that.identification) && Objects.equals(licenceNumber, that.licenceNumber) && Objects.equals(medicalFields, that.medicalFields) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, telephone, identification, licenceNumber, medicalFields, verified, url);
    }
}
