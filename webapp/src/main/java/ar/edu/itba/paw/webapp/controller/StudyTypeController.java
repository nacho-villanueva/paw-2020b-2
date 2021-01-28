package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.dto.ClinicGetDto;
import ar.edu.itba.paw.webapp.dto.ConstraintViolationDto;
import ar.edu.itba.paw.webapp.dto.StudyTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("study-types")
@Component
public class StudyTypeController {

    private static final int FIRST_PAGE = 1;

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private Validator validator;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response listStudyTypes() {

        Collection<StudyType> studyTypes;
        Response.ResponseBuilder response;

        studyTypes = studyTypeService.getAll();

        if(studyTypes.isEmpty())
            return Response.noContent().build();

        Collection<StudyTypeDto> studyTypeDtos = (studyTypes.stream().map(st -> (new StudyTypeDto(st,uriInfo))).collect(Collectors.toList()));
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<StudyTypeDto>>( studyTypeDtos ) {}).type(StudyTypeDto.CONTENT_TYPE+"+json");
        response = response.tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getStudyTypeById(@PathParam("id") final String id){

        Response.ResponseBuilder response;
        Integer studyTypeId;

        try {
            studyTypeId = Integer.valueOf(id);
        }catch (Error e){
            studyTypeId = null;
        }

        if (studyTypeId == null)
            return Response.status(Status.BAD_REQUEST).build();

        Optional<StudyType> studyTypeOptional = studyTypeService.findById(studyTypeId);

        if (!studyTypeOptional.isPresent())
            return Response.status(Status.NOT_FOUND).build();

        StudyTypeDto studyTypeDto = new StudyTypeDto(studyTypeOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDto.hashCode()));
        response = Response.ok(studyTypeDto).type(StudyTypeDto.CONTENT_TYPE+"+json");
        response = response.tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response registerStudyType(@Valid StudyTypeDto studyTypeDto, @Context HttpHeaders headers){

        Response.ResponseBuilder response;

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<StudyTypeDto>> violations = validator.validate(studyTypeDto);

        if(!violations.isEmpty()){
            response = Response.status(Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations.stream().map(cv -> (new ConstraintViolationDto(cv,messageSource.getMessage(cv.getMessageTemplate(),null,locale)))).collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json");
            return response.build();
        }

        // no errors
        final URI uri;
        String name = studyTypeDto.getName();

        Optional<StudyType> studyTypeOptional = studyTypeService.findByName(name);
        if(studyTypeOptional.isPresent()){
            // 422 Unprocessable Entity
            StudyType studyType = studyTypeOptional.get();

            uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(studyType.getId())).build();
            EntityTag entityTag = new EntityTag(Integer.toHexString(uri.toString().hashCode()));

            response = Response.status(422).location(uri);
            response = response.tag(entityTag).cacheControl(cacheControl);
        }else{
            // register
            final StudyType studyType = studyTypeService.register(name);

            uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(studyType.getId())).build();

            response = Response.created(uri);
        }

        return response.build();
    }

    @GET
    @Path("/{id}/clinics")
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE})
    public Response getClinicsByStudyType(
            @PathParam("id") final String id,
            @QueryParam("page") final Integer page,
            @QueryParam("per_page") final Integer per_page
    ){

        int queryPage = (page==null)?(FIRST_PAGE):(page);

        if(queryPage < FIRST_PAGE || (per_page!=null && per_page < 1))
            return Response.status(422).build();

        Response.ResponseBuilder response;
        Integer studyTypeId;
        try {
            studyTypeId = Integer.valueOf(id);
        }catch (Error e){
            studyTypeId = null;
        }
        if (studyTypeId == null)
            return Response.status(Status.BAD_REQUEST).build();

        long lastPage;
        if(per_page==null)
            lastPage = clinicService.getByStudyTypeIdLastPage(studyTypeId);
        else
            lastPage = clinicService.getByStudyTypeIdLastPage(studyTypeId,per_page);

        if(lastPage <= 0)
            return Response.noContent().build();

        if(queryPage > lastPage)
            return Response.status(422).build();

        Collection<Clinic> clinics = clinicService.getByStudyTypeId(studyTypeId);

        Collection<ClinicGetDto> clinicDtos = (clinics.stream().map(c -> (new ClinicGetDto(c,uriInfo))).collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(clinicDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<ClinicGetDto>>( clinicDtos ) {}).tag(etag).cacheControl(cacheControl);

        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        if(queryPage>FIRST_PAGE){
            response.link(uriBuilder.replaceQueryParam("page",FIRST_PAGE).build(),"first");
            response.link(uriBuilder.replaceQueryParam("page",queryPage-1).build(),"prev");
        }

        if(queryPage<lastPage){
            response.link(uriBuilder.replaceQueryParam("page",queryPage+1).build(),"next");
            response.link(uriBuilder.replaceQueryParam("page",lastPage).build(),"last");
        }

        return response.build();
    }

}
