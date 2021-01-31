package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.constraintGroups.MedicPostGroup;
import ar.edu.itba.paw.webapp.dto.constraintGroups.MedicPutGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("medics")
@Component
public class MedicController {

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private Validator validator;

    @Autowired
    private MedicService medicService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE})
    public Response getMedics(){
        // Not planned at the moment
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerMedic(
            @Valid MedicPostAndPutDto medicPostAndPutDto,
            @Context HttpHeaders headers
            ){

        Response.ResponseBuilder response;

        // TODO: GET THE LOGGEDIN USER, OR ELSE RESPOND WITH ERROR
        User user = userService.findById(4).get();

        if(!user.isUndefined())
            return Response.status(Response.Status.FORBIDDEN).build();

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<MedicPostAndPutDto>> violations = validator.validate(medicPostAndPutDto, MedicPostGroup.class);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations
                            .stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale))))
                            .collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        ImageDto identification = medicPostAndPutDto.getIdentification();

        String name = medicPostAndPutDto.getName();
        String telephone = medicPostAndPutDto.getTelephone();
        String contentType = identification.getContentType();
        byte[] image = identification.getImageAsByteArray();
        Collection<MedicalField> knownFields = medicPostAndPutDto.getMedicalFieldCollection();
        String licenceNumber = medicPostAndPutDto.getLicenceNumber();

        if(image==null || image.length==0)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Medic medic = medicService.register(
                user,
                name,
                telephone,
                contentType,
                image,
                licenceNumber,
                knownFields
        );

        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(medic.getUser().getId())).build();
        response = Response.created(uri);

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE+"+json"})
    public Response getMedicById(@PathParam("id") final String id){

        int medicId;
        try {
            medicId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<Medic> medicOptional = medicService.findByUserId(medicId);
        if(!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Medic medic = medicOptional.get();

        MedicGetDto medicGetDto = new MedicGetDto(medic,uriInfo);
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
            @PathParam("id") final String id,
            @Valid MedicPostAndPutDto medicPostAndPutDto,
            @Context HttpHeaders headers
    ){
        int medicId;
        try {
            medicId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<Medic> medicOptional = medicService.findByUserId(medicId);
        if(!medicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Medic medic = medicOptional.get();

        Response.ResponseBuilder response;

        // TODO: GET THE LOGGEDIN USER, OR ELSE RESPOND WITH ERROR
        User user = userService.findById(4).get();

        if(!user.getId().equals(medic.getUser().getId()))
            return Response.status(Response.Status.FORBIDDEN).build();

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<MedicPostAndPutDto>> violations = validator.validate(medicPostAndPutDto, MedicPutGroup.class);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations
                            .stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale))))
                            .collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        ImageDto identification = medicPostAndPutDto.getIdentification();

        String name;
        if(isEmpty(medicPostAndPutDto.getName()))
            name = medic.getName();
        else
            name = medicPostAndPutDto.getName();
        String telephone;
        if (isEmpty(medicPostAndPutDto.getTelephone())) {
            telephone = medic.getTelephone();
        } else {
            telephone = medicPostAndPutDto.getTelephone();
        }
        String contentType;
        byte[] image;
        if (null == identification) {
            contentType = medic.getIdentificationType();
            image = medic.getIdentification();
        } else {
            contentType = identification.getContentType();
            image = identification.getImageAsByteArray();
        }
        Collection<MedicalField> knownFields;
        if(isEmpty(medicPostAndPutDto.getMedicalFieldCollection()))
            knownFields = medic.getMedicalFields();
        else
            knownFields = medicPostAndPutDto.getMedicalFieldCollection();
        String licenceNumber;
        if( isEmpty(medicPostAndPutDto.getLicenceNumber()))
            licenceNumber = medic.getLicenceNumber();
        else
            licenceNumber = medicPostAndPutDto.getLicenceNumber();
        boolean verified = medic.isVerified();

        Medic newMedic = medicService.updateMedicInfo(
                user,
                name,
                telephone,
                contentType,
                image,
                licenceNumber,
                knownFields,
                verified
        );

        URI uri = uriInfo.getBaseUriBuilder().path("orders").path(String.valueOf(newMedic.getUser().getId())).build();
        response = Response.noContent().location(uri);

        return response.build();
    }

    @GET
    @Path("/{id}/identification")
    @Produces(value = { ImageDto.CONTENT_TYPE})
    public Response getMedicIdentification(@PathParam("id") final String id){

        int medicId;
        try {
            medicId = Integer.parseInt(id);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Medic> medicOptional = medicService.findByUserId(medicId);
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
    @Produces(value = { MedicalFieldDto.CONTENT_TYPE+"+json"})
    public Response getMedicMedicalFields(@PathParam("id") final String id){

        int medicId;
        try {
            medicId = Integer.parseInt(id);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Medic> medicOptional = medicService.findByUserId(medicId);
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

    // auxiliar functions
    private boolean isEmpty(String s){
        return s==null || s.trim().length()==0;
    }

    private boolean isEmpty(Collection<?> collection){
        return collection==null || collection.isEmpty();
    }
}