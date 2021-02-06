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
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.*;
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

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private MedicService medicService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE+"+json"})
    public Response getMedics(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @Min(value = MINIMUM_PAGE, message = "page!!Page number must be at least {value}")
                    int page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @Min(value = MINIMUM_PAGE_SIZE, message = "perPage!!Number of entries per page must be at least {value}")
            @Max(value = MAXIMUM_PAGE_SIZE, message = "perPage!!Page number must be at most {value}")
                    int perPage
    ){
        final Collection<Medic> medics = medicService.getAll(page,perPage);

        if(medics.isEmpty()) {
            return Response.noContent().build();
        }

        final int pages = medicService.getAllLastPage(perPage);

        List<MedicGetDto> medicGetDtoList = medics.stream().map(m -> new MedicGetDto(m,uriInfo)).collect(Collectors.toList());
        EntityTag etag = new EntityTag(Integer.toHexString(medicGetDtoList.hashCode()));

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<MedicGetDto>>(medicGetDtoList) {})
                .type(MedicGetDto.CONTENT_TYPE+"+json")
                .tag(etag).cacheControl(cacheControl);

        //TODO: see if we can unify this on a util class and call it from the different controllers
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

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerMedic(
            @Valid MedicPostDto medicDto
            ){
        //We extract user info from authentication
        User loggedUser = getLoggedUser();

        if(loggedUser == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        ImageDto identification = medicDto.getIdentification();

        String name = medicDto.getName();
        String telephone = medicDto.getTelephone();
        String contentType = identification.getContentType();
        byte[] image = identification.getImageAsByteArray();
        Collection<MedicalField> knownFields = medicDto.getMedicalFieldCollection();
        String licenceNumber = medicDto.getLicenceNumber();

        if(image==null || image.length==0)
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
    @Produces(value = { MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE+"+json"})
    public Response getMedicById(@PathParam("id") final int id){
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if(!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        MedicGetDto medicGetDto = new MedicGetDto(medicOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicGetDto.hashCode()));

        return Response.ok(medicGetDto)
                .type(MedicGetDto.CONTENT_TYPE+"+json")
                .cacheControl(cacheControl).tag(entityTag)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response updateMedic(
            @PathParam("id") final int id,
            @Valid MedicPutDto medicDto
    ){
        //We extract user info from authentication
        User loggedUser = getLoggedUser();

        if(loggedUser == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        //We search for medic based on input
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if(!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        Medic medic = medicOptional.get();

        if(!loggedUser.getId().equals(medic.getUser().getId()))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        //We extract the data we need from the given DTO and use previous values for the ones missing
        ImageDto identification = medicDto.getIdentification();

        String name = (isEmpty(medicDto.getName())) ? medic.getName() : medicDto.getName();
        String telephone = (isEmpty(medicDto.getTelephone())) ? medic.getTelephone() : medicDto.getTelephone();
        String contentType = (identification == null) ? medic.getIdentificationType() : identification.getContentType();
        byte[] image = (identification == null) ? medic.getIdentification() : identification.getImageAsByteArray();
        Collection<MedicalField> knownFields = (isEmptyCollection(medicDto.getMedicalFieldCollection())) ? medic.getMedicalFields() : medicDto.getMedicalFieldCollection();
        String licenceNumber = (isEmpty(medicDto.getLicenceNumber())) ? medic.getLicenceNumber() : medicDto.getLicenceNumber();

        //We persist the changes
        medicService.updateMedicInfo(
                loggedUser,
                name,
                telephone,
                contentType,
                image,
                licenceNumber,
                knownFields,
                medic.isVerified()
        );

        //Location is same as request url
        return Response.noContent().location(uriInfo.getRequestUriBuilder().build()).build();
    }

    @GET
    @Path("/{id}/identification")
    @Produces(value = { ImageDto.CONTENT_TYPE })
    public Response getMedicIdentification(@PathParam("id") final int id){
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if(!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Medic medic = medicOptional.get();

        String contentType = medic.getIdentificationType();
        ByteArrayInputStream identification = new ByteArrayInputStream(medic.getIdentification());
        EntityTag entityTag = new EntityTag(Integer.toHexString(Arrays.hashCode(medic.getIdentification())));

        return Response.ok(identification).type(contentType)
                .cacheControl(cacheControl).tag(entityTag)
                .build();
    }

    @GET
    @Path("/{id}/medical-fields")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE+"+json"})
    public Response getMedicMedicalFields(@PathParam("id") final int id){
        Optional<Medic> medicOptional = medicService.findByUserId(id);
        if(!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Medic medic = medicOptional.get();

        Collection<MedicalFieldDto> medicalFieldDtos = medic.getMedicalFields().stream()
                .map(mf -> (new MedicalFieldDto(mf,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicalFieldDtos.hashCode()));

        return Response.ok(new GenericEntity<Collection<MedicalFieldDto>>(medicalFieldDtos){})
                .type(MedicalFieldDto.CONTENT_TYPE+"+json")
                .cacheControl(cacheControl).tag(entityTag)
                .build();
    }

    //Medical fields have their own location, this endpoint should not be neccesary
    /*@GET
    @Path("/{id}/medical-fields/{mfid}")
    @Produces(value = { MedicalFieldDto.CONTENT_TYPE+"+json"})
    public Response getMedicHasMedicalField(
            @PathParam("id") final String id,
            @PathParam("mfid") final String mfid
    ){

        int medicId;
        int medicalFieldId;
        try {
            medicId = Integer.parseInt(id);
            medicalFieldId = Integer.parseInt(mfid);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean hasField = medicService.knowsField(medicId,medicalFieldId);

        if(!hasField)
            return Response.status(Response.Status.NOT_FOUND).build();

        URI uri = uriInfo.getBaseUriBuilder()
                .path("medical-fields").path(String.valueOf(medicalFieldId)).build();
        return Response.noContent().location(uri).build();
    }*/

    // auxiliar functions
    private boolean isEmptyCollection(Collection<?> collection){
        return collection==null || collection.isEmpty();
    }

    private User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getName() == null)
            return null;

        Optional<User> maybeUser = userService.findByEmail(auth.getName());
        return maybeUser.orElse(null);
    }
}
