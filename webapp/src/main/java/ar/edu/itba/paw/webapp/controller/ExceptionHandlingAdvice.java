package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    // occurs when dispatcher can't find a corresponding handler for requested domain
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandler() {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorNumber",HttpStatus.NOT_FOUND.value());
        mav.addObject("errorText","Page Not Found");
        return mav;
    }

    // occurs on unhandled exceptions
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleInternalError() {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorNumber",HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("errorText",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return mav;
    }
}
