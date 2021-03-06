package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.Result;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.ResultService;
import ar.edu.itba.paw.services.UrlEncoderService;
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
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Path("orders/{orderId}/results")
@Component
public class ResultController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";


    @Autowired
    private ResultService resultService;

    @Autowired
    private UserService userService;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON, ResultGetDto.CONTENT_TYPE + "+json"})
    public Response getResults(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "page!!Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max = MAX_PAGE_SIZE, message = "perPage!!Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @PathParam("orderId") final String oid
    ) {
        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        long lastPage = resultService.findByOrderIdLastPage(orderId, perPage);

        if (lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<Result> results = resultService.findByOrderId(orderId, page, perPage);

        Collection<ResultGetDto> resultGetDtos = results.stream()
                .map(r -> (new ResultGetDto(r, urlEncoderService.encode(r.getOrderId()), uriInfo)))
                .collect(Collectors.toList());

        EntityTag entityTag = new EntityTag(Integer.toHexString(resultGetDtos.hashCode()));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<ResultGetDto>>(resultGetDtos) {
        })
                .type(ResultGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        if (page > MIN_PAGE)
            response.link(uriBuilder.replaceQueryParam("page", page - 1).build(), "prev");

        if (page < lastPage)
            response.link(uriBuilder.replaceQueryParam("page", page + 1).build(), "next");

        response.link(uriBuilder.replaceQueryParam("page", MIN_PAGE).build(), "first");
        response.link(uriBuilder.replaceQueryParam("page", lastPage).build(), "last");

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON, ResultGetDto.CONTENT_TYPE + "+json"})
    public Response getResultById(
            @PathParam("orderId") final String oid,
            @PathParam("id") final int id
    ) {
        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Result> resultOptional = resultService.findById(id);
        if (!resultOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Result result = resultOptional.get();
        if (result.getOrderId() != orderId)
            return Response.status(Response.Status.NOT_FOUND).build();

        String encodedPath = urlEncoderService.encode(result.getOrderId());
        ResultGetDto resultGetDto = new ResultGetDto(result, encodedPath, uriInfo);

        EntityTag entityTag = new EntityTag(Integer.toHexString(resultGetDto.hashCode()));
        Response.ResponseBuilder response = Response.ok(resultGetDto)
                .type(ResultGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

        return response.build();
    }

    @GET
    @Path("/{id}/identification")
    @Produces(value = {ImageDto.CONTENT_TYPE})
    public Response getResultIdentification(
            @PathParam("orderId") final String oid,
            @PathParam("id") final int id,
            @Context HttpHeaders httpHeaders
    ) {
        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Result> resultOptional = resultService.findById(id);
        if (!resultOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Result result = resultOptional.get();
        if (result.getOrderId() != orderId)
            return Response.status(Response.Status.NOT_FOUND).build();

        // Cache control
        CacheControl cc = new CacheControl();
        // They can store the cache but must revalidate before using it
        cc.setNoCache(true);

        String contentType = result.getIdentificationType();
        EntityTag entityTag = new EntityTag(Integer.toHexString(Arrays.hashCode(result.getIdentification())));
        // Evaluate etag
        Response.ResponseBuilder builder = request.evaluatePreconditions(entityTag);
        if (builder != null) {
            builder.cacheControl(cc);
            return builder.build();
        }

        // Return based on accept header
        String acceptHeader = httpHeaders.getHeaderString("Accept").toLowerCase();

        String ctType;
        try {
            ctType = MediaType.valueOf(contentType).getType();
        } catch (Exception e) {
            ctType = null;
        }

        if (acceptHeader.contains("*/*;encoding=base64") ||
                (ctType != null && acceptHeader.contains(ctType + "/*;encoding=base64")) ||
                acceptHeader.contains(contentType + ";encoding=base64")) {
            String b64Image = Base64.getEncoder().encodeToString(result.getIdentification());
            return Response.ok(b64Image).type(contentType + ";encoding=base64")
                    .tag(entityTag)
                    .cacheControl(cc)
                    .build();
        }

        ByteArrayInputStream identification = new ByteArrayInputStream(result.getIdentification());
        return Response.ok(identification).type(contentType)
                .tag(entityTag)
                .cacheControl(cc)
                .build();
    }

    @GET
    @Path("/{id}/file")
    @Produces(value = {ImageDto.CONTENT_TYPE})
    public Response getResultFile(
            @PathParam("orderId") final String oid,
            @PathParam("id") final int id,
            @Context HttpHeaders httpHeaders
    ) {
        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Result> resultOptional = resultService.findById(id);
        if (!resultOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Result result = resultOptional.get();
        if (result.getOrderId() != orderId)
            return Response.status(Response.Status.NOT_FOUND).build();

        // Cache control
        CacheControl cc = new CacheControl();
        // They can store the cache but must revalidate before using it
        cc.setNoCache(true);

        String contentType = result.getDataType().toLowerCase();
        EntityTag entityTag = new EntityTag(Integer.toHexString(Arrays.hashCode(result.getData())));

        // Evaluate etag
        Response.ResponseBuilder builder = request.evaluatePreconditions(entityTag);
        if (builder != null) {
            builder.cacheControl(cc);
            return builder.build();
        }

        // Return based on accept header
        String acceptHeader = httpHeaders.getHeaderString("Accept").toLowerCase();

        String ctType;
        try {
            ctType = MediaType.valueOf(contentType).getType();
        } catch (Exception e) {
            ctType = null;
        }

        if (acceptHeader.contains("*/*;encoding=base64") ||
                (ctType != null && acceptHeader.contains(ctType + "/*;encoding=base64")) ||
                acceptHeader.contains(contentType + ";encoding=base64")) {
            String b64Image = Base64.getEncoder().encodeToString(result.getData());
            return Response.ok(b64Image).type(contentType + ";encoding=base64")
                    .tag(entityTag)
                    .cacheControl(cc)
                    .build();
        }

        ByteArrayInputStream file = new ByteArrayInputStream(result.getData());
        return Response.ok(file).type(contentType)
                .tag(entityTag)
                .cacheControl(cc)
                .build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response createResult(
            @PathParam("orderId") final String oid,
            @Valid @NotNull ResultPostDto resultPostDto
    ) {
        Response.ResponseBuilder response;

        long orderId;
        try {
            orderId = urlEncoderService.decode(oid);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Order> orderOptional = orderService.findById(orderId);
        if (!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Order order = orderOptional.get();

        // clinic should be the one doing this task
        String userEmail = getLoggedUserEmail();
        if (userEmail == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        if (!order.getClinic().getEmail().equals(userEmail))
            return Response.status(Response.Status.FORBIDDEN).build();

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

    // auxiliar functions
    private User getLoggedUser() {
        String userEmail = getLoggedUserEmail();
        if (userEmail == null)
            return null;

        Optional<User> maybeUser = userService.findByEmail(userEmail);
        return maybeUser.orElse(null);
    }

    private String getLoggedUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null)
            return null;

        return auth.getName();
    }
}
