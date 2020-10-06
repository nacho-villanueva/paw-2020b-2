package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class MyStudiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private StudyTypeService studyService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private PatientService patientService;  //TO DO: Los medicos pueden filtrar por pacientes?
                                            // entonces necesitamos un pt.getAll()

    @Autowired
    private OrderService orderService;

    @RequestMapping("/my-studies")
    public ModelAndView myStudies() {
        final ModelAndView mav = new ModelAndView("my-studies");
        User user = loggedUser();
        mav.addObject("medicsList", medicService.getAll());
        mav.addObject("studiesList", studyService.getAll());
        mav.addObject("clinicsList", clinicService.getAll());
        mav.addObject("studies", orderService.getAllAsPatient(user));
        mav.addObject("encodeds", null);        //oh jeez...
        mav.addObject("patientsList", null);

        //si el user es patient, la lista de pacientes debe solo tenerlo a el
        //si el user es medic, la lista de medicos debe solo tenerlo a el
        //si el user es clinic, la lista solo debe tenerlo a el

        //si es medic, la lista de pacientes debe tener todos sus pacientes (no deberia tener los pacientes que no haya consultado)
        //same con clinic

        //si un user es role 4... deberia poder ver todo?
        //
        return mav;
    }


    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }
}
