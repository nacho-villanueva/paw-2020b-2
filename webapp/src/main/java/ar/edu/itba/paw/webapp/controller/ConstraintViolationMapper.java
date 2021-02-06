package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.ConstraintViolationDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
@Component
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Resource(name="annotationCodes")
    private Map<String,String> annotationCodes;

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(ConstraintViolationException e) {

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Collection<ConstraintViolation<?>> violations = e.getConstraintViolations();

        return Response.status(Response.Status.BAD_REQUEST).language(locale)
                .entity(new GenericEntity<Collection<ConstraintViolationDto>>
                        ( (violations.stream().map(this::createDto).collect(Collectors.toList())) ) {})
                .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();
    }

    private ConstraintViolationDto createDto(ConstraintViolation<?> violation){

        String code = getCode(violation);
        String message = solveMessage(violation);

        return new ConstraintViolationDto(violation,message,code);
    }

    private String solveMessage(ConstraintViolation<?> violation){
        return violation.getMessage();
    }

    private String getCode(ConstraintViolation<?> violation){
        String code = violation.getConstraintDescriptor().getAnnotation().annotationType().getName();
        return annotationCodes.getOrDefault(code,"other");
    }
}
