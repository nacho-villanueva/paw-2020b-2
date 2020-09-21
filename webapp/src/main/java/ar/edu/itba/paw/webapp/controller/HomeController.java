package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
public class HomeController {

    @Autowired
    private UserService us;

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
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
        final User user = us.findByUsername((String) auth.getName());   //Todo: Change function name to new function name in feature/allow-user-to-register
        //LOGGER.debug("Logged user is {}", user);
        return user;
    }
}
