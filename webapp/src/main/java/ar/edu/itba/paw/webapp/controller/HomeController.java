package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView home(@ModelAttribute("registerUserForm") RegisterUserForm registerUserForm) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("registerUserForm", registerUserForm);
        return mav;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request" + e.getMessage());
    }
}
