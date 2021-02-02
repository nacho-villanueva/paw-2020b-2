package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response listUsers() {
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, UserDto.CONTENT_TYPE+"+json"})
    public Response getUser(@PathParam("id") final int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        final Optional<User> maybeUser = us.findById(id);

        if (!maybeUser.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!maybeUser.get().getEmail().equals(currentUserName)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UserDto userDto = new UserDto(maybeUser.get(),uriInfo);
        EntityTag entityTag = new EntityTag(String.valueOf(userDto.hashCode()));
        Response.ResponseBuilder response = Response.ok(userDto)
                .type(UserDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }


}
