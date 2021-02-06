package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.dto.ClinicGetDto;
import ar.edu.itba.paw.webapp.dto.StudyTypeDto;
import ar.edu.itba.paw.webapp.dto.annotations.IntegerSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("study-types")
@Component
public class StudyTypeController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private ClinicService clinicService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response listStudyTypes() {

        Collection<StudyType> studyTypes = studyTypeService.getAll();

        if(studyTypes.isEmpty())
            return Response.noContent().build();

        Collection<StudyTypeDto> studyTypeDtos =
                (studyTypes.stream().map(st -> (new StudyTypeDto(st,uriInfo))).collect(Collectors.toList()));
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<StudyTypeDto>>( studyTypeDtos ) {})
                .type(StudyTypeDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getStudyTypeById(@PathParam("id") final int id){

        Optional<StudyType> studyTypeOptional = studyTypeService.findById(id);

        if (!studyTypeOptional.isPresent())
            return Response.status(Status.NOT_FOUND).build();

        StudyTypeDto studyTypeDto = new StudyTypeDto(studyTypeOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDto.hashCode()));
        Response.ResponseBuilder response = Response.ok(studyTypeDto).type(StudyTypeDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response registerStudyType(@Valid StudyTypeDto studyTypeDto){

        Response.ResponseBuilder response;
        final URI uri;
        String name = studyTypeDto.getName();

        Optional<StudyType> studyTypeOptional = studyTypeService.findByName(name);
        if(studyTypeOptional.isPresent()){
            // 422 Unprocessable Entity
            StudyType studyType = studyTypeOptional.get();

            uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(studyType.getId())).build();
            EntityTag entityTag = new EntityTag(Integer.toHexString(uri.toString().hashCode()));

            response = Response.status(422).location(uri)
                    .tag(entityTag).cacheControl(cacheControl);
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
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE+"+json"})
    public Response getClinicsByStudyType(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "page!!Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max=MAX_PAGE_SIZE, message = "perPage!!Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @PathParam("id") final int id
    ){

        long lastPage = clinicService.getByStudyTypeIdLastPage(id,perPage);

        if(lastPage <= 0)
            return Response.noContent().build();

        if(page > lastPage)
            return Response.status(422).build();

        Collection<Clinic> clinics = clinicService.getByStudyTypeId(id,page,perPage);

        Collection<ClinicGetDto> clinicDtos =
                (clinics.stream().map(c -> (new ClinicGetDto(c,uriInfo))).collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(clinicDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<ClinicGetDto>>( clinicDtos ) {})
                .type(ClinicGetDto.CONTENT_TYPE+"+json")
                .tag(etag).cacheControl(cacheControl);

        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        if(page> MIN_PAGE){
            response.link(uriBuilder.replaceQueryParam("page",page-1).build(),"prev");
        }

        if(page<lastPage){
            response.link(uriBuilder.replaceQueryParam("page",page+1).build(),"next");
        }

        response.link(uriBuilder.replaceQueryParam("page", MIN_PAGE).build(),"first");
        response.link(uriBuilder.replaceQueryParam("page",lastPage).build(),"last");

        return response.build();
    }

}
