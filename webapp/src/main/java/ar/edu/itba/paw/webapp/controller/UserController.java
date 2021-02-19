package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.UserGetDto;
import ar.edu.itba.paw.webapp.dto.UserPostDto;
import ar.edu.itba.paw.webapp.dto.UserPutDto;
import ar.edu.itba.paw.webapp.dto.annotations.Number;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Max;
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
    private static final int MAXIMUM_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

    @Autowired
    private UserService userService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    private CacheControl cacheControl;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, UserGetDto.CONTENT_TYPE+"+json"})
    public Response listUsers(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @Min(value = MINIMUM_PAGE, message = "page!!Page number must be at least {value}")
                    int page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @Min(value = MINIMUM_PAGE_SIZE, message = "perPage!!Number of entries per page must be at least {value}")
            @Max(value = MAXIMUM_PAGE_SIZE, message = "perPage!!Page number must be at most {value}")
                    int perPage
    ) {
        final Collection<User> users = userService.getAll(page, perPage);

        if(users.isEmpty()) {
            return Response.noContent().build();
        }

        final int pages = userService.getPageCount(perPage);

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
    public Response getUser(@PathParam("id") @Number final String id) {
        Optional<User> maybeUser = userService.findById(Integer.parseInt(id));

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
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response registerUser(@Valid @RequestBody UserPostDto userDto) {
        if(isEmpty(userDto.getLocale())) {
            userDto.setLocale(Locale.getDefault().toString());
        }
        User user = userService.register(userDto.getEmail(),userDto.getPassword(),userDto.getLocale());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response modifyUser(@PathParam("id") final int id, @Valid UserPutDto userDto) {
        Optional<User> maybeUser = userService.findById(id);

        if(!maybeUser.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        //We make sure logged user is the one trying to make changes
        if(!validateEditPermissions(maybeUser.get())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        //TODO: try and make this part smaller
        //We make the changes based on the input dto
        User user = maybeUser.get();
        if(!isEmpty(userDto.getEmail())) {
            user = userService.updateEmail(user, userDto.getEmail());
        }

        if(user == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if(!isEmpty(userDto.getPassword())) {
            user = userService.updatePassword(user, userDto.getPassword());
        }

        if(user == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if(!isEmpty(userDto.getLocale())) {
            user = userService.updateLocale(user, userDto.getLocale());
        }

        if(user == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.noContent().build();
    }

    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( authentication == null) {
            return null;
        }
        Optional<User> maybeUser = userService.findByEmail(authentication.getName());

        return maybeUser.orElse(null);
    }

    //If admin role is implemented properly, consider it here
    private boolean validateEditPermissions(final User targetUser) {
        User loggedUser = getLoggedUser();

        if(loggedUser == null) {
            return false;
        }

        return targetUser.getEmail().equals(loggedUser.getEmail());
    }

    private boolean validateViewPermissions(final User targetUser) {

        //Clinic's are public domain
        if (targetUser.getRole() == User.CLINIC_ROLE_ID) {
            return true;
        }

        //Admin's are private regardless
        if (targetUser.getRole() == User.ADMIN_ROLE_ID) {
            return false;
        }

        //Here on is no longer public domain
        User user = getLoggedUser();

        if (user == null) {
            return false;
        }

        //Medic's are public for logged users
        if(targetUser.getRole() == User.MEDIC_ROLE_ID) {
            return true;
        }

        //Patient's are available for verified medic's and clinic's and for the same patients, unavailable for the rest
        //If it's the owner or an admin
        if(user.getEmail().equals(targetUser.getEmail()) || user.getRole() == User.ADMIN_ROLE_ID) {
            return true;
        }

        //If it's a medic
        if(user.getRole() == User.MEDIC_ROLE_ID) {
            Optional<Medic> maybeMedic = medicService.findByUserId(user.getId());

            return maybeMedic.isPresent() && maybeMedic.get().isVerified();
        }

        //If it's a clinic
        if(user.getRole() == User.CLINIC_ROLE_ID) {
            Optional<Clinic> maybeClinic = clinicService.findByUserId(user.getId());

            return maybeClinic.isPresent() && maybeClinic.get().isVerified();
        }

        //For the rest, unavailable
        return false;
    }

}
