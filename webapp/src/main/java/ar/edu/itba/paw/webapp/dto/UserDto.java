package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class UserDto {

    public static final String CONTENT_TYPE = "application/vnd.user.v1";

    @NotNull(message = "UserDto.id.NotNull")
    private Integer id;

    private String email;

    private String url;

    public UserDto() {
        // Use factory method
    }

    public UserDto(User user, final UriInfo uriInfo) {
        this.id = user.getId();
        this.email = user.getEmail();
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("users").path(user.getId().toString());
        this.url = uriBuilder.build().toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(email, userDto.email) && Objects.equals(url, userDto.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, url);
    }
}
