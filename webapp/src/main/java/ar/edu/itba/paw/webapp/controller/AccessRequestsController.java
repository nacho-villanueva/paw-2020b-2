package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.ShareRequest;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.MedicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.StudyTypeNotFoundException;
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
public class AccessRequestsController {

    @Autowired
    private UserService userService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private ShareRequestService shareRequestService;

    @RequestMapping(value = "/access-requests", method = RequestMethod.GET)
    public ModelAndView accessRequests(@ModelAttribute("requestOrdersForm") RequestOrdersForm requestOrdersForm,
                                       @RequestParam(value = "s", required = false) boolean creationSuccesful){
        final ModelAndView mav = new ModelAndView("access-requests");
        mav.addObject("requestsList", shareRequestService.getAllPatientRequest(loggedUser().getEmail()));
        mav.addObject("success", creationSuccesful);
        return mav;
    }

    @RequestMapping(value = "/access-request/view", method = RequestMethod.GET)
    public ModelAndView requestOrderView(@Valid @ModelAttribute("requestOrdersForm") RequestOrdersForm requestOrdersForm, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return new ModelAndView("redirect:/404");

        if(!loggedUser().getEmail().equals(requestOrdersForm.getPatientEmail()))
            return new ModelAndView("redirect:/403");

        // check if it exists

        Optional<Medic> medicOptional = medicService.findByUserId(requestOrdersForm.getMedicId());
        Optional<StudyType> studyTypeOptional = studyTypeService.findById(requestOrdersForm.getStudyTypeId());

        if(medicOptional.isPresent() && studyTypeOptional.isPresent()) {

            ShareRequest shareRequest = new ShareRequest(medicOptional.get(),requestOrdersForm.getPatientEmail(),studyTypeOptional.get());
            if(!shareRequestService.requestExists(shareRequest)){
                return new ModelAndView("redirect:/404");
            }

            final ModelAndView mav = new ModelAndView("access-request");
            mav.addObject("medic",medicOptional.get());
            mav.addObject("studyType",studyTypeOptional.get());
            return mav;
        }

        if(!medicOptional.isPresent()){
            throw new MedicNotFoundException();
        }else {
            // study Type not found
            throw  new StudyTypeNotFoundException();
        }

    }

    @RequestMapping(value = "access-request/complete", method = RequestMethod.POST)
    public ModelAndView requestOrderComplete(@Valid @ModelAttribute("requestOrdersForm") RequestOrdersForm requestOrdersForm, BindingResult bindingResult,
                                             @RequestParam(value = "submit", required = true) boolean accept){
        if(bindingResult.hasErrors())
            return new ModelAndView("redirect:/404");

        if(!loggedUser().getEmail().equals(requestOrdersForm.getPatientEmail()))
            return new ModelAndView("redirect:/403");


        Optional<Medic> medicOptional = medicService.findByUserId(requestOrdersForm.getMedicId());
        Optional<StudyType> studyTypeOptional = studyTypeService.findById(requestOrdersForm.getStudyTypeId());

        if(medicOptional.isPresent() && studyTypeOptional.isPresent()){
            ShareRequest shareRequest = new ShareRequest(medicOptional.get(),requestOrdersForm.getPatientEmail(),studyTypeOptional.get());

            if(shareRequestService.requestExists(shareRequest)){
                shareRequestService.acceptOrDenyShare(shareRequest,accept);

                return new ModelAndView("redirect:/access-requests?s=true");
            }
        }

        // request didn't exist
        return new ModelAndView("redirect:/404");
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
