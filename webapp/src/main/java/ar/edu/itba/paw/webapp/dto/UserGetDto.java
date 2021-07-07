package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

public class UserGetDto {

    public static final String CONTENT_TYPE = "application/vnd.user.v1";
    public static final String REQUEST_PATH = "users";

    private int id;
    @Email(message = "Please provide a valid email")
    private String email;
    @URL
    private String url;

    public UserGetDto() {

    }

    public UserGetDto(User user, final UriInfo uriInfo) {
        this.id = user.getId();
        this.email = user.getEmail();
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(REQUEST_PATH).path(user.getId().toString());
        this.url = uriBuilder.build().toString();
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
