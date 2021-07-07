package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Path("medics")
@Component
public class MedicController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MINIMUM_PAGE = 1;
    private static final int MINIMUM_PAGE_SIZE = 1;
    private static final int MAXIMUM_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

    @Autowired
    private MedicService medicService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE + "+json"})
    public Response getMedics(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @Min(value = MINIMUM_PAGE, message = "page!!Page number must be at least {value}")
                    int page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @Min(value = MINIMUM_PAGE_SIZE, message = "perPage!!Number of entries per page must be at least {value}")
            @Max(value = MAXIMUM_PAGE_SIZE, message = "perPage!!Page number must be at most {value}")
                    int perPage
    ) {
        final Collection<Medic> medics = medicService.getAll(page, perPage);

        if (medics.isEmpty()) {
            return Response.noContent().build();
        }

        final int pages = medicService.getAllLastPage(perPage);

        List<MedicGetDto> medicGetDtoList = medics.stream().map(m -> new MedicGetDto(m, uriInfo)).collect(Collectors.toList());
        EntityTag etag = new EntityTag(Integer.toHexString(medicGetDtoList.hashCode()));

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<MedicGetDto>>(medicGetDtoList) {
        })
                .type(MedicGetDto.CONTENT_TYPE + "+json")
                .tag(etag);

        //TODO: see if we can unify this on a util class and call it from the different controllers
        //We check what links apply
        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        if (page > Integer.parseInt(DEFAULT_PAGE)) {
            response = response.link(uriBuilder.replaceQueryParam("page", page - 1).build(), "prev");
        }
        if (page < pages) {
            response = response.link(uriBuilder.replaceQueryParam("page", page + 1).build(), "next");
        }

        //Links that always apply
        response = response.link(uriBuilder.replaceQueryParam("page", DEFAULT_PAGE).build(), "first");
        response = response.link(uriBuilder.replaceQueryParam("page", pages).build(), "last");

        return response.build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response registerMedic(
            @Valid @NotNull MedicPostDto medicDto
    ) {
        //We extract user info from authentication
        User loggedUser = getLoggedUser();

        if (loggedUser == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        ImageDto identification = medicDto.getIdentification();

        String name = medicDto.getName();
        String telephone = medicDto.getTelephone();
        String contentType = identification.getContentType();
        byte[] image = identification.getImageAsByteArray();
        Collection<MedicalField> knownFields = medicDto.getMedicalFieldCollection();
        String licenceNumber = medicDto.getLicenceNumber();

        if (image == null || image.length == 0)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Medic medic = medicService.register(
                loggedUser,
                name,
                telephone,
                contentType,
                image,
                licenceNumber,
                knownFields
        );

        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(medic.getUser().getId())).build();

        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE + "+json"})
    public Response getMedicById(@PathParam("id") final int id) {
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if (!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        MedicGetDto medicGetDto = new MedicGetDto(medicOptional.get(), uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicGetDto.hashCode()));

        return Response.ok(medicGetDto)
                .type(MedicGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response updateMedic(
            @PathParam("id") final int id,
            @Valid @NotNull MedicPutDto medicDto
    ) {
        //We extract user info from authentication
        User loggedUser = getLoggedUser();

        if (loggedUser == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        //We search for medic based on input
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if (!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        Medic medic = medicOptional.get();

        if (!loggedUser.getId().equals(medic.getUser().getId()))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        //We extract the data we need from the given DTO
        ImageDto identification = medicDto.getIdentification();
        String contentType = null;
        if (identification != null)
            contentType = identification.getContentType();
        byte[] image = null;
        if (identification != null)
            image = identification.getImageAsByteArray();

        //We persist the changes
        medicService.updateMedicInfo(
                loggedUser,
                medicDto.getName(),
                medicDto.getTelephone(),
                contentType,
                image,
                medicDto.getLicenceNumber(),
                medicDto.getMedicalFieldCollection()
        );

        //Location is same as request url
        return Response.noContent().location(uriInfo.getRequestUriBuilder().build()).build();
    }

    @GET
    @Path("/{id}/identification")
    @Produces(value = {ImageDto.CONTENT_TYPE})
    public Response getMedicIdentification(@Context HttpHeaders httpHeaders, @PathParam("id") final int id) {
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if (!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Medic medic = medicOptional.get();

        String contentType = medic.getIdentificationType();

        CacheControl cc = new CacheControl();
        // It can cache the response but must ask if it changed before using that cached image
        cc.setNoCache(true);
        // Get etag
        EntityTag etag = new EntityTag(Integer.toHexString(Arrays.hashCode(medic.getIdentification())));
        // Evaluate etag
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder != null) {
            builder.cacheControl(cc);
            return builder.build();
        }
        // Return based on accept header
        String acceptHeader = httpHeaders.getHeaderString("Accept").toLowerCase();

        String ctType;
        try {
            ctType = MediaType.valueOf(contentType).getType();
        } catch (Exception e) {
            ctType = null;
        }

        if (acceptHeader.contains("*/*;encoding=base64") ||
                (ctType != null && acceptHeader.contains(ctType + "/*;encoding=base64")) ||
                acceptHeader.contains(contentType + ";encoding=base64")) {
            String b64Image = Base64.getEncoder().encodeToString(medic.getIdentification());
            return Response.ok(b64Image).type(contentType + ";encoding=base64")
                    .tag(etag)
                    .cacheControl(cc)
                    .build();
        }
        ByteArrayInputStream identification = new ByteArrayInputStream(medic.getIdentification());
        return Response.ok(identification).type(contentType)
                .tag(etag)
                .cacheControl(cc)
                .build();
    }

    @GET
    @Path("/{id}/medical-fields")
    @Produces(value = {MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE + "+json"})
    public Response getMedicMedicalFields(@PathParam("id") final int id) {
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if (!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Medic medic = medicOptional.get();

        Collection<MedicalFieldDto> medicalFieldDtos = medic.getMedicalFields().stream()
                .map(mf -> (new MedicalFieldDto(mf, uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicalFieldDtos.hashCode()));

        return Response.ok(new GenericEntity<Collection<MedicalFieldDto>>(medicalFieldDtos) {
        })
                .type(MedicalFieldDto.CONTENT_TYPE + "+json")
                .tag(entityTag)
                .build();
    }

    // Basing URI definition on: https://docs.github.com/en/rest/reference/gists#star-a-gist
    @PUT
    @Path("/{id}/verify")
    @Produces(value = {MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE + "+json"})
    public Response verifyMedic(@PathParam("id") final int id) {
        medicService.verifyMedic(id);
        return Response.noContent().build();
    }

    // auxiliar functions
    private boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    private User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null)
            return null;

        Optional<User> maybeUser = userService.findByEmail(auth.getName());
        return maybeUser.orElse(null);
    }
}
