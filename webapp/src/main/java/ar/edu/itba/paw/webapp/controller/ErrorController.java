package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @RequestMapping("/403")
    @ResponseStatus(code=HttpStatus.FORBIDDEN)
    public ModelAndView forbidden() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorNumber", HttpStatus.FORBIDDEN.value());
        mav.addObject("errorText","You seem to not have the permission to enter this site, please enter with the proper user");
        return mav;
    }
}
