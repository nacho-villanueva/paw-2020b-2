package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.ConstraintViolationDto;
import ar.edu.itba.paw.webapp.dto.MedicGetDto;
import ar.edu.itba.paw.webapp.dto.OrderGetDto;
import ar.edu.itba.paw.webapp.dto.OrderPostAndPutDto;
import ar.edu.itba.paw.webapp.dto.constraintGroups.OrderPostGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

@Path("orders")
@Component
public class OrderController {

    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private Validator validator;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

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

    @GET
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, OrderGetDto.CONTENT_TYPE})
    public Response getOrders(){
        // TODO: implement
        return Response.status(501).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, OrderGetDto.CONTENT_TYPE})
    public Response getOrderWithId(@PathParam("id") final String id){

        Response.ResponseBuilder response;
        long orderId;

        try {
            orderId = urlEncoderService.decode(id);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Order> orderOptional = orderService.findById(orderId);
        if(!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Order order = orderOptional.get();
        String encodedPath = urlEncoderService.encode(order.getOrderId());
        OrderGetDto orderGetDto = new OrderGetDto(order,encodedPath,uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(orderGetDto.hashCode()));
        response = Response.ok(orderGetDto).tag(entityTag).cacheControl(cacheControl);

        return response.build();
    }

    @GET
    @Path("/{id}/identification")
    @Produces(value = { "image/*"})
    public Response getOrderIdentification(@PathParam("id") final String id){

        long orderId;

        try {
            orderId = urlEncoderService.decode(id);
        }catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Order> orderOptional = orderService.findById(orderId);
        if(!orderOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        Order order = orderOptional.get();

        String contentType = order.getIdentificationType();
        ByteArrayInputStream identification = new ByteArrayInputStream(order.getIdentification());
        EntityTag entityTag = new EntityTag(Integer.toHexString(Arrays.hashCode(order.getIdentification())));

        return Response.ok(identification).header("Content-Type",contentType)
                .cacheControl(cacheControl).tag(entityTag)
                .build();
    }

    @GET
    @Path("{id}/shared-with")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicGetDto.CONTENT_TYPE})
    public Response getMedicsSharedWith(@PathParam("id") final String id){
        // TODO: implement
        // only affected patient should be able to do this action
        return Response.status(501).build();
    }

    @POST
    @Path("{id}/shared-with")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response addMedicsSharedWith(@PathParam("id") final String id){
        // TODO: implement
        // only affected patient should be able to do this action
        return Response.status(501).build();
    }

    @POST
    @Path("/")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response createOrder(
            @Valid OrderPostAndPutDto orderPostAndPutDto,
            @Context HttpHeaders headers
    ){
        Response.ResponseBuilder response;

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Set<ConstraintViolation<OrderPostAndPutDto>> violations = validator.validate(orderPostAndPutDto, OrderPostGroup.class);

        if(!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).language(locale)
                    .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations.stream().map(vc -> (new ConstraintViolationDto(vc,messageSource.getMessage(vc.getMessage(),null,locale)))).collect(Collectors.toList())) ) {})
                    .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();

        // no errors
        final URI uri;

        // TODO: GET THE LOGGEDIN USER, OR ELSE RESPOND WITH ERROR
        User user = userService.findById(2).get();

        Optional<Medic> medicOptional = medicService.findByUserId(user.getId());
        if(!medicOptional.isPresent() || !medicOptional.get().isVerified())
            return Response.status(Response.Status.FORBIDDEN).build();

        Optional<Clinic> clinicOptional = clinicService.findByUserId(orderPostAndPutDto.getClinicId());
        if(!clinicOptional.isPresent() || !clinicOptional.get().isVerified())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Optional<StudyType> studyTypeOptional = studyTypeService.findById(orderPostAndPutDto.getStudyTypeId());
        if(!studyTypeOptional.isPresent())
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        Medic medic = medicOptional.get();
        LocalDate localDate = LocalDate.now();
        Clinic clinic = clinicOptional.get();
        String patientEmail = orderPostAndPutDto.getPatientEmail();
        String patientName = orderPostAndPutDto.getPatientName();
        StudyType studyType = studyTypeOptional.get();
        String description = orderPostAndPutDto.getDescription();
        String identificationType = medic.getIdentificationType();
        byte[] identification = medic.getIdentification();
        String medicPlan = (orderPostAndPutDto.getMedicPlan()==null)? null : orderPostAndPutDto.getMedicPlan().getPlan();
        String medicPlanNumber = (orderPostAndPutDto.getMedicPlan()==null)? null : orderPostAndPutDto.getMedicPlan().getNumber();

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
        uri = uriInfo.getAbsolutePathBuilder().path(encodedPath).build();
        response = Response.created(uri);

        return response.build();
    }

    // auxiliar functions
    private boolean isEmpty(String s){
        return s==null || s.trim().length()==0;
    }
}
