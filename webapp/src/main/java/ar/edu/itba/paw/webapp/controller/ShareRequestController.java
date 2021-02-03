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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("share-requests")
@Component
public class ShareRequestController {
    private static final int FIRST_PAGE = 1;

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

    @Context
    private HttpHeaders headers;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, ShareRequestPostDto.CONTENT_TYPE+"+json"})
    public Response listShareRequests(
            @QueryParam("page") Integer page,
            @QueryParam("per_page") Integer perPage
    ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        String patientEmail = authentication.getName();

        int queryPage = (page==null)?(FIRST_PAGE):(page);
        if(queryPage < FIRST_PAGE || (perPage!=null && perPage < 1))
            return Response.status(422).build();

        long lastPage = shareRequestService.getAllPatientRequestsLastPage(patientEmail);

        if(lastPage <= 0)
            return Response.noContent().build();

        if(queryPage > lastPage)
            return Response.status(422).build();

        Collection<ShareRequest> shareRequests;
        if(perPage==null){
            shareRequests = shareRequestService.getAllPatientRequests(patientEmail,queryPage);
        }else{
            shareRequests = shareRequestService.getAllPatientRequests(patientEmail,queryPage,perPage);
        }

        Collection<ShareRequestGetDto> shareRequestGetDtos = shareRequests.stream()
                .map(sr -> (new ShareRequestGetDto(sr,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(shareRequestGetDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<ShareRequestGetDto>>( shareRequestGetDtos ) {})
                .type(ShareRequestGetDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

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
    @Path("/{medicId}/{studyTypeId}")
    @Produces(value = { MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE+"+json"})
    public Response getShareRequest(
            @PathParam("medicId") Integer medicId,
            @PathParam("studyTypeId") Integer studyTypeId
    ){

        if(medicId==null || studyTypeId==null)
            return Response.status(Response.Status.BAD_REQUEST).build();

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
        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Optional<User> userOptional = userService.findByEmail(authentication.getName());
        if(!userOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Optional<Medic> medicOptional = medicService.findByUserId(userOptional.get().getId());
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
            @PathParam("medicId") Integer medicId,
            @PathParam("studyTypeId") Integer studyTypeId
    ){
        return RespondToShareRequest(medicId,studyTypeId,true);
    }

    @DELETE
    @Path("/{medicId}/{studyTypeId}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response RejectShareRequest(
            @PathParam("medicId") Integer medicId,
            @PathParam("studyTypeId") Integer studyTypeId
    ){
        return RespondToShareRequest(medicId,studyTypeId,false);
    }

    // Accepts or Declines the Share Request
    private Response RespondToShareRequest(Integer medicId, Integer studyTypeId, boolean acceptRequest){

        if(medicId==null || studyTypeId==null)
            return Response.status(Response.Status.BAD_REQUEST).build();

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

        shareRequestService.acceptOrDenyShare(shareRequest,acceptRequest);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
