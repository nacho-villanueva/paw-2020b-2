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
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Path("study-types")
@Component
public class StudyTypeController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

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
                .tag(entityTag);

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
                .tag(entityTag);

        return response.build();
    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response registerStudyType(@Valid @NotNull StudyTypeDto studyTypeDto){
        StudyType newType = studyTypeService.register(studyTypeDto.getName());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(newType.getId())).build()).build();
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

        if(lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<Clinic> clinics = clinicService.getByStudyTypeId(id,page,perPage);

        Collection<ClinicGetDto> clinicDtos =
                (clinics.stream().map(c -> (new ClinicGetDto(c,uriInfo))).collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(clinicDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<ClinicGetDto>>( clinicDtos ) {})
                .type(ClinicGetDto.CONTENT_TYPE+"+json")
                .tag(etag);

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
