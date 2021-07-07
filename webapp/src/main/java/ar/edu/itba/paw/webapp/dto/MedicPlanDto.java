package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.MedicPlan;
import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class MedicPlanDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.medic-plan.v1";
    public static final String REQUEST_PATH = "medic-plans";

    // Variables
    private Integer id;

    @NotBlank(message = "MedicPlanDto.name.NotBlank")
    private String name;

    private String url;

    public MedicPlanDto() {

    }

    public MedicPlanDto(final MedicPlan medicPlan, final UriInfo uriInfo) {
        this.id = medicPlan.getId();
        this.name = medicPlan.getName();

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(medicPlan.getId().toString());
        this.url = uriBuilder.build().toString();
    }

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
        MedicPlanDto that = (MedicPlanDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url);
    }
}
