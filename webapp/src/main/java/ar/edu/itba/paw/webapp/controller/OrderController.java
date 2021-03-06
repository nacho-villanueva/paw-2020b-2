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
            @IntegerSize(min = MIN_PAGE_SIZE, max = MAX_PAGE_SIZE, message = "Number of entries per page must be between {min} and {max}")
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

        Collection<Integer> clinicUsers = orderFilterDto.getClinics();
        Collection<Integer> medicUsers = orderFilterDto.getMedics();
        Collection<String> patientEmails = orderFilterDto.getPatientEmails();
        LocalDate fromDate = orderFilterDto.getFromDate();
        LocalDate toDate = orderFilterDto.getToDate();
        Collection<Integer> studyTypes = orderFilterDto.getStudyTypes();
        boolean includeShared = orderFilterDto.isIncludeShared();

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

        if (lastPage <= 0 || page > lastPage)
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
    @Path("/{id}/clinic")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response changeClinic(
            @PathParam("id") final String encodedId,
            @Valid @NotNull OrderChangeClinicDto changeClinicDto
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

        // only affected members should be able to change
        String userEmail = getLoggedUserEmail();
        if (userEmail == null)
            return Response.status(Response.Status.FORBIDDEN).build();

        if (!order.getPatientEmail().equals(userEmail) ||
                !order.getClinic().getUser().getEmail().equals(userEmail) ||
                !order.getMedic().getUser().getEmail().equals(userEmail))
            return Response.status(Response.Status.FORBIDDEN).build();

        final Optional<Clinic> clinicOptional = clinicService.findByUserId(changeClinicDto.getClinicId());
        if (!clinicOptional.isPresent() || !clinicOptional.get().isVerified())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Clinic clinic = clinicOptional.get();

        if (!clinic.getMedicalStudies().stream().map(StudyType::getId).collect(Collectors.toList()).contains(order.getStudy().getId()))
            return Response.status(Response.Status.BAD_REQUEST).build();

        try {
            if(orderService.changeOrderClinic(order, clinic) == null)
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/filters/study-type")
    @Produces(value = {MediaType.APPLICATION_JSON, StudyTypeDto.CONTENT_TYPE + "+json"})
    public Response getRelevantStudyTypes(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max = MAX_PAGE_SIZE, message = "Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @Valid @BeanParam OrderFilterDto orderFilterDto
    ) {
        Response.ResponseBuilder response;

        // we only want to fetch the orders that belong to the requesting user, or the ones they have access to (through sharing if medic)
        User user = getLoggedUser();
        if (user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Collection<Integer> clinicUsers = orderFilterDto.getClinics();
        Collection<Integer> medicUsers = orderFilterDto.getMedics();
        Collection<String> patientEmails = orderFilterDto.getPatientEmails();
        LocalDate fromDate = orderFilterDto.getFromDate();
        LocalDate toDate = orderFilterDto.getToDate();
        Collection<Integer> studyTypes = orderFilterDto.getStudyTypes();
        boolean includeShared = orderFilterDto.isIncludeShared();

        long lastPage = orderService.studyTypesFromFilteredOrdersLastPage(
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

        if (lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<StudyType> studyTypeCollection = orderService.studyTypesFromFilteredOrders(
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

        Collection<StudyTypeDto> studyTypeDtos =
                (studyTypeCollection.stream().map(st -> (new StudyTypeDto(st, uriInfo))).collect(Collectors.toList()));
        EntityTag entityTag = new EntityTag(Integer.toHexString(studyTypeDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<StudyTypeDto>>(studyTypeDtos) {
        })
                .type(StudyTypeDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

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
    @Path("/filters/clinic")
    @Produces(value = {MediaType.APPLICATION_JSON, ClinicGetDto.CONTENT_TYPE + "+json"})
    public Response getRelevantClinics(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max = MAX_PAGE_SIZE, message = "Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @Valid @BeanParam OrderFilterDto orderFilterDto
    ) {
        Response.ResponseBuilder response;

        // we only want to fetch the orders that belong to the requesting user, or the ones they have access to (through sharing if medic)
        User user = getLoggedUser();
        if (user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Collection<Integer> clinicUsers = orderFilterDto.getClinics();
        Collection<Integer> medicUsers = orderFilterDto.getMedics();
        Collection<String> patientEmails = orderFilterDto.getPatientEmails();
        LocalDate fromDate = orderFilterDto.getFromDate();
        LocalDate toDate = orderFilterDto.getToDate();
        Collection<Integer> studyTypes = orderFilterDto.getStudyTypes();
        boolean includeShared = orderFilterDto.isIncludeShared();

        long lastPage = orderService.clinicsFromFilteredOrdersLastPage(
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

        if (lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<Clinic> clinicCollection = orderService.clinicsFromFilteredOrders(
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

        Collection<ClinicGetDto> clinicDtos = (clinicCollection.stream().map(c -> (new ClinicGetDto(c, uriInfo))).collect(Collectors.toList()));
        EntityTag entityTag = new EntityTag(Integer.toHexString(clinicDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<ClinicGetDto>>(clinicDtos) {
        })
                .type(ClinicGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

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
    @Path("/filters/medic")
    @Produces(value = {MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE + "+json"})
    public Response getRelevantMedics(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max = MAX_PAGE_SIZE, message = "Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @Valid @BeanParam OrderFilterDto orderFilterDto
    ) {
        Response.ResponseBuilder response;

        // we only want to fetch the orders that belong to the requesting user, or the ones they have access to (through sharing if medic)
        User user = getLoggedUser();
        if (user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Collection<Integer> clinicUsers = orderFilterDto.getClinics();
        Collection<Integer> medicUsers = orderFilterDto.getMedics();
        Collection<String> patientEmails = orderFilterDto.getPatientEmails();
        LocalDate fromDate = orderFilterDto.getFromDate();
        LocalDate toDate = orderFilterDto.getToDate();
        Collection<Integer> studyTypes = orderFilterDto.getStudyTypes();
        boolean includeShared = orderFilterDto.isIncludeShared();

        long lastPage = orderService.medicsFromFilteredOrdersLastPage(
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

        if (lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<Medic> medicCollection = orderService.medicsFromFilteredOrders(
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

        Collection<MedicGetDto> medicDtos =
                medicCollection.stream().map(m -> new MedicGetDto(m, uriInfo)).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicDtos.hashCode()));
        response = Response.ok(new GenericEntity<Collection<MedicGetDto>>(medicDtos) {
        })
                .type(MedicGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

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
    @Path("/filters/patient-email")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRelevantPatientEmails(
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE)
            @IntegerSize(min = MIN_PAGE, message = "Page number must be at least {min}")
                    Integer page,
            @QueryParam("per_page") @DefaultValue(DEFAULT_PAGE_SIZE)
            @IntegerSize(min = MIN_PAGE_SIZE, max = MAX_PAGE_SIZE, message = "Number of entries per page must be between {min} and {max}")
                    Integer perPage,
            @Valid @BeanParam OrderFilterDto orderFilterDto
    ) {
        Response.ResponseBuilder response;

        // we only want to fetch the orders that belong to the requesting user, or the ones they have access to (through sharing if medic)
        User user = getLoggedUser();
        if (user == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Collection<Integer> clinicUsers = orderFilterDto.getClinics();
        Collection<Integer> medicUsers = orderFilterDto.getMedics();
        Collection<String> patientEmails = orderFilterDto.getPatientEmails();
        LocalDate fromDate = orderFilterDto.getFromDate();
        LocalDate toDate = orderFilterDto.getToDate();
        Collection<Integer> studyTypes = orderFilterDto.getStudyTypes();
        boolean includeShared = orderFilterDto.isIncludeShared();

        long lastPage = orderService.patientEmailsFromFilteredOrdersLastPage(
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

        if (lastPage <= 0 || page > lastPage)
            return Response.noContent().build();

        Collection<String> patientEmailCollection = orderService.patientEmailsFromFilteredOrders(
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

        EntityTag entityTag = new EntityTag(Integer.toHexString(patientEmailCollection.hashCode()));
        Collection<PatientGetDto> patientDtos =
                patientEmailCollection.stream().map(p -> new PatientGetDto(p)).collect(Collectors.toList());
        response = Response.ok(new GenericEntity<Collection<PatientGetDto>>(patientDtos) {
        })
                .type(PatientGetDto.CONTENT_TYPE + "+json")
                .tag(entityTag);

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

    // auxiliary functions
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
