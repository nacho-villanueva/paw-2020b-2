package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.PatientService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.PatientGetDto;
import ar.edu.itba.paw.webapp.dto.PatientPostDto;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.net.URI;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

@Path("patients")
@Component
public class PatientController {

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, PatientGetDto.CONTENT_TYPE+"+json"})
    public Response listPatients(
            @QueryParam("email") @Email(message = "email!!") final String patientEmail
    ){
        if(patientEmail == null)
            return Response.noContent().build();

        Optional<Patient> patientOptional = patientService.findByEmail(patientEmail);
        if(!patientOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        URI uri = uriInfo.getBaseUriBuilder().path(PatientGetDto.REQUEST_PATH)
                .path(String.valueOf(patientOptional.get().getUser().getId())).build();
        EntityTag entityTag = new EntityTag(Integer.toHexString(uri.hashCode()));

        ResponseBuilder response = Response.noContent().location(uri)
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, PatientGetDto.CONTENT_TYPE+"+json"})
    public Response getPatient(@PathParam("id") final int patientId){
        Optional<Patient> patientOptional = patientService.findByUserId(patientId);
        if(!patientOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        PatientGetDto patientDto = new PatientGetDto(patientOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(patientDto.hashCode()));

        ResponseBuilder response = Response.ok(patientDto).type(PatientGetDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response registerPatient(
            @Valid PatientPostDto patientDto
    ){
        //We extract user info from authentication
        User loggedUser = getLoggedUser();

        if(loggedUser == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        Patient patient;
        String name = patientDto.getName();
        //If medic plan is empty, then given the dto is valid as requested, plan number is also empty
        if(isEmpty(patientDto.getMedicPlan())) {
            patient = patientService.register(loggedUser, name);
        } else {
            //They can specify medic plan but no number so we don't care what's on the medic plan number in this case
            patient = patientService.register(
                    loggedUser,
                    name,
                    patientDto.getMedicPlan(),
                    patientDto.getMedicPlanNumber()
            );
        }

        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(patient.getUser().getId())).build();

        return Response.created(uri).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response updatePatient(
            @PathParam("id") final int patientId,
            PatientPostDto patientDto
    ){
        //We extract user info from authentication
        User loggedUser = getLoggedUser();

        if(loggedUser == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        //We search for patient based on input
        Optional<Patient> maybePatient = patientService.findByUserId(patientId);
        if(!maybePatient.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        Patient patient = maybePatient.get();

        if(!loggedUser.getId().equals(patient.getUser().getId()))
            return Response.status(Response.Status.UNAUTHORIZED).build();


        //We extract the data we need from the given DTO and use previous values for the ones missing
        String name = (isEmpty(patientDto.getName())) ? (patient.getName()) : (patientDto.getName());
        String medicPlan = (isEmpty(patientDto.getMedicPlan())) ? patient.getMedicPlan() : patientDto.getMedicPlan();
        String medicPlanNumber = (isEmpty(patientDto.getMedicPlanNumber())) ? patient.getMedicPlanNumber() : patientDto.getMedicPlanNumber();

        //We persist the changes
        patientService.updatePatientInfo(
                loggedUser,
                name,
                medicPlan,
                medicPlanNumber
        );

        //Location is same as request url
        return Response.noContent().location(uriInfo.getRequestUriBuilder().build()).build();
    }

    private User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getName() == null)
            return null;

        Optional<User> maybeUser = userService.findByEmail(auth.getName());
        return maybeUser.orElse(null);
    }
}
