package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.Result;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.ResultService;
import ar.edu.itba.paw.services.UrlEncoderService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.constraintGroups.ResultGroup;
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
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Path("orders/{orderId}/results")
@Component
public class ResultController {

    private static final int FIRST_PAGE = 1;

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private Validator validator;

    @Autowired
    private ResultService resultService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, ResultGetDto.CONTENT_TYPE+"+json"})
    public Response getResults(
            @PathParam("orderId") final String oid,
            @QueryParam("page") Integer page,
            @QueryParam("per_page") Integer perPage
    ){
        Response.ResponseBuilder response;
        long orderId;

        try {
            orderId = urlEncoderService.decode(oid);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int queryPage = (page==null)?(FIRST_PAGE):(page);
        if(queryPage < FIRST_PAGE || (perPage!=null && perPage < 1))
            return Response.status(422).build();

        long lastPage;
        if(perPage==null)
            lastPage = resultService.findByOrderIdLastPage(orderId);
        else
            lastPage = resultService.findByOrderIdLastPage(orderId,perPage);

        if(lastPage <= 0)
            return Response.noContent().build();

        if(queryPage > lastPage)
            return Response.status(422).build();

        Collection<Result> results;
        if(perPage==null)
            results = resultService.findByOrderId(orderId,queryPage);
        else
            results = resultService.findByOrderId(orderId,queryPage,perPage);

        Collection<ResultGetDto> resultGetDtos = results.stream()
                .map(r -> (new ResultGetDto(r,urlEncoderService.encode(r.getOrderId()),uriInfo)))
                .collect(Collectors.toList());

        EntityTag entityTag = new EntityTag(Integer.toHexString(resultGetDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<ResultGetDto>>(resultGetDtos) {})
                .type(ResultGetDto.CONTENT_TYPE+"+json")
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
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, ResultGetDto.CONTENT_TYPE+"+json"})
    public Response getResultById(
            @PathParam("orderId") final String oid,
            @PathParam("id") final String id
    ){
        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int resultId;
        try {
            resultId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Result> resultOptional = resultService.findById(resultId);
        if(!resultOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Result result = resultOptional.get();
        if(result.getOrderId()!=orderId)
            return Response.status(Response.Status.NOT_FOUND).build();

        String encodedPath = urlEncoderService.encode(result.getOrderId());
        ResultGetDto resultGetDto = new ResultGetDto(result,encodedPath,uriInfo);

        EntityTag entityTag = new EntityTag(Integer.toHexString(resultGetDto.hashCode()));
        Response.ResponseBuilder response = Response.ok(resultGetDto)
                .type(ResultGetDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}/identification")
    @Produces(value = { ImageDto.CONTENT_TYPE })
    public Response getResultIdentification(
            @PathParam("orderId") final String oid,
            @PathParam("id") final String id
    ){
        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int resultId;
        try {
            resultId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Result> resultOptional = resultService.findById(resultId);
        if(!resultOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Result result = resultOptional.get();
        if(result.getOrderId()!=orderId)
            return Response.status(Response.Status.NOT_FOUND).build();

        String contentType = result.getIdentificationType();
        ByteArrayInputStream identification = new ByteArrayInputStream(result.getIdentification());
        EntityTag entityTag = new EntityTag(Integer.toHexString(Arrays.hashCode(result.getIdentification())));

        return Response.ok(identification).type(contentType)
                .cacheControl(cacheControl).tag(entityTag)
                .build();
    }

    @GET
    @Path("/{id}/file")
    @Produces(value = { ImageDto.CONTENT_TYPE })
    public Response getResultFile(
            @PathParam("orderId") final String oid,
            @PathParam("id") final String id
    ){
        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int resultId;
        try {
            resultId = Integer.parseInt(id);
        }catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Result> resultOptional = resultService.findById(resultId);
        if(!resultOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Result result = resultOptional.get();
        if(result.getOrderId()!=orderId)
            return Response.status(Response.Status.NOT_FOUND).build();

        String contentType = result.getDataType();
        ByteArrayInputStream file = new ByteArrayInputStream(result.getData());
        EntityTag entityTag = new EntityTag(Integer.toHexString(Arrays.hashCode(result.getData())));

        return Response.ok(file).type(contentType)
                .cacheControl(cacheControl).tag(entityTag)
                .build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response createResult(
            @PathParam("orderId") final String oid,
            @Valid ResultPostDto resultPostDto
            ){
        Response.ResponseBuilder response;

        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Order> orderOptional = orderService.findById(orderId);
        if(!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Order order = orderOptional.get();

        // clinic should be the one doing this task
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()==null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        String userEmail = authentication.getName();

        if(!order.getClinic().getEmail().equals(userEmail))
            return Response.status(Response.Status.FORBIDDEN).build();

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<ResultPostDto>> violations = validator.validate(resultPostDto, ResultGroup.class);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations
                            .stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale))))
                            .collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        String resultDataType = resultPostDto.getFile().getContentType();
        byte[] resultData = resultPostDto.getFile().getImageAsByteArray();
        String identificationType = resultPostDto.getIdentification().getContentType();
        byte[] identification = resultPostDto.getIdentification().getImageAsByteArray();
        LocalDate date = LocalDate.now();
        String responsibleName = resultPostDto.getResponsibleName();
        String responsibleLicenceNumber = resultPostDto.getResponsibleLicence();

        Result result = resultService.register(
                orderId,
                resultDataType,
                resultData,
                identificationType,
                identification,
                date,
                responsibleName,
                responsibleLicenceNumber
        );

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(result.getId())).build();
        response = Response.created(uri);

        return response.build();
    }
}
