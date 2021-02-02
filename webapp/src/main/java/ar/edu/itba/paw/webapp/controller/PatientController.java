package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.PatientService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.ConstraintViolationDto;
import ar.edu.itba.paw.webapp.dto.PatientDto;
import ar.edu.itba.paw.webapp.dto.constraintGroups.PatientPostGroup;
import ar.edu.itba.paw.webapp.dto.constraintGroups.PatientPutGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("patients")
@Component
public class PatientController {

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private Validator validator;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, PatientDto.CONTENT_TYPE+"+json"})
    public Response listPatients(){
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, PatientDto.CONTENT_TYPE+"+json"})
    public Response getPatient(@PathParam("id") final Integer patientId){

        if(patientId==null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Optional<Patient> patientOptional = patientService.findByUserId(patientId);
        if(!patientOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        PatientDto patientDto = new PatientDto(patientOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(String.valueOf(patientDto.hashCode()));

        ResponseBuilder response = Response.ok(patientDto).type(PatientDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerPatient(
            @Valid PatientDto patientDto
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Optional<User> userOptional = userService.findByEmail(authentication.getName());
        if(!userOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        User user = userOptional.get();

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<PatientDto>> violations = validator.validate(patientDto, PatientPostGroup.class);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations
                            .stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale))))
                            .collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        Patient patient;
        String name = patientDto.getName();
        if(patientDto.getMedicPlan()!=null){
            String medicPlan = patientDto.getMedicPlan().getPlan();
            String medicPlanNumber = patientDto.getMedicPlan().getNumber();

            patient = patientService.register(
                    user,
                    name,
                    medicPlan,
                    medicPlanNumber
            );
        }else{
            patient = patientService.register(user, name);
        }

        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(patient.getUser().getId())).build();

        return Response.created(uri).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response updatePatient(
            @PathParam("id") final Integer patientId,
            @Valid PatientDto patientDto
    ){
        if(patientId==null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Optional<User> userOptional = userService.findByEmail(authentication.getName());
        if(!userOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        User user = userOptional.get();

        Optional<Patient> patientOptional = patientService.findByUserId(patientId);
        if(!patientOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        Patient patient = patientOptional.get();

        if(!user.getId().equals(patient.getUser().getId()))
            return Response.status(Response.Status.FORBIDDEN).build();

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<PatientDto>> violations = validator.validate(patientDto, PatientPutGroup.class);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations
                            .stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale))))
                            .collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();


        String name = (isEmpty(patientDto.getName()))?(patient.getName()):(patientDto.getName());
        String medicPlan = patient.getMedicPlan();
        String medicPlanNumber = patient.getMedicPlanNumber();
        if(patientDto.getMedicPlan()!=null){
            medicPlan = patientDto.getMedicPlan().getPlan();
            medicPlanNumber = patientDto.getMedicPlan().getNumber();
        }

        Patient newPatient = patientService.updatePatientInfo(
                user,
                name,
                medicPlan,
                medicPlanNumber
        );

        URI uri = uriInfo.getBaseUriBuilder().path("patients")
                .path(String.valueOf(newPatient.getUser().getId())).build();

        return Response.noContent().location(uri).build();
    }

    // auxiliar functions
    private boolean isEmpty(String s){
        return s==null || s.trim().length()==0;
    }
}
