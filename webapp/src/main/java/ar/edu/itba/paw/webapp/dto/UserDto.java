package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class UserDto {

    private String email;
    private String url;

    public static UserDto fromUser(User user, final UriInfo uriInfo) {
        final UserDto dto = new UserDto();
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
}
