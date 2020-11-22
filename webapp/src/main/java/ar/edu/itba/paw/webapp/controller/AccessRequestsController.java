package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ShareRequestService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.RequestOrdersForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class AccessRequestsController {

    @Autowired
    private UserService us;

    @Autowired
    private ShareRequestService shareRequestService;

    @RequestMapping(value = "/access-requests", method = RequestMethod.GET)
    public ModelAndView accessRequests(@ModelAttribute("requestOrdersForm") RequestOrdersForm requestOrdersForm){
        final ModelAndView mav = new ModelAndView("access-requests");
        mav.addObject("requestsList", shareRequestService.getAllPatientRequest(loggedUser().getEmail()));
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
