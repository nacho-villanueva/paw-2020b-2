package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.services.MedicalFieldService;
import ar.edu.itba.paw.webapp.dto.MedicalFieldDto;
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

@Path("medical-fields")
@Component
public class MedicalFieldController {
    // default cache
    @Autowired
    private CacheControl cacheControl;

    @Autowired
    private MedicalFieldService medicalFieldService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE+"+json",})
    public Response listMedicalFields() {
        Collection<MedicalField> medicalFields = medicalFieldService.getAll();

        if(medicalFields.isEmpty())
            return Response.noContent().build();

        Collection<MedicalFieldDto> medicalFieldDtos = (medicalFields.stream().map(mf -> (new MedicalFieldDto(mf,uriInfo))).collect(Collectors.toList()));
        EntityTag etag = new EntityTag(Integer.toHexString(medicalFieldDtos.hashCode()));
        return Response.ok(new GenericEntity<Collection<MedicalFieldDto>>( medicalFieldDtos ) {})
                .type(MedicalFieldDto.CONTENT_TYPE+"+json")
                .tag(etag).cacheControl(cacheControl).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE+"+json",})
    public Response getMedicalFieldById(@PathParam("id") final int id){

        Optional<MedicalField> medicalFieldOptional = medicalFieldService.findById(id);

        if (!medicalFieldOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        MedicalFieldDto medicalFieldDto = new MedicalFieldDto(medicalFieldOptional.get(),uriInfo);
        EntityTag entityTag = new EntityTag(Integer.toHexString(medicalFieldDto.hashCode()));
        // Immutable resource
        CacheControl cache = new CacheControl();
        cache.setMaxAge(Math.toIntExact(TimeUnit.DAYS.toSeconds(7)));

        return Response.ok(medicalFieldDto).type(MedicalFieldDto.CONTENT_TYPE+"+json")
                .tag(entityTag).cacheControl(cache).build();
    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, MedicalFieldDto.CONTENT_TYPE+"+json",})
    public Response registerMedicalField(@Valid @NotNull MedicalFieldDto medicalFieldDto){
        final MedicalField medicalField = medicalFieldService.register(medicalFieldDto.getName());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(medicalField.getId())).build()).build();
    }
}
