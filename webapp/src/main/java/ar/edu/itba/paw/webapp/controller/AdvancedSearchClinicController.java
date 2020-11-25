package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.AdvancedSearchClinicForm;
import ar.edu.itba.paw.webapp.form.constraintGroups.AdvancedSearchGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping(value="advanced-search/clinic")
public class AdvancedSearchClinicController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private ClinicService clinicService;

    @RequestMapping(method = RequestMethod.GET,params = {"submit!=search"})
    public ModelAndView getAllClinics(@ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm) {


        final ModelAndView mav = new ModelAndView("advanced-search-clinic");

        advancedSearchClinicForm.resetValues();

        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());
        mav.addObject("clinicsList",clinicService.getAll());

        if(loggedUser() == null){
            mav.addObject("notLogged", true);
        }
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET,params = {"submit=search"})
    public ModelAndView getClinicsWithSearch(@Validated(AdvancedSearchGroup.class) @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm, BindingResult bindingResult,
                                             HttpServletRequest httpServletRequest) {

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();

        advancedSearchClinicForm.decodeForm(uri);

        final ModelAndView mav = new ModelAndView("advanced-search-clinic");

        Collection<Clinic> clinicsList = clinicService.getAll();

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            mav.addObject("errorAlert",true);
        } else {

            clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                    advancedSearchClinicForm.getClinicHours(),
                    advancedSearchClinicForm.getMedicalPlan(),
                    advancedSearchClinicForm.getMedicalStudy());
        }

        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());
        mav.addObject("clinicsList",clinicsList);

        if(loggedUser() == null){
            mav.addObject("notLogged", true);
        }

        return mav;
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail(auth.getName());
        return user.orElse(null);
    }

    private String decode(String value){
        try {
            return UriUtils.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        return value;
    }

}
