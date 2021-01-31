package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.services.MedicalFieldService;
import ar.edu.itba.paw.webapp.dto.ClinicGetDto;
import ar.edu.itba.paw.webapp.dto.ConstraintViolationDto;
import ar.edu.itba.paw.webapp.dto.MedicalFieldDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("medical-fields")
@Component
public class MedicalFieldController {

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private Validator validator;

    @Autowired
    private MedicalFieldService medicalFieldService;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE+"+json",})
    public Response listMedicalFields() {

        Collection<MedicalField> medicalFields ;
        Response.ResponseBuilder response;

        medicalFields = medicalFieldService.getAll();

        if(medicalFields.isEmpty())
            response = Response.noContent();
        else{
            Collection<MedicalFieldDto> medicalFieldDtos = (medicalFields.stream().map(mf -> (new MedicalFieldDto(mf,uriInfo))).collect(Collectors.toList()));
            EntityTag etag = new EntityTag(Integer.toHexString(medicalFieldDtos.hashCode()));
            response = Response.ok(new GenericEntity<Collection<MedicalFieldDto>>( medicalFieldDtos ) {})
                    .type(MedicalFieldDto.CONTENT_TYPE+"+json")
                    .tag(etag).cacheControl(cacheControl);
        }

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE+"+json",})
    public Response getMedicalFieldById(@PathParam("id") final String id){

        Response.ResponseBuilder response;
        Integer medicalFieldId;

        try {
            medicalFieldId = Integer.valueOf(id);
        }catch (Error e){
            medicalFieldId = null;
        }

        if (medicalFieldId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Optional<MedicalField> medicalFieldOptional = medicalFieldService.findById(medicalFieldId);

        if (!medicalFieldOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        MedicalFieldDto medicalFieldDto = new MedicalFieldDto(medicalFieldOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicalFieldDto.hashCode()));
        response = Response.ok(medicalFieldDto).type(MedicalFieldDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE+"+json",})
    public Response registerMedicalField(@Valid MedicalFieldDto medicalFieldDto, @Context HttpHeaders headers){

        Response.ResponseBuilder response;

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<MedicalFieldDto>> violations = validator.validate(medicalFieldDto);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations.stream()
                            .map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale))))
                            .collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        // no errors
        final URI uri;
        String name = medicalFieldDto.getName();

        Optional<MedicalField> medicalFieldOptional = medicalFieldService.findByName(name);
        if(medicalFieldOptional.isPresent()){
            // 422 Unprocessable Entity
            MedicalField medicalField = medicalFieldOptional.get();

            uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(medicalField.getId())).build();
            EntityTag entityTag = new EntityTag(Integer.toHexString(uri.toString().hashCode()));

            response = Response.status(422).location(uri).tag(entityTag).cacheControl(cacheControl);
        }else{
            // register
            final MedicalField medicalField = medicalFieldService.register(name);

            uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(medicalField.getId())).build();

            response = Response.created(uri);
        }

        return response.build();
    }
}
