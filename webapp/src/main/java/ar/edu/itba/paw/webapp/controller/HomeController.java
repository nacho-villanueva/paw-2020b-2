package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserService us;

    @RequestMapping("/")
    public ModelAndView home(@ModelAttribute("registerUserForm") RegisterUserForm registerUserForm,
                             @RequestParam(value = "error", required = false) boolean loginError,
                             @RequestParam(value = "registrationSuccess", required = false) boolean registrationSuccess) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("registerUserForm", registerUserForm);
        mav.addObject("loginError", loginError);
        mav.addObject("registrationSuccess", registrationSuccess);
        return mav;
    }

    @RequestMapping("/home")
    public ModelAndView dashboard() {
        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("loggedUser",loggedUser());
        return mav;
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }
}
