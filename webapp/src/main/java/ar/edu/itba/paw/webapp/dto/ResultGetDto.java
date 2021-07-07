package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Result;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.Objects;

public class ResultGetDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.result.v1";
    private static final String REQUEST_PATH = "results";
    private static final String ORDER_PATH = "orders";
    private static final String IDENTIFICATION_PATH = "identification";
    private static final String FILE_PATH = "file";

    // Variables
    private String order;

    private LocalDate date;

    private String identification;

    private String file;

    private String responsibleName;

    private String responsibleLicenceNumber;

    private String url;

    // Constructors
    public ResultGetDto() {
        // Use factory method
    }

    public ResultGetDto(Result result, String encodedPath, UriInfo uriInfo){
        this.responsibleName = result.getResponsibleName();
        this.responsibleLicenceNumber = result.getResponsibleLicenceNumber();
        this.date = result.getDate();

        this.order = getUriBuilder(encodedPath,uriInfo).build().toString();
        this.identification = getUriBuilder(encodedPath,uriInfo).path(REQUEST_PATH)
                .path(String.valueOf(result.getId())).path(IDENTIFICATION_PATH).build().toString();
        this.file = getUriBuilder(encodedPath,uriInfo).path(REQUEST_PATH)
                .path(String.valueOf(result.getId())).path(FILE_PATH).build().toString();
        this.url = getUriBuilder(encodedPath,uriInfo).path(REQUEST_PATH)
                .path(String.valueOf(result.getId())).build().toString();
    }

    private UriBuilder getUriBuilder(String path , UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path(ORDER_PATH).path(path);
    }

    // Getters&Setters
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    public String getResponsibleLicenceNumber() {
        return responsibleLicenceNumber;
    }

    public void setResponsibleLicenceNumber(String responsibleLicenceNumber) {
        this.responsibleLicenceNumber = responsibleLicenceNumber;
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
        ResultGetDto that = (ResultGetDto) o;
        return Objects.equals(order, that.order) && Objects.equals(date, that.date) && Objects.equals(identification, that.identification) && Objects.equals(file, that.file) && Objects.equals(responsibleName, that.responsibleName) && Objects.equals(responsibleLicenceNumber, that.responsibleLicenceNumber) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, date, identification, file, responsibleName, responsibleLicenceNumber, url);
    }
}
