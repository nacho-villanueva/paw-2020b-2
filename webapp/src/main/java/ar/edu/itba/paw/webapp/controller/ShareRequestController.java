package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.ShareRequest;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.services.ShareRequestService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.ShareRequestGetDto;
import ar.edu.itba.paw.webapp.dto.ShareRequestPostDto;
import ar.edu.itba.paw.webapp.dto.StudyTypeDto;
import ar.edu.itba.paw.webapp.dto.annotations.IntegerSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("share-requests")
@Component
public class ShareRequestController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private ShareRequestService shareRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private StudyTypeService studyTypeService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, ShareRequestPostDto.CONTENT_TYPE+"+json"})
    public Response listShareRequests(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "page!!Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max=MAX_PAGE_SIZE, message = "perPage!!Number of entries per page must be between {min} and {max}")
                    Integer perPage
    ){

        String patientEmail = getLoggedUserEmail();
        if(patientEmail == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        long lastPage = shareRequestService.getAllPatientRequestsLastPage(patientEmail,perPage);

        if(lastPage <= 0)
            return Response.noContent().build();

        if(page > lastPage)
            return Response.status(422).build();

        Collection<ShareRequest> shareRequests = shareRequestService.getAllPatientRequests(patientEmail,page,perPage);

        Collection<ShareRequestGetDto> shareRequestGetDtos = shareRequests.stream()
                .map(sr -> (new ShareRequestGetDto(sr,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(shareRequestGetDtos.hashCode()));
        ResponseBuilder response =
                Response.ok(new GenericEntity<Collection<ShareRequestGetDto>>( shareRequestGetDtos ) {})
                .type(ShareRequestGetDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

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
    @Path("/{medicId}/{studyTypeId}")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getShareRequest(
            @PathParam("medicId") final int medicId,
            @PathParam("studyTypeId") final int studyTypeId
    ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        String patientEmail = authentication.getName();

        Optional<Medic> medicOptional = medicService.findByUserId(medicId);
        if(!medicOptional.isPresent() || !medicOptional.get().isVerified())
            return Response.status(Response.Status.BAD_REQUEST).build();
        Medic medic = medicOptional.get();

        Optional<StudyType> studyTypeOptional = studyTypeService.findById(studyTypeId);
        if(!studyTypeOptional.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).build();
        StudyType studyType = studyTypeOptional.get();

        Optional<ShareRequest> shareRequestOptional = shareRequestService.getShareRequest(medic,patientEmail,studyType);
        if(!shareRequestOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        ShareRequest shareRequest = shareRequestOptional.get();

        ShareRequestGetDto shareRequestGetDto = new ShareRequestGetDto(shareRequest, uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(shareRequestGetDto.hashCode()));
        ResponseBuilder response = Response.ok(shareRequestGetDto).type(ShareRequestGetDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response requestShare(
            @Valid ShareRequestPostDto shareRequestPostDto
    ){

        User user = getLoggedUser();
        if(user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Optional<Medic> medicOptional = medicService.findByUserId(user.getId());
        if(!medicOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Optional<StudyType> studyTypeOptional = studyTypeService.findById(shareRequestPostDto.getStudyTypeId());
        if(!studyTypeOptional.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).build();

        Medic medic = medicOptional.get();
        String patientEmail = shareRequestPostDto.getPatientEmail();
        StudyType studyType = studyTypeOptional.get();

        if(shareRequestService.getShareRequest(medic,patientEmail,studyType).isPresent())
            return Response.status(422).build();

        ShareRequest shareRequest = shareRequestService.requestShare(medic,patientEmail,studyType);

        // The medic doesn't have access to this resource (only the patient can access, therefore no resource is returned)
        /*
        final URI uri = uriInfo.getBaseUriBuilder().path("share-requests")
                .path(String.valueOf(shareRequest.getMedic().getUser().getId()))
                .path(String.valueOf(shareRequest.getStudyType().getId()))
                .build();
        */

        return Response.status(201).build();
    }

    @POST
    @Path("/{medicId}/{studyTypeId}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response AcceptShareRequest(
            @PathParam("medicId") final int medicId,
            @PathParam("studyTypeId") final int studyTypeId
    ){
        return RespondToShareRequest(medicId,studyTypeId,true);
    }

    @DELETE
    @Path("/{medicId}/{studyTypeId}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response RejectShareRequest(
            @PathParam("medicId") final int medicId,
            @PathParam("studyTypeId") final int studyTypeId
    ){
        return RespondToShareRequest(medicId,studyTypeId,false);
    }

    // Accepts or Declines the Share Request
    private Response RespondToShareRequest(final int medicId, final int studyTypeId, boolean acceptRequest){

        String patientEmail = getLoggedUserEmail();
        if(patientEmail == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Optional<Medic> medicOptional = medicService.findByUserId(medicId);
        if(!medicOptional.isPresent() || !medicOptional.get().isVerified())
            return Response.status(Response.Status.BAD_REQUEST).build();
        Medic medic = medicOptional.get();

        Optional<StudyType> studyTypeOptional = studyTypeService.findById(studyTypeId);
        if(!studyTypeOptional.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).build();
        StudyType studyType = studyTypeOptional.get();

        Optional<ShareRequest> shareRequestOptional = shareRequestService.getShareRequest(medic,patientEmail,studyType);
        if(!shareRequestOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        ShareRequest shareRequest = shareRequestOptional.get();

        shareRequestService.acceptOrDenyShare(shareRequest,acceptRequest);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // Auxiliar functions
    private User getLoggedUser() {
        String userEmail = getLoggedUserEmail();
        if(userEmail==null)
            return null;

        Optional<User> maybeUser = userService.findByEmail(userEmail);
        return maybeUser.orElse(null);
    }

    private String getLoggedUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getName() == null)
            return null;

        return auth.getName();
    }
}
