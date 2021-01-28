package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.constraintGroups.ClinicPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("clinics")
@Component
public class ClinicController {

    private static final int FIRST_PAGE = 1;

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private Validator validator;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE+"+json"})
    public Response listClinics(
            @QueryParam("clinic") String clinicName,
            @Valid @BeanParam ClinicHoursAvailabilityDto hours,
            @QueryParam("plan") String acceptedPlan,
            @QueryParam("study-type") String studyType,
            @QueryParam("page") Integer page,
            @QueryParam("per_page") Integer per_page,
            @Context HttpHeaders headers
    ) {

        Collection<Clinic> clinics;
        Response.ResponseBuilder response;

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        boolean isGetAllQuery = (clinicName==null && (hours==null || hours.getDays()==null) && acceptedPlan==null && studyType==null);

        ClinicHours clinicHours=null;
        if(hours!=null && hours.getDays()!=null){
            Set<ConstraintViolation<ClinicHoursAvailabilityDto>> violations = validator.validate(hours);

            if(!violations.isEmpty()){
                response = Response.status(Response.Status.BAD_REQUEST).language(locale)
                        .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations.stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale)))).collect(Collectors.toList())) ) {})
                        .type(ConstraintViolationDto.CONTENT_TYPE+"+json");
                return response.build();
            }

            clinicHours = hours.getClinicHours();
        }

        int queryPage = (page==null)?(FIRST_PAGE):(page);
        if(queryPage < FIRST_PAGE || (per_page!=null && per_page < 1))
            return Response.status(422).build();

        long lastPage;
        if(per_page==null){
            if(isGetAllQuery){
                lastPage = clinicService.getAllLastPage();
            }else{
                lastPage = clinicService.searchClinicsByLastPage(clinicName,clinicHours,acceptedPlan,studyType);
            }
        }else{
            if(isGetAllQuery){
                lastPage = clinicService.getAllLastPage(per_page);
            }else{
                lastPage = clinicService.searchClinicsByLastPage(clinicName,clinicHours,acceptedPlan,studyType,per_page);
            }
        }

        if(lastPage <= 0)
            return Response.noContent().build();

        if(queryPage > lastPage)
            return Response.status(422).build();

        if(per_page==null){
            if(isGetAllQuery){
                clinics = clinicService.getAll();
            }else{
                clinics = clinicService.searchClinicsBy(clinicName,clinicHours,acceptedPlan,studyType);
            }
        }else{
            if(isGetAllQuery){
                clinics = clinicService.getAll(per_page);
            }else{
                clinics = clinicService.searchClinicsBy(clinicName,clinicHours,acceptedPlan,studyType,per_page);
            }
        }

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

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE+"+json"})
    public Response getClinicById(@PathParam("id") final String id){

        Response.ResponseBuilder response;
        Integer clinicId;

        try {
            clinicId = Integer.valueOf(id);
        }catch (Exception e){
            clinicId = null;
        }
        if (clinicId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        ClinicGetDto clinicGetDto = new ClinicGetDto(clinicOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(clinicGetDto.hashCode()));
        response = Response.ok(clinicGetDto).tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}/available-studies")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getClinicStudyTypes(@PathParam("id") final String id){

        Response.ResponseBuilder response;
        Integer clinicId;

        try {
            clinicId = Integer.valueOf(id);
        }catch (Exception e){
            clinicId = null;
        }
        if (clinicId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Collection<StudyType> studyTypes = clinicOptional.get().getMedicalStudies();
        if(studyTypes.isEmpty())
            return Response.noContent().build();

        Collection<StudyTypeDto> studyTypeDtos = studyTypes.stream().map(st -> (new StudyTypeDto(st,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<StudyTypeDto>>(studyTypeDtos) {}).tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}/available-studies/{study}")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getClinicHasStudyType(
            @PathParam("id") final String id,
            @PathParam("study") final String sId
    ){

        Integer clinicId;
        Integer studyTypeId;

        try {
            clinicId = Integer.valueOf(id);
            studyTypeId = Integer.valueOf(sId);
        }catch (Exception e){
            clinicId = null;
            studyTypeId = null;
        }

        if (clinicId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        boolean hasStudy = clinicService.hasStudy(clinicId,studyTypeId);

        if(hasStudy)
            return Response.noContent().build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/accepted-plans")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response getClinicAcceptedPlans(@PathParam("id") final String id){
        Response.ResponseBuilder response;
        Integer clinicId;

        try {
            clinicId = Integer.valueOf(id);
        }catch (Exception e){
            clinicId = null;
        }
        if (clinicId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Collection<String> acceptedPlans = clinicOptional.get().getAcceptedPlans();
        if(acceptedPlans.isEmpty())
            return Response.noContent().build();

        Collection<MedicPlanDto> medicPlanDtos = acceptedPlans.stream().map(MedicPlanDto::new).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicPlanDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<MedicPlanDto>>(medicPlanDtos) {}).tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerClinic(
            @Valid ClinicPostAndPutDto clinicPostAndPutDto,
            @Context HttpHeaders headers
    ){
        Response.ResponseBuilder response;

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<ClinicPostAndPutDto>> violations = validator.validate(clinicPostAndPutDto, ClinicPost.class);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations.stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale)))).collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        // no errors
        final URI uri;

        // TODO: GET THE LOGGEDIN USER, OR ELSE RESPOND WITH ERROR
        User user = userService.findById(7).get();

        if(!user.isUndefined())
            return Response.status(Response.Status.FORBIDDEN).build();

        String name = clinicPostAndPutDto.getName();
        String telephone = clinicPostAndPutDto.getTelephone();
        Collection<StudyType> availableStudies = clinicPostAndPutDto.getStudiesCollection();
        Set<String> medicPlans = clinicPostAndPutDto.getMedicPlansCollection();
        ClinicHours clinicHours = clinicPostAndPutDto.getClinicHours();

        Clinic clinic = clinicService.register(
                user,
                name,
                telephone,
                availableStudies,
                medicPlans,
                clinicHours
                );

        uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(clinic.getUser().getId())).build();
        response = Response.created(uri);

        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response updateClinic(
            @PathParam("id") Integer id,
            @Valid ClinicPostAndPutDto clinicPostAndPutDto,
            @Context HttpHeaders headers
    ){
        Response.ResponseBuilder response;

        Integer clinicId;

        try {
            clinicId = Integer.valueOf(id);
        }catch (Exception e){
            clinicId = null;
        }
        if (clinicId == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Optional<Clinic> clinicOptional = clinicService.findByUserId(clinicId);

        if(!clinicOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Clinic clinic = clinicOptional.get();

        // TODO: GET THE LOGGEDIN USER, OR ELSE RESPOND WITH ERROR
        User user = userService.findById(7).get();

        // check permissions to edit profile
        if(!(user.getId().equals(clinic.getUser().getId())))
            return Response.status(Response.Status.FORBIDDEN).build();

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<ClinicPostAndPutDto>> violations = validator.validate(clinicPostAndPutDto);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations.stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale)))).collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        // no errors
        final URI uri;

        String name;
        if (isEmpty(clinicPostAndPutDto.getName()))
            name = clinic.getName();
        else
            name = clinicPostAndPutDto.getName();
        String telephone;
        if (isEmpty(clinicPostAndPutDto.getTelephone()))
            telephone = clinic.getTelephone();
        else
            telephone = clinicPostAndPutDto.getTelephone();
        Collection<StudyType> availableStudies;
        if (clinicPostAndPutDto.getStudiesCollection() == null || clinicPostAndPutDto.getStudiesCollection().isEmpty())
            availableStudies = clinic.getMedicalStudies();
        else
            availableStudies = clinicPostAndPutDto.getStudiesCollection();
        Set<String> medicPlans;
        if (clinicPostAndPutDto.getMedicPlansCollection() == null)
            medicPlans = clinic.getAcceptedPlans();
        else
            medicPlans = clinicPostAndPutDto.getMedicPlansCollection();
        ClinicHours clinicHours;
        if (clinicPostAndPutDto.getClinicHours() == null)
            clinicHours = clinic.getHours();
        else
            clinicHours = clinicPostAndPutDto.getClinicHours();
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
        response = Response.status(Response.Status.NO_CONTENT).location(uri);

        return response.build();
    }

    // auxiliar functions
    private boolean isEmpty(String s){
        return s==null || s.trim().length()==0;
    }
}
