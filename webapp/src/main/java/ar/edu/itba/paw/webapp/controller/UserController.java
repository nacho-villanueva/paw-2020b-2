package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.UserGetDto;
import ar.edu.itba.paw.webapp.dto.UserPostDto;
import ar.edu.itba.paw.webapp.dto.UserPutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Path("users")
@Component
public class UserController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MINIMUM_PAGE = 1;
    private static final int MINIMUM_PAGE_SIZE = 1;
    private static final String DEFAULT_PAGE_SIZE = "20";

    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @Autowired
    private CacheControl cacheControl;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, UserGetDto.CONTENT_TYPE+"+json"})
    public Response listUsers(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @Min(value = MINIMUM_PAGE,
                    message = "Page number must be at least " + MINIMUM_PAGE) int page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @Min(value = MINIMUM_PAGE_SIZE,
                    message = "Number of entries per page must be at least " + MINIMUM_PAGE_SIZE) int perPage) {
        final List<User> users = us.getAll(page, perPage);

        if(users.isEmpty()) {
            return Response.noContent().build();
        }

        final int pages = us.getPageCount(perPage);

        List<UserGetDto> userGetDtoList = users.stream().map(u -> new UserGetDto(u, uriInfo)).collect(Collectors.toList());

        EntityTag etag = new EntityTag(Integer.toHexString(userGetDtoList.hashCode()));

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<UserGetDto>>(userGetDtoList) {})
                .type(UserGetDto.CONTENT_TYPE+"+json")
                .tag(etag).cacheControl(cacheControl);

        //We check what links apply
        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        if(page > Integer.parseInt(DEFAULT_PAGE)) {
            response = response.link(uriBuilder.replaceQueryParam("page", page - 1).build(), "prev");
        }
        if(page < pages) {
            response = response.link(uriBuilder.replaceQueryParam("page", page + 1).build(), "next");
        }

        //Links that always apply
        response = response.link(uriBuilder.replaceQueryParam("page", DEFAULT_PAGE).build(), "first");
        response = response.link(uriBuilder.replaceQueryParam("page", pages).build(), "last");

        return response.build();

    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, UserGetDto.CONTENT_TYPE+"+json" })
    public Response getUser(@PathParam("id") final int id) {
        Optional<User> maybeUser = us.findById(id);

        if(!maybeUser.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!validateViewPermissions(maybeUser.get())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UserGetDto userGetDto = new UserGetDto(maybeUser.get(),uriInfo);
        EntityTag etag = new EntityTag(Integer.toHexString(userGetDto.hashCode()));

        return Response.ok(userGetDto).type(UserGetDto.CONTENT_TYPE+"+json")
                .tag(etag).cacheControl(cacheControl).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON, UserPostDto.CONTENT_TYPE+"+json" })
    public Response registerUser(@Valid @RequestBody UserPostDto userDto) {
        if(isEmpty(userDto.getLocale())) {
            userDto.setLocale(Locale.getDefault().toString());
        }
        User user = us.register(userDto.getEmail(),userDto.getPassword(),userDto.getLocale());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, UserPutDto.CONTENT_TYPE+"+json" })
    public Response modifyUser(@PathParam("id") final int id, @Valid UserPutDto userDto) {
        Optional<User> maybeUser = us.findById(id);

        if(!maybeUser.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        //We make sure logged user is the one trying to make changes
        if(!validateEditPermissions(maybeUser.get())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        //We make the changes based on the input dto
        User user = maybeUser.get();
        if(!isEmpty(userDto.getEmail())) {
            user = us.updateEmail(user, userDto.getEmail());
        }

        if(user == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if(!isEmpty(userDto.getPassword())) {
            user = us.updatePassword(user, userDto.getPassword());
        }

        if(user == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if(!isEmpty(userDto.getLocale())) {
            user = us.updateLocale(user, userDto.getLocale());
        }

        if(user == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.noContent().location(uriInfo.getAbsolutePathBuilder().build()).build();
    }

    private String getLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "";
    }

    private Collection<? extends GrantedAuthority> getLoggedRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getAuthorities() : new HashSet<>();
    }

    //If admin role is implemented properly, consider it here
    private boolean validateEditPermissions(final User targetUser) {
        String loggedUser = getLoggedUsername();

        return targetUser.getEmail().equals(loggedUser);
    }

    private boolean validateViewPermissions(final User targetUser) {
        String username = getLoggedUsername();

        if(targetUser.getEmail().equals(username)) {
            return true;
        }

        Collection<? extends GrantedAuthority> authorities = getLoggedRole();

        return authorities.stream().anyMatch(a -> a.getAuthority().equals("MEDIC") || a.getAuthority().equals("CLINIC") || a.getAuthority().equals("ADMIN"));
    }

}
