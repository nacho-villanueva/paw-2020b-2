package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class ErrorController {

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/403")
    @ResponseStatus(code=HttpStatus.FORBIDDEN)
    public ModelAndView forbidden(Locale locale) {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorNumber", HttpStatus.FORBIDDEN.value());
        mav.addObject("errorText",messageSource.getMessage("error.403",null,locale));
        return mav;
    }
}
