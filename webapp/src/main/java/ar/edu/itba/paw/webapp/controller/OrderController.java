package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Path("orders")
@Component
public class OrderController {

    private static final String DEFAULT_PAGE = "1";
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_PAGE_SIZE = "20";

    @Autowired
    private UserService userService;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON, OrderGetDto.CONTENT_TYPE + "+json"})
    public Response getOrders(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max=MAX_PAGE_SIZE, message = "Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @Valid @BeanParam OrderFilterDto orderFilterDto
    ) {
        Response.ResponseBuilder response;

        boolean isGetAllQuery = (isEmpty(orderFilterDto.getClinics()) && isEmpty(orderFilterDto.getMedics()) &&
                isEmpty(orderFilterDto.getPatientEmails()) && orderFilterDto.getFromDate() == null && orderFilterDto.getToDate() == null
                && isEmpty(orderFilterDto.getStudyTypes()));

        // we only want to fetch the orders that belong to the requesting user, or the ones they have access to (through sharing if medic)
        User user = getLoggedUser();
        if (user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Collection<User> clinicUsers = new ArrayList<>();
        Collection<User> medicUsers = new ArrayList<>();
        Collection<String> patientEmails = orderFilterDto.getPatientEmails();
        LocalDate fromDate = orderFilterDto.getFromDate();
        LocalDate toDate = orderFilterDto.getToDate();
        Collection<StudyType> studyTypes = new ArrayList<>();
        boolean includeShared = orderFilterDto.isIncludeShared();
        if (!isGetAllQuery) {
            if (!isEmpty(orderFilterDto.getClinics())) {
                for (Integer id : orderFilterDto.getClinics()) {
                    if (id != null)
                        clinicService.findByUserId(id).ifPresent(clinic -> clinicUsers.add(clinic.getUser()));
                }
            }

            if (!isEmpty(orderFilterDto.getMedics())) {
                for (Integer id : orderFilterDto.getMedics()) {
                    if (id != null) medicService.findByUserId(id).ifPresent(medic -> medicUsers.add(medic.getUser()));
                }
            }

            if (!isEmpty(orderFilterDto.getStudyTypes())) {
                for (Integer id : orderFilterDto.getStudyTypes()) {
                    if (id != null) studyTypeService.findById(id).ifPresent(studyTypes::add);
                }
            }
        }

        long lastPage;
        if (isGetAllQuery) {
            lastPage = orderService.getAllAsUserLastPage(user, includeShared, perPage);
        } else {
            lastPage = orderService.filterOrdersLastPage(
                    user,
                    clinicUsers,
                    medicUsers,
                    patientEmails,
                    fromDate,
                    toDate,
                    studyTypes,
                    includeShared,
                    perPage
            );
        }

        if(lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<Order> orders;
        if (isGetAllQuery) {
            orders = orderService.getAllAsUser(user, includeShared, page, perPage);
        } else {
            orders = orderService.filterOrders(
                    user,
                    clinicUsers,
                    medicUsers,
                    patientEmails,
                    fromDate,
                    toDate,
                    studyTypes,
                    includeShared,
                    page,
                    perPage
            );
        }

        Collection<OrderGetDto> orderDtos = (
                orders.stream()
                        .map(o -> (new OrderGetDto(o, urlEncoderService.encode(o.getOrderId()), uriInfo)))
                        .collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(orderDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<OrderGetDto>>(orderDtos) {
        })
                .type(OrderGetDto.CONTENT_TYPE + "+json")
                .tag(etag);

        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        if (page > MIN_PAGE) {

            response.link(uriBuilder.replaceQueryParam("page", page - 1).build(), "prev");
        }

        if (page < lastPage) {
            response.link(uriBuilder.replaceQueryParam("page", page + 1).build(), "next");
        }

        // Links that always apply
        response.link(uriBuilder.replaceQueryParam("page", MIN_PAGE).build(), "first");
        response.link(uriBuilder.replaceQueryParam("page", lastPage).build(), "last");

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON, OrderGetDto.CONTENT_TYPE + "+json"})
    public Response getOrderById(@PathParam("id") final String encodedId) {

        long orderId;
        try {
            orderId = urlEncoderService.decode(encodedId);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Order> orderOptional = orderService.findById(orderId);
        if (!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Order order = orderOptional.get();
        String encodedPath = urlEncoderService.encode(order.getOrderId());
        OrderGetDto orderGetDto = new OrderGetDto(order, encodedPath, uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(orderGetDto.hashCode()));
        Response.ResponseBuilder response = Response.ok(orderGetDto).type(OrderGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

        return response.build();
    }

    @GET
    @Path("/{id}/identification")
    @Produces(value = {ImageDto.CONTENT_TYPE})
    public Response getOrderIdentification(@Context HttpHeaders httpHeaders, @PathParam("id") final String encodedId) {

        long orderId;
        try {
            orderId = urlEncoderService.decode(encodedId);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Order> orderOptional = orderService.findById(orderId);
        if (!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Order order = orderOptional.get();

        String contentType = order.getIdentificationType();

        // Cache control
        CacheControl cc = new CacheControl();
        // They can store the cache but must revalidate before using it
        cc.setNoCache(true);
        // Get etag
        EntityTag etag = new EntityTag(Integer.toHexString(Arrays.hashCode(order.getIdentification())));
        // Evaluate etag
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder != null) {
            builder.cacheControl(cc);
            return builder.build();
        }
        // Return based on accept header
        String acceptHeader = httpHeaders.getHeaderString("Accept");

        if (acceptHeader.contains("image/*;encoding=base64")) {
            String b64Image = Base64.getEncoder().encodeToString(order.getIdentification());
            return Response.ok(b64Image).type(contentType + ";encoding=base64")
                    .tag(etag)
                    .cacheControl(cc)
                    .build();
        }
        ByteArrayInputStream identification = new ByteArrayInputStream(order.getIdentification());
        return Response.ok(identification).type(contentType)
                .tag(etag)
                .cacheControl(cc)
                .build();
    }

    @GET
    @Path("{id}/shared-with")
    @Produces(value = {MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE + "+json"})
    public Response getMedicsSharedWith(@PathParam("id") final String encodedId) {

        long orderId;
        try {
            orderId = urlEncoderService.decode(encodedId);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Optional<Order> orderOptional = orderService.findById(orderId);
        if (!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        Order order = orderOptional.get();

        // only affected patient should be able to see whom they shared with
        String userEmail = getLoggedUserEmail();
        if (userEmail == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        if (!order.getPatientEmail().equals(userEmail))
            return Response.status(Response.Status.FORBIDDEN).build();

        if (order.getSharedWith() == null || order.getSharedWith().isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();

        Collection<MedicGetDto> medicGetDtos = new ArrayList<>();
        for (User u : order.getSharedWith()) {
            medicService.findByUserId(u.getId()).ifPresent(medic -> medicGetDtos.add(new MedicGetDto(medic, uriInfo)));
        }
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicGetDtos.hashCode()));

        Response.ResponseBuilder response = Response.ok(new GenericEntity<Collection<MedicGetDto>>(medicGetDtos) {
        })
                .type(MedicGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

        return response.build();
    }

    @POST
    @Path("{id}/shared-with")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response addMedicsSharedWith(
            @Valid @NotNull UserGetDto userDto,
            @PathParam("id") final String encodedId
    ) {

        long orderId;
        try {
            orderId = urlEncoderService.decode(encodedId);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Optional<Order> orderOptional = orderService.findById(orderId);
        if (!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        Order order = orderOptional.get();

        // only affected patient should be able to do this action
        String userEmail = getLoggedUserEmail();
        if (userEmail == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        if (!order.getPatientEmail().equals(userEmail))
            return Response.status(Response.Status.FORBIDDEN).build();

        Optional<Medic> medicOptional = medicService.findByUserId(userDto.getId());
        if (!medicOptional.isPresent() || !medicOptional.get().isVerified())
            return Response.status(Response.Status.FORBIDDEN).build();
        User userMedic = medicOptional.get().getUser();

        if (order.getSharedWith().contains(userMedic) || order.getMedic().getUser().equals(userMedic))
            return Response.status(422).build();

        Order newOrder = orderService.shareWithMedic(order, userMedic);

        String path = urlEncoderService.encode(newOrder.getOrderId());
        URI uri = uriInfo.getBaseUriBuilder().path("orders").path(path).path("shared-with").build();

        return Response.status(Response.Status.NO_CONTENT).location(uri).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response createOrder(
            @Valid @NotNull OrderPostDto orderPostDto
    ) {
        User user = getLoggedUser();
        if (user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Optional<Medic> medicOptional = medicService.findByUserId(user.getId());
        if (!medicOptional.isPresent() || !medicOptional.get().isVerified())
            return Response.status(Response.Status.FORBIDDEN).build();

        Optional<Clinic> clinicOptional = clinicService.findByUserId(orderPostDto.getClinicId());
        if (!clinicOptional.isPresent() || !clinicOptional.get().isVerified())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Optional<StudyType> studyTypeOptional = studyTypeService.findById(orderPostDto.getStudyTypeId());
        if (!studyTypeOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Medic medic = medicOptional.get();
        LocalDate localDate = LocalDate.now();
        Clinic clinic = clinicOptional.get();
        String patientEmail = orderPostDto.getPatientEmail();
        String patientName = orderPostDto.getPatientName();
        StudyType studyType = studyTypeOptional.get();
        String description = orderPostDto.getDescription();
        String identificationType = medic.getIdentificationType();
        byte[] identification = medic.getIdentification();
        String medicPlan = orderPostDto.getPatientMedicPlan();
        String medicPlanNumber = orderPostDto.getPatientMedicPlanNumber();

        Order order = orderService.register(
                medic,
                localDate,
                clinic,
                patientEmail,
                patientName,
                studyType,
                description,
                identificationType,
                identification,
                medicPlan,
                medicPlanNumber
        );

        String encodedPath = urlEncoderService.encode(order.getOrderId());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(encodedPath).build();
        Response.ResponseBuilder response = Response.created(uri);

        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response updateOrder(
            @PathParam("id") final String encodedId,
            @Valid @NotNull OrderPutDto orderPutDto
    ){
        long orderId;
        try {
            orderId = urlEncoderService.decode(encodedId);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // only affected members should be able to change
        String userEmail = getLoggedUserEmail();
        if(userEmail == null)
            return Response.status(Response.Status.FORBIDDEN).build();

        final Optional<Order> orderOptional = orderService.findById(orderId);
        if (!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        Order order = orderOptional.get();

        if (!order.getPatientEmail().equals(userEmail) ||
                !order.getClinic().getUser().getEmail().equals(userEmail) ||
                !order.getMedic().getUser().getEmail().equals(userEmail))
            return Response.status(Response.Status.FORBIDDEN).build();

        Integer clinicId = orderPutDto.getClinicId();


        if (clinicId == null) {
            return Response.noContent().build();
        }

        final Optional<Clinic> clinicOptional = clinicService.findByUserId(orderPutDto.getClinicId());
        if (!clinicOptional.isPresent() || !clinicOptional.get().isVerified())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Clinic clinic = clinicOptional.get();

        try {
            orderService.changeOrderClinic(order, clinic);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


        return Response.noContent().build();
    }

    // auxiliar functions
    private boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

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
