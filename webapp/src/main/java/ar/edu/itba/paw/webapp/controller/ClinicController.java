package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("clinics")
@Component
public class ClinicController {

    private static final int FIRST_PAGE = 1;

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE+"+json"})
    public Response listClinics(
            @QueryParam("clinic") String clinicName,
            @Valid @BeanParam ClinicHoursAvailabilityDto hours,
            @QueryParam("plan") String acceptedPlan,
            @QueryParam("study-type") String studyType,
            @QueryParam("page") Integer page,
            @QueryParam("per_page") Integer perPage
    ) {

        Collection<Clinic> clinics;
        Response.ResponseBuilder response;

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        boolean isGetAllQuery = (clinicName==null && (hours==null || hours.getDays()==null) && acceptedPlan==null && studyType==null);

        ClinicHours clinicHours=null;
        if(hours!=null && hours.getDays()!=null){
            clinicHours = hours.getClinicHours();
        }

        int queryPage = (page==null)?(FIRST_PAGE):(page);
        if(queryPage < FIRST_PAGE || (perPage!=null && perPage < 1))
            return Response.status(422).build();

        long lastPage;
        if(perPage==null){
            if(isGetAllQuery){
                lastPage = clinicService.getAllLastPage();
            }else{
                lastPage = clinicService.searchClinicsByLastPage(clinicName,clinicHours,acceptedPlan,studyType);
            }
        }else{
            if(isGetAllQuery){
                lastPage = clinicService.getAllLastPage(perPage);
            }else{
                lastPage = clinicService.searchClinicsByLastPage(clinicName,clinicHours,acceptedPlan,studyType,perPage);
            }
        }

        if(lastPage <= 0)
            return Response.noContent().build();

        if(queryPage > lastPage)
            return Response.status(422).build();

        if(perPage==null){
            if(isGetAllQuery){
                clinics = clinicService.getAll(queryPage);
            }else{
                clinics = clinicService.searchClinicsBy(clinicName,clinicHours,acceptedPlan,studyType,queryPage);
            }
        }else{
            if(isGetAllQuery){
                clinics = clinicService.getAll(queryPage,perPage);
            }else{
                clinics = clinicService.searchClinicsBy(clinicName,clinicHours,acceptedPlan,studyType,queryPage,perPage);
            }
        }

        Collection<ClinicGetDto> clinicDtos = (clinics.stream().map(c -> (new ClinicGetDto(c,uriInfo))).collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(clinicDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<ClinicGetDto>>( clinicDtos ) {})
                .type(ClinicGetDto.CONTENT_TYPE+"+json")
                .tag(etag).cacheControl(cacheControl);

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

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE+"+json"})
    public Response getClinicById(@PathParam("id") final String id){

        int clinicId;
        try {
            clinicId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);
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
    public Response getClinicStudyTypes(@PathParam("id") final String id){

        Response.ResponseBuilder response;
        int clinicId;

        try {
            clinicId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Collection<StudyType> studyTypes = clinicOptional.get().getMedicalStudies();
        if(studyTypes.isEmpty())
            return Response.noContent().build();

        Collection<StudyTypeDto> studyTypeDtos = studyTypes.stream().map(st -> (new StudyTypeDto(st,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<StudyTypeDto>>(studyTypeDtos) {})
                .type(StudyTypeDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}/available-studies/{study}")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getClinicHasStudyType(
            @PathParam("id") final String id,
            @PathParam("study") final String sId
    ){

        int clinicId;
        int studyTypeId;

        try {
            clinicId = Integer.parseInt(id);
            studyTypeId = Integer.parseInt(sId);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean hasStudy = clinicService.hasStudy(clinicId,studyTypeId);

        if(!hasStudy)
            return Response.status(Response.Status.NOT_FOUND).build();

        URI uri = uriInfo.getBaseUriBuilder()
                .path("study-types").path(String.valueOf(studyTypeId)).build();
        return Response.noContent().location(uri).build();
    }

    @GET
    @Path("/{id}/accepted-plans")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response getClinicAcceptedPlans(@PathParam("id") final String id){
        Response.ResponseBuilder response;
        int clinicId;

        try {
            clinicId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Collection<String> acceptedPlans = clinicOptional.get().getAcceptedPlans();
        if(acceptedPlans.isEmpty())
            return Response.noContent().build();

        Collection<MedicPlanDto> medicPlanDtos = acceptedPlans.stream().map(MedicPlanDto::new).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicPlanDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<MedicPlanDto>>(medicPlanDtos) {})
                .type(MedicPlanDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerClinic(
            @Valid ClinicPostDto clinicPostDto
    ){
        Response.ResponseBuilder response;

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Optional<User> userOptional = userService.findByEmail(authentication.getName());
        if(!userOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        User user = userOptional.get();

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
        response = Response.created(uri);

        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response updateClinic(
            @PathParam("id") String id,
            @Valid ClinicPutDto clinicPutDto
    ){
        Response.ResponseBuilder response;

        int clinicId;

        try {
            clinicId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Clinic clinic = clinicOptional.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Optional<User> userOptional = userService.findByEmail(authentication.getName());
        if(!userOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        User user = userOptional.get();

        // check permissions to edit profile
        if(!user.getId().equals(clinic.getUser().getId()))
            return Response.status(Response.Status.FORBIDDEN).build();

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

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
        response = Response.noContent().location(uri);

        return response.build();
    }

    // auxiliar functions
    private boolean isEmpty(String s){
        return s==null || s.trim().isEmpty();
    }
}
