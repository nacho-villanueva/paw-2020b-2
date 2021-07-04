package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.MedicPlan;
import ar.edu.itba.paw.services.MedicPlanService;
import ar.edu.itba.paw.webapp.dto.MedicPlanDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Path("medic-plans")
@Component
public class MedicPlanController {

    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private MedicPlanService medicPlanService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response listMedicPlans() {
        Collection<MedicPlan> medicPlans = medicPlanService.getAll();

        if(medicPlans.isEmpty())
            return Response.noContent().build();

        Collection<MedicPlanDto> medicPlanDtos = medicPlans.stream().map(p -> (new MedicPlanDto(p,uriInfo))).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicPlanDtos.hashCode()));
        return Response.ok(new GenericEntity<Collection<MedicPlanDto>>(medicPlanDtos) {})
                .type(MedicPlanDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cacheControl).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response getPlanById(@PathParam("id") final int id) {
        Optional<MedicPlan> medicPlanOptional = medicPlanService.findById(id);

        if(!medicPlanOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        MedicPlanDto medicPlanDto = new MedicPlanDto(medicPlanOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicPlanDto.hashCode()));
        CacheControl cache = new CacheControl();
        cache.setMaxAge(Math.toIntExact(TimeUnit.DAYS.toSeconds(7)));
        return Response.ok(medicPlanDto).type(MedicPlanDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cache).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON, MedicPlanDto.CONTENT_TYPE+"+json"})
    public Response registerMedicPlan(@Valid @NotNull MedicPlanDto medicPlanDto) {
        MedicPlan newPlan = medicPlanService.register(medicPlanDto.getName());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(newPlan.getId())).build()).build();
    }
}
