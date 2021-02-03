package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.ConstraintViolationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

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
import java.util.stream.Collectors;

@Provider
@Component
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Autowired
    private MessageSource messageSource;

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(ConstraintViolationException e) {

        Locale locale = (headers.getAcceptableLanguages().isEmpty())?(Locale.getDefault()):headers.getAcceptableLanguages().get(0);

        Collection<ConstraintViolation<?>> violations = e.getConstraintViolations();

        return Response.status(Response.Status.BAD_REQUEST).language(locale)
                .entity(new GenericEntity<Collection<ConstraintViolationDto>>( (violations.stream().map(vc -> (new ConstraintViolationDto(vc,solveMessage(vc,locale)))).collect(Collectors.toList())) ) {})
                .type(ConstraintViolationDto.CONTENT_TYPE+"+json").build();
    }

    private String solveMessage(ConstraintViolation<?> template, Locale locale){

        String message;
        try{
            message = messageSource.getMessage(template.getMessage(),null,locale);
        }catch (NoSuchMessageException e){
            message = template.getMessage();
        }

        return message;
    }
}
