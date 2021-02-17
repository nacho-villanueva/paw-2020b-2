package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.annotations.IntegerSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("clinics")
@Component
public class ClinicController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

    // default cache
    @Autowired
    private CacheControl cacheControl;

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
            @IntegerSize(min = MIN_PAGE, message = "page!!Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max=MAX_PAGE_SIZE, message = "perPage!!Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @QueryParam("clinic") String clinicName,
            @Valid @BeanParam ClinicHoursAvailabilityDto hours,
            @QueryParam("plan") String acceptedPlan,
            @QueryParam("study-type") String studyType
    ) {

        Collection<Clinic> clinics;
        Response.ResponseBuilder response;

        boolean isGetAllQuery = (clinicName==null && (hours==null || hours.getDays()==null) && acceptedPlan==null && studyType==null);

        ClinicHours clinicHours=null;
        if(hours!=null && hours.getDays()!=null){
            clinicHours = hours.getClinicHours();
        }

        int lastPage;
        if(isGetAllQuery){
            lastPage = clinicService.getAllLastPage(perPage);
        }else{
            lastPage = clinicService.searchClinicsByLastPage(clinicName,clinicHours,acceptedPlan,studyType,perPage);
        }

        if(lastPage <= 0)
            return Response.noContent().build();

        if(page > lastPage)
            return Response.status(422).build();

        if(isGetAllQuery){
            clinics = clinicService.getAll(page,perPage);
        }else{
            clinics = clinicService.searchClinicsBy(clinicName,clinicHours,acceptedPlan,studyType,page,perPage);
        }

        Collection<ClinicGetDto> clinicDtos = (clinics.stream().map(c -> (new ClinicGetDto(c,uriInfo))).collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(clinicDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<ClinicGetDto>>( clinicDtos ) {})
                .type(ClinicGetDto.CONTENT_TYPE+"+json")
                .tag(etag).cacheControl(cacheControl);

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
                .tag(entityTag).cacheControl(cacheControl);

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
                .tag(entityTag).cacheControl(cacheControl);

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
        else
            return Response.noContent().build();
    }

    @GET
    @Path("/{id}/accepted-plans")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response getClinicAcceptedPlans(@PathParam("id") final int id){

        Optional<Clinic> clinicOptional = clinicService.findByUserId(id);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Collection<String> acceptedPlans = clinicOptional.get().getAcceptedPlans();
        if(acceptedPlans.isEmpty())
            return Response.noContent().build();

        Collection<MedicPlanDto> medicPlanDtos = acceptedPlans.stream().map(MedicPlanDto::new).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicPlanDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<MedicPlanDto>>(medicPlanDtos) {})
                .type(MedicPlanDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerClinic(
            @Valid ClinicPostDto clinicPostDto
    ){
        User user = getLoggedUser();
        if(user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        String name = clinicPostDto.getName();
        String telephone = clinicPostDto.getTelephone();
        Collection<StudyType> availableStudies = clinicPostDto.getStudiesCollection();
        Set<String> medicPlans = clinicPostDto.getMedicPlansCollection();
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
            @Valid ClinicPutDto clinicPutDto
    ){

        Optional<Clinic> clinicOptional = clinicService.findByUserId(id);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Clinic clinic = clinicOptional.get();

        User user = getLoggedUser();
        if(user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        // check permissions to edit profile
        if(!user.getId().equals(clinic.getUser().getId()))
            return Response.status(Response.Status.FORBIDDEN).build();

        // no errors
        final URI uri;

        String name;
        if (isEmpty(clinicPutDto.getName()))
            name = clinic.getName();
        else
            name = clinicPutDto.getName();
        String telephone;
        if (isEmpty(clinicPutDto.getTelephone()))
            telephone = clinic.getTelephone();
        else
            telephone = clinicPutDto.getTelephone();
        Collection<StudyType> availableStudies;
        if (clinicPutDto.getStudiesCollection() == null || clinicPutDto.getStudiesCollection().isEmpty())
            availableStudies = clinic.getMedicalStudies();
        else
            availableStudies = clinicPutDto.getStudiesCollection();
        Set<String> medicPlans;
        if (clinicPutDto.getMedicPlansCollection() == null)
            medicPlans = clinic.getAcceptedPlans();
        else
            medicPlans = clinicPutDto.getMedicPlansCollection();
        ClinicHours clinicHours;
        if (clinicPutDto.getClinicHours() == null)
            clinicHours = clinic.getHours();
        else
            clinicHours = clinicPutDto.getClinicHours();
        boolean isVerified = clinic.isVerified();

        Clinic newClinic = clinicService.updateClinicInfo(
                user,
                name,
                telephone,
                availableStudies,
                medicPlans,
                clinicHours,
                isVerified
        );

        uri = uriInfo.getAbsolutePathBuilder().build();
        Response.ResponseBuilder response = Response.noContent().location(uri);

        return response.build();
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
