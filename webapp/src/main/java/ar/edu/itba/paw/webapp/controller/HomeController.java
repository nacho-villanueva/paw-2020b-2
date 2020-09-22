package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.UrlEncoderService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
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
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("registerUserForm", registerUserForm);
        mav.addObject("loginError", loginError);
        mav.addObject("registrationSuccess", registrationSuccess);
        return mav;
    }

    @RequestMapping("/home")
    public ModelAndView dashboard() {
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("loggedUser",loggedUser());

        mav = homeSetup(mav);

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



    private ModelAndView homeSetup(ModelAndView mav){
        Collection<Order> patient_studies, medic_studies, clinic_studies;
        Collection<String> patient_encodeds = null, medic_encodeds = null, clinic_encodeds = null;
        clinic_studies = os.getAllAsClinic(loggedUser());
        medic_studies = os.getAllAsMedic(loggedUser());
        patient_studies = os.getAllAsPatient(loggedUser());
        patient_encodeds = encoder(patient_studies, patient_encodeds);
        medic_encodeds = encoder(medic_studies, medic_encodeds);
        clinic_encodeds = encoder(clinic_studies, clinic_encodeds);
        mav.addObject("patient_studies", patient_studies);
        mav.addObject("medic_studies", medic_studies);
        mav.addObject("clinic_studies", clinic_studies);
        mav.addObject("patient_encodeds", patient_encodeds);
        mav.addObject("medic_encodeds", medic_encodeds);
        mav.addObject("clinic_encodeds", clinic_encodeds);
        boolean has_studies = false;
        if(patient_studies.size() + clinic_studies.size() + medic_studies.size() > 0){
            has_studies = true;
        }
        mav.addObject("has_studies", has_studies);
        return mav;

    }

    private Collection<String> encoder(Collection<Order> orders, Collection<String> encodeds){
        for(Order order : orders){
            encodeds.add(urlEncoderService.encode(order.getOrder_id()));
        }
        return encodeds;
    }
}
