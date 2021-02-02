package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class UserGetDto {

    public static final String CONTENT_TYPE = "application/vnd.user.v1";
    private int id;
    private String email;
    private String url;

    public static UserGetDto fromUser(User user, final UriInfo uriInfo) {
        final UserGetDto dto = new UserGetDto();
        dto.id = user.getId();
        dto.email = user.getEmail();
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("users").path(user.getId().toString());
        dto.url = uriBuilder.build().toString();
        return dto;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGetDto userDto = (UserGetDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(email, userDto.email) && Objects.equals(url, userDto.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, url);
    }
}
