package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.MedicalField;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.MedicalFieldService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.CreateTypeForm;
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
public class CreateTypesController {

    @Autowired
    private UserService us;

    @Autowired
    private StudyTypeService sts;

    @Autowired
    private MedicalFieldService mfs;

    @RequestMapping(value = "/create-type", method = RequestMethod.GET)
    public ModelAndView getCreateTypes(@ModelAttribute("createTypeForm") CreateTypeForm createTypeForm,
                                       @RequestParam(value = "s", required = false) boolean creationSuccesful){
        ModelAndView mav = new ModelAndView("create-type");
        mav.addObject("createTypeForm", createTypeForm);
        mav.addObject("success", creationSuccesful);
        return mav;
    }

    @RequestMapping(value = "/create-type", method = RequestMethod.POST)
    public ModelAndView postCreateTypes(@Valid @ModelAttribute("createTypeForm")CreateTypeForm createTypeForm, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ModelAndView("create-type");

        sts.findOrRegister(createTypeForm.getName());

        return new ModelAndView("redirect:/create-type?s=true");
    }

    @RequestMapping(value = "/create-field", method = RequestMethod.GET)
    public ModelAndView getCreateField(@ModelAttribute("createTypeForm") CreateTypeForm createTypeForm,
                                       @RequestParam(value = "s", required = false) boolean creationSuccesful){
        ModelAndView mav = new ModelAndView("create-field");
        mav.addObject("createTypeForm", createTypeForm);
        mav.addObject("success", creationSuccesful);
        return mav;
    }

    @RequestMapping(value = "/create-field", method = RequestMethod.POST)
    public ModelAndView postCreateField(@Valid @ModelAttribute("createTypeForm")CreateTypeForm createTypeForm, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ModelAndView("create-field");

        mfs.findOrRegister(createTypeForm.getName());

        return new ModelAndView("redirect:/create-field?s=true");
    }

    @RequestMapping(value = "/create-field-return", method = RequestMethod.GET)
    public ModelAndView returnFromCreateField() {
        User loggedUser = loggedUser();

        if(loggedUser == null) {
            return new ModelAndView("redirect:/");
        } else if (loggedUser.isUndefined()) {
            return new ModelAndView("redirect:/complete-registration");
        } else if (loggedUser.isMedic()) {
            return new ModelAndView("redirect:/profile/edit/medic");
        }

        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/create-type-return", method = RequestMethod.GET)
    public ModelAndView returnFromCreateType() {
        User loggedUser = loggedUser();

        if(loggedUser == null) {
            return new ModelAndView("redirect:/");
        } else if (loggedUser.isUndefined()) {
            return new ModelAndView("redirect:/complete-registration");
        } else if (loggedUser.isClinic()) {
            return new ModelAndView("redirect:/profile/edit/clinic");
        }

        return new ModelAndView("redirect:/home");
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
