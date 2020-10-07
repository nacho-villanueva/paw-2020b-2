package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.ClinicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.MedicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.StudyTypeNotFoundException;
import ar.edu.itba.paw.webapp.form.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Date;
import java.util.Optional;

@Controller
@RequestMapping(value = "/create-order")
public class OrderController {

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StudyTypeService studyService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService us;

    @Autowired
    private ValidationService vs;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getOrderCreationForm(@ModelAttribute("orderForm") OrderForm orderForm) {
        final ModelAndView mav = new ModelAndView("create-order");
        Medic m = null;
        if(medicService.findByUserId(loggedUser().getId()).isPresent())
            m = medicService.findByUserId(loggedUser().getId()).get();
        mav.addObject("loggedMedic", m);
        mav.addObject("studiesList", studyService.getAll());
        mav.addObject("clinicsList", clinicService.getAll());
        mav.addObject("orderForm", orderForm);
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

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("orderForm") OrderForm orderForm, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView errorMav = new ModelAndView("create-order");
            if(medicService.findByUserId(loggedUser().getId()).isPresent())
                errorMav.addObject("loggedMedic", medicService.findByUserId(loggedUser().getId()).get());
            return errorMav;

        } else {

            Optional<Medic> medic = medicService.findByUserId(loggedUser().getId());
            if(!medic.isPresent())
                throw new MedicNotFoundException();

            Optional<Clinic> clinic = clinicService.findByUserId(orderForm.getClinicId());
            if(!clinic.isPresent())
                throw new ClinicNotFoundException();
            Optional<StudyType> studyType = studyService.findById(orderForm.getStudyId());
            if(!studyType.isPresent())
                throw new StudyTypeNotFoundException();

            Order order = orderService.register(
                    medic.get(),
                    new Date(System.currentTimeMillis()),
                    clinic.get(),
                    orderForm.getPatientName(),
                    orderForm.getPatientEmail(),
                    studyType.get(),
                    orderForm.getDescription(),
                    medic.get().getIdentification_type(),
                    medic.get().getIdentification(),
                    orderForm.getPatient_insurance_plan(),
                    orderForm.getPatient_insurance_number());

            return new ModelAndView("redirect:view-study/" + urlEncoderService.encode(order.getOrder_id()));


        }

    }


}
