package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.UrlEncoderService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserService us;

    @Autowired
    private OrderService os;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @RequestMapping("/")
    public ModelAndView home(@ModelAttribute("registerUserForm") RegisterUserForm registerUserForm,
                             @RequestParam(value = "error", required = false) boolean loginError,
                             @RequestParam(value = "registrationSuccess", required = false) boolean registrationSuccess) {
        if(isLoggedUser()) {
            return new ModelAndView("redirect:/home");
        }

        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("loginError", loginError);
        mav.addObject("registrationSuccess", registrationSuccess);

        mav.addObject("registerUserForm", registerUserForm);
        mav.addObject("patientRoleID",User.PATIENT_ROLE_ID);
        mav.addObject("medicRoleID",User.MEDIC_ROLE_ID);
        mav.addObject("clinicRoleID",User.CLINIC_ROLE_ID);
        return mav;
    }

    @RequestMapping("/home")
    public ModelAndView dashboard() {
        if(!loggedUser().isRegistered())
            return new ModelAndView("redirect:/complete-registration");

        ModelAndView mav = new ModelAndView("home");
        mav.addObject("loggedUser",loggedUser());
        mav = homeSetup(mav);
        return mav;
    }

    private boolean isLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }



    private ModelAndView homeSetup(ModelAndView mav){
        Collection<Order> orders = os.getAllAsUser(loggedUser());;

        Map<Long, String> ordersEncoded = new HashMap<>();
        encoder(orders, ordersEncoded);
        mav.addObject("orders", orders);
        mav.addObject("ordersEncoded", ordersEncoded);
        mav.addObject("hasStudies", !orders.isEmpty());

        return mav;

    }

    private Map<Long, String> encoder(Collection<Order> orders, Map<Long, String> encodeds){
        for(Order order : orders){
            encodeds.put(order.getOrderId(), urlEncoderService.encode(order.getOrderId()));
        }
        return encodeds;
    }
}
