package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.services.ShareRequestService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.RequestOrdersForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class RequestOrdersController {

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private ShareRequestService shareRequestService;

    @Autowired
    private UserService us;

    @Autowired
    private MedicService medicService;

    @RequestMapping(value = "/request-orders", method = RequestMethod.GET)
    public ModelAndView requestOrders(@ModelAttribute("requestOrdersForm") RequestOrdersForm requestOrdersForm,
                                      @RequestParam(value = "s", required = false) boolean creationSuccesful){
        final ModelAndView mav = new ModelAndView("request-orders");
        mav.addObject("studiesList", studyTypeService.getAll());
        mav.addObject("success", creationSuccesful);
        return mav;
    }

    @RequestMapping(value = "/request-orders", method = RequestMethod.POST)
    public ModelAndView requestOrders(@Valid @ModelAttribute("requestOrdersForm") RequestOrdersForm requestOrdersForm,
                                      final BindingResult bindingResult){
        if(!bindingResult.hasErrors()) {
            Optional<Medic> medic = medicService.findByUserId(loggedUser().getId());
            Optional<StudyType> studyType = studyTypeService.findById(requestOrdersForm.getStudyId());
            if(medic.isPresent() && studyType.isPresent()){
                shareRequestService.requestShare(medic.get(), requestOrdersForm.getPatientEmail(), studyType.get());

                return new ModelAndView("redirect:/request-orders?s=true");
            }

        }

        return new ModelAndView("request-orders");
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
