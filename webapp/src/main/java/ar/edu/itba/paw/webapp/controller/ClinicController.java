package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.annotations.IntegerSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("clinics")
@Component
public class ClinicController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE+"+json"})
    public Response listClinics(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max=MAX_PAGE_SIZE, message = "Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @QueryParam("clinic") String clinicName,
            @Valid @BeanParam ClinicHoursAvailabilityDto hours,
            @QueryParam("plan") String acceptedPlan,
            @QueryParam("study-type") String studyType
    ) {
        boolean isGetAllQuery = (clinicName == null &&
                (hours == null || hours.getDays() == null) && acceptedPlan == null && studyType == null);

        ClinicHours clinicHours = null;
        if(hours != null && hours.getDays() != null){
            clinicHours = hours.getClinicHours();
        }

        int lastPage;
        if(isGetAllQuery){
            lastPage = clinicService.getAllLastPage(perPage);
        }else{
            lastPage = clinicService.searchClinicsByLastPage(clinicName,clinicHours,acceptedPlan,studyType,perPage);
        }

        if(lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<Clinic> clinics;
        if(isGetAllQuery){
            clinics = clinicService.getAll(page,perPage);
        }else{
            clinics = clinicService.searchClinicsBy(clinicName,clinicHours,acceptedPlan,studyType,page,perPage);
        }

        Collection<ClinicGetDto> clinicDtos = (clinics.stream().map(c -> (new ClinicGetDto(c,uriInfo))).collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(clinicDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<ClinicGetDto>>( clinicDtos ) {})
                .type(ClinicGetDto.CONTENT_TYPE+"+json")
                .tag(etag);

        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        if(page> MIN_PAGE)
            response.link(uriBuilder.replaceQueryParam("page",page-1).build(),"prev");

        if(page<lastPage)
            response.link(uriBuilder.replaceQueryParam("page",page+1).build(),"next");

        response.link(uriBuilder.replaceQueryParam("page", MIN_PAGE).build(),"first");
        response.link(uriBuilder.replaceQueryParam("page",lastPage).build(),"last");

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE+"+json"})
    public Response getClinicById(@PathParam("id") final int id){
        Optional<Clinic> clinicOptional = clinicService.findByUserId(id);
        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        ClinicGetDto clinicGetDto = new ClinicGetDto(clinicOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(clinicGetDto.hashCode()));
        Response.ResponseBuilder response = Response.ok(clinicGetDto).type(ClinicGetDto.CONTENT_TYPE+"+json")
                .tag(entityTag);

        return response.build();
    }

    @GET
    @Path("/{id}/available-studies")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getClinicStudyTypes(@PathParam("id") final int id){
        Optional<Clinic> clinicOptional = clinicService.findByUserId(id);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Collection<StudyType> studyTypes = clinicOptional.get().getMedicalStudies();
        if(studyTypes.isEmpty())
            return Response.noContent().build();

        Collection<StudyTypeDto> studyTypeDtos = studyTypes.stream().map(st -> (new StudyTypeDto(st,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<StudyTypeDto>>(studyTypeDtos) {})
                .type(StudyTypeDto.CONTENT_TYPE+"+json")
                .tag(entityTag);

        return response.build();
    }

    @GET
    @Path("/{id}/available-studies/{study}")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getClinicHasStudyType(
            @PathParam("id") final int clinicId,
            @PathParam("study") final int studyTypeId
    ){
        boolean hasStudy = clinicService.hasStudy(clinicId,studyTypeId);

        if(!hasStudy)
            return Response.status(Response.Status.NOT_FOUND).build();

        URI uri = uriInfo.getBaseUriBuilder()
                .path(StudyTypeDto.REQUEST_PATH).path(String.valueOf(studyTypeId)).build();
        return Response.noContent().location(uri).build();
    }

    @GET
    @Path("/{id}/accepted-plans")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response getClinicAcceptedPlans(@PathParam("id") final int id){
        Optional<Clinic> clinicOptional = clinicService.findByUserId(id);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Collection<MedicPlan> acceptedPlans = clinicOptional.get().getAcceptedPlans();
        if(acceptedPlans.isEmpty())
            return Response.noContent().build();

        Collection<MedicPlanDto> medicPlanDtos = acceptedPlans.stream().map(medicPlan -> (new MedicPlanDto(medicPlan,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicPlanDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<MedicPlanDto>>(medicPlanDtos) {})
                .type(MedicPlanDto.CONTENT_TYPE+"+json")
                .tag(entityTag);

        return response.build();
    }

    @GET
    @Path("/{id}/accepted-plans/{plan}")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response getClinicAcceptsPlan(
            @PathParam("id") final int clinicId,
            @PathParam("plan") final int medicPlanId
    ){
        boolean acceptsPlan = clinicService.acceptsPlan(clinicId,medicPlanId);

        if(!acceptsPlan)
            return Response.status(Response.Status.NOT_FOUND).build();

        URI uri = uriInfo.getBaseUriBuilder()
                .path(MedicPlanDto.REQUEST_PATH).path(String.valueOf(medicPlanId)).build();
        return Response.noContent().location(uri).build();
    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerClinic(
            @Valid @NotNull ClinicPostDto clinicPostDto
    ){
        User user = getLoggedUser();
        if(user == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();

        String name = clinicPostDto.getName();
        String telephone = clinicPostDto.getTelephone();
        Collection<StudyType> availableStudies = clinicPostDto.getStudiesCollection();
        Collection<MedicPlan> medicPlans = clinicPostDto.getMedicPlansCollection();
        ClinicHours clinicHours = clinicPostDto.getClinicHours();

        Clinic clinic = clinicService.register(
                user,
                name,
                telephone,
                availableStudies,
                medicPlans,
                clinicHours
                );

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(clinic.getUser().getId())).build();
        Response.ResponseBuilder response = Response.created(uri);

        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response updateClinic(
            @PathParam("id") final int id,
            @Valid @NotNull ClinicPostDto clinicPutDto
    ){

        Optional<Clinic> clinicOptional = clinicService.findByUserId(id);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Clinic clinic = clinicOptional.get();

        User user = getLoggedUser();
        if(user == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();

        // check permissions to edit profile
        if(!user.getId().equals(clinic.getUser().getId()))
            return Response.status(Response.Status.FORBIDDEN).build();

        // no errors
        clinicService.updateClinicInfo(user, clinicPutDto.getName(), clinicPutDto.getTelephone(),
                clinicPutDto.getStudiesCollection(), clinicPutDto.getMedicPlansCollection(), clinicPutDto.getClinicHours());

        return Response.noContent().location(uriInfo.getAbsolutePathBuilder().build()).build();
    }

    // Basing URI definition on: https://docs.github.com/en/rest/reference/gists#star-a-gist
    @PUT
    @Path("/{id}/verify")
    @Produces(value = {MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE + "+json"})
    public Response verifyClinic(@PathParam("id") final int id) {
        clinicService.verifyClinic(id);
        return Response.noContent().build();
    }

    // auxiliar functions
    private boolean isEmpty(String s){
        return s==null || s.trim().isEmpty();
    }

    private User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getName() == null)
            return null;

        Optional<User> maybeUser = userService.findByEmail(auth.getName());
        return maybeUser.orElse(null);
    }
}
