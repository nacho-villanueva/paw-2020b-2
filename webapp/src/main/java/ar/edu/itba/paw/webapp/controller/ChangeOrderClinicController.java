package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.ClinicNotFoundException;
import ar.edu.itba.paw.webapp.form.AdvancedSearchClinicForm;
import ar.edu.itba.paw.webapp.form.ChangeOrderClinicForm;
import ar.edu.itba.paw.webapp.form.constraintGroups.AdvancedSearchGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Controller
public class ChangeOrderClinicController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @RequestMapping(value="change-clinic/{encodedId}", method = RequestMethod.GET)
    public ModelAndView getClinicChangeView(@PathVariable("encodedId") final String encodedId, @ModelAttribute("changeOrderClinicForm") ChangeOrderClinicForm changeOrderClinicForm,
                                            @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm){
        ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        Optional<Order> maybeOrder = orderService.findById(urlEncoderService.decode(encodedId));
        if (!maybeOrder.isPresent())
            return new ModelAndView("redirect:/404");
        Order order = maybeOrder.get();

        Collection<Clinic> clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                advancedSearchClinicForm.getClinicHours(),
                advancedSearchClinicForm.getMedicalPlan(),
                advancedSearchClinicForm.getMedicalStudy());

        mav.addObject("daysOfWeek", ClinicHours.getDaysOfWeek());
        mav.addObject("clinicsList",clinicsList);
        mav.addObject("changeClinic", true);
        mav.addObject("encodedId", encodedId);
        mav.addObject("changeClinicOrder", order);


        return mav;
    }

    @RequestMapping(value="change-clinic/{encodedId}", method = RequestMethod.GET,params = {"submit=search"})
    public ModelAndView changeOrderSearchClinic(@Validated(AdvancedSearchGroup.class) @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm, BindingResult bindingResult,
                                                @PathVariable("encodedId") final String encodedId, @ModelAttribute("changeOrderClinicForm") ChangeOrderClinicForm changeOrderClinicForm,
                                                HttpServletRequest httpServletRequest) {

        Optional<Order> maybeOrder = orderService.findById(urlEncoderService.decode(encodedId));
        if (!maybeOrder.isPresent())
            return new ModelAndView("redirect:/404");
        Order order = maybeOrder.get();

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();
        advancedSearchClinicForm.decodeForm(uri);

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");
        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());

        mav.addObject("changeClinic", true);
        mav.addObject("encodedId", encodedId);
        mav.addObject("changeClinicOrder", order);

        if(bindingResult.hasErrors()){
            mav.addObject("clinicsList",clinicService.getAll());
            mav.addObject("errorAlert",true);

            return mav;
        }

        Collection<Clinic> clinicsList;
        clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                advancedSearchClinicForm.getClinicHours(),
                advancedSearchClinicForm.getMedicalPlan(),
                advancedSearchClinicForm.getMedicalStudy());
        mav.addObject("clinicsList", clinicsList);

        return mav;
    }

    @RequestMapping(value="change-clinic/{encodedId}", method = RequestMethod.GET,params = {"submit=reset","submit!=submit"})
    public ModelAndView changeOrderResetFilter(@PathVariable("encodedId") final String encodedId, @ModelAttribute("changeOrderClinicForm") ChangeOrderClinicForm changeOrderClinicForm,
                                               @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm,
                                             HttpServletRequest httpServletRequest) {

        Optional<Order> maybeOrder = orderService.findById(urlEncoderService.decode(encodedId));
        if (!maybeOrder.isPresent())
            return new ModelAndView("redirect:/404");
        Order order = maybeOrder.get();

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();
        advancedSearchClinicForm.decodeForm(uri);

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");
        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());

        advancedSearchClinicForm.resetValues();

        Collection<Clinic> clinicsList;
        clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                advancedSearchClinicForm.getClinicHours(),
                advancedSearchClinicForm.getMedicalPlan(),
                advancedSearchClinicForm.getMedicalStudy());

        mav.addObject("clinicsList", clinicsList);
        mav.addObject("changeClinic", true);
        mav.addObject("encodedId", encodedId);
        mav.addObject("changeClinicOrder", order);

        return mav;
    }

    @RequestMapping(value="change-clinic/{encodedId}", method = RequestMethod.POST)
    public ModelAndView postChangeClinic(@PathVariable("encodedId") final String encodedId, @Valid @ModelAttribute("changeOrderClinicForm") ChangeOrderClinicForm changeOrderClinicForm, BindingResult bindingResult,
                                         @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm){

        Optional<Order> maybeOrder = orderService.findById(urlEncoderService.decode(encodedId));
        if (!maybeOrder.isPresent())
            return new ModelAndView("redirect:/404");
        Order order = maybeOrder.get();

        if(bindingResult.hasErrors()){
            final ModelAndView mavError = new ModelAndView("/advanced-search-clinic");
            Collection<Clinic> clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                    advancedSearchClinicForm.getClinicHours(),
                    advancedSearchClinicForm.getMedicalPlan(),
                    advancedSearchClinicForm.getMedicalStudy());

            mavError.addObject("daysOfWeek", ClinicHours.getDaysOfWeek());
            mavError.addObject("clinicsList",clinicsList);
            mavError.addObject("changeClinic", true);
            mavError.addObject("encodedId", encodedId);
            mavError.addObject("changeClinicOrder", order);
            return mavError;
        }



        // Verify logged user is the owner of the order
        if (!order.getPatientEmail().equals(loggedUser().getEmail()))
            return new ModelAndView("redirect:/");

        // Verify clinic exists
        Optional<Clinic> maybeClinic = clinicService.findByUserId(changeOrderClinicForm.getChangeClinicId());
        if(!maybeClinic.isPresent())
            throw new ClinicNotFoundException();

        // Verify the order does not have uploaded results
        if(!resultService.findByOrderId(order.getOrderId()).isEmpty())
            return new ModelAndView("redirect:/view-study/"+ encodedId);

        orderService.changeOrderClinic(order, maybeClinic.get());
        return new ModelAndView("redirect:/view-study/"+encodedId);
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail(auth.getName());
        return user.orElse(null);
    }

}
