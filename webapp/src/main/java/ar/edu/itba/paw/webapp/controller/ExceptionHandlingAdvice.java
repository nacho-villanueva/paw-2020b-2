package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    @Autowired
    private MessageSource messageSource;

    // occurs when dispatcher can't find a corresponding handler for requested domain
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandler(Locale locale) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorNumber",HttpStatus.NOT_FOUND.value());
        mav.addObject("errorText",messageSource.getMessage("error.404", null, locale));
        return mav;
    }

    // occurs on unhandled exceptions
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleInternalError(Locale locale) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorNumber",HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("errorText",messageSource.getMessage("error.500",null,locale));
        return mav;
    }
}
