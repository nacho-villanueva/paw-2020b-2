package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.ClinicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.MedicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.StudyTypeNotFoundException;
import ar.edu.itba.paw.webapp.form.AdvancedSearchClinicForm;
import ar.edu.itba.paw.webapp.form.OrderForm;
import ar.edu.itba.paw.webapp.form.PatientInfoForm;
import ar.edu.itba.paw.webapp.form.constraintGroups.ExistingPatientGroup;
import ar.edu.itba.paw.webapp.form.constraintGroups.OrderGroup;
import ar.edu.itba.paw.webapp.form.constraintGroups.OrderWithoutClinicGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(value = "/create-order")
public class OrderController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService us;

    @RequestMapping(method = RequestMethod.GET,params={"submit!=search","submit!=reset"})
    public ModelAndView getOrderCreationForm(@ModelAttribute("orderForm") OrderForm orderForm) {
        final ModelAndView mav = new ModelAndView("create-order");
        Optional<Medic> m = medicService.findByUserId(loggedUser().getId());
        if (m.isPresent()){
            mav.addObject("loggedMedic", m.get());
        }else
            throw new MedicNotFoundException();
        orderForm.setMedicId(loggedUser().getId());
        mav.addObject("studiesList", studyTypeService.getAll());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=back","submit!=toClinicSelection","submit!=create-order","submit!=getExistingPatient"})
    public ModelAndView getOrderCreationFormBack(@ModelAttribute("orderForm") OrderForm orderForm) {
        final ModelAndView mav = new ModelAndView("create-order");
        Optional<Medic> m = medicService.findByUserId(loggedUser().getId());
        if (m.isPresent()){
            mav.addObject("loggedMedic", m.get());
        }else
            throw new MedicNotFoundException();
        orderForm.setMedicId(loggedUser().getId());
        mav.addObject("studiesList", studyTypeService.getAll());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=getExistingPatient","submit!=toClinicSelection","submit!=back","submit!=create-order"})
    public ModelAndView getExistingPatient(@Validated(ExistingPatientGroup.class) @ModelAttribute("orderForm") OrderForm orderForm, final BindingResult bindingResult){
        final ModelAndView mav = new ModelAndView("create-order");
        Optional<Medic> m = medicService.findByUserId(loggedUser().getId());
        if (m.isPresent()){
            mav.addObject("loggedMedic", m.get());
        }else
            throw new MedicNotFoundException();
        mav.addObject("studiesList", studyTypeService.getAll());

        // patient email is valid, therefore i need to load patient info
        if(!bindingResult.hasErrors()){
            Optional<Patient> patientOptional = patientService.findByEmail(orderForm.getPatientEmail());
            if(!patientOptional.isPresent()){throw new PatientNotFoundException();}

            Patient patient = patientOptional.get();

            orderForm.setPatientInsurancePlan(patient.getMedic_plan());
            orderForm.setPatientInsuranceNumber(patient.getMedic_plan_number());
            orderForm.setPatientName(patient.getName());
        }

        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=toClinicSelection","submit!=getExistingPatient","submit!=back","submit!=create-order"})
    public ModelAndView getClinicAdvancedSearch(@Validated(OrderWithoutClinicGroup.class) @ModelAttribute("orderForm") OrderForm orderForm, final BindingResult bindingResult,
                                                @ModelAttribute("advancedSearchClinicForm")AdvancedSearchClinicForm advancedSearchClinicForm, Locale locale){
        if (bindingResult.hasErrors()) {
            ModelAndView errorMav = new ModelAndView("create-order");
            if(medicService.findByUserId(loggedUser().getId()).isPresent())
                errorMav.addObject("loggedMedic", medicService.findByUserId(loggedUser().getId()).get());
            errorMav.addObject("studiesList", studyTypeService.getAll());
            errorMav.addObject("clinicsList", clinicService.getAll());
            return errorMav;
        }

        ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        // passing all the data
        advancedSearchClinicForm.setOrderForm(orderForm);

        advancedSearchClinicForm.setMedicalPlan(orderForm.getPatientInsurancePlan());
        advancedSearchClinicForm.setMedicalStudy(studyNameFromOrderForm(orderForm,locale));
        Collection<Clinic> clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                advancedSearchClinicForm.getClinicHours(),
                advancedSearchClinicForm.getMedicalPlan(),
                advancedSearchClinicForm.getMedicalStudy());

        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());
        mav.addObject("clinicsList",clinicsList);
        mav.addObject("studyName", studyNameFromOrderForm(orderForm,locale));


        return mav;
    }

    @RequestMapping(method = RequestMethod.GET,params = {"submit=search","submit!=reset"})
    public ModelAndView getOrderCreationForm(@Valid @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm, BindingResult bindingResult,
                                             @ModelAttribute("orderForm") OrderForm orderForm,
                                             HttpServletRequest httpServletRequest, Locale locale) {

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();
        advancedSearchClinicForm.decodeForm(uri);

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");
        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());

        orderForm.copyOrderForm(advancedSearchClinicForm.getOrderForm());

        Collection<Clinic> clinicsList;
        if(bindingResult.hasErrors()){
            mav.addObject("clinicsList",clinicService.getAll());

            mav.addObject("errorAlert",true);

            return mav;
        }

        advancedSearchClinicForm.setMedicalStudy(studyNameFromOrderForm(orderForm,locale));
        clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                advancedSearchClinicForm.getClinicHours(),
                advancedSearchClinicForm.getMedicalPlan(),
                advancedSearchClinicForm.getMedicalStudy());

        mav.addObject("clinicsList", clinicsList);
        mav.addObject("studyName", studyNameFromOrderForm(orderForm,locale));

        return mav;
    }

    @RequestMapping(method = RequestMethod.GET,params = {"submit=reset","submit!=search"})
    public ModelAndView getOrderCreationFormReset(@ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm,
                                                  @ModelAttribute("orderForm") OrderForm orderForm,
                                                  HttpServletRequest httpServletRequest, Locale locale) {

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();
        advancedSearchClinicForm.decodeForm(uri);

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());

        orderForm.copyOrderForm(advancedSearchClinicForm.getOrderForm());

        advancedSearchClinicForm.resetValues();

        advancedSearchClinicForm.setMedicalPlan(orderForm.getPatientInsurancePlan());
        advancedSearchClinicForm.setMedicalStudy(studyNameFromOrderForm(orderForm,locale));
        Collection<Clinic> clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                advancedSearchClinicForm.getClinicHours(),
                advancedSearchClinicForm.getMedicalPlan(),
                advancedSearchClinicForm.getMedicalStudy());

        mav.addObject("clinicsList", clinicsList);

        mav.addObject("studyName", studyNameFromOrderForm(orderForm,locale));

        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=create-order","submit!=back","submit!=toClinicSelection","submit!=getExistingPatient"})
    public ModelAndView createOrder(@Validated(OrderGroup.class) @ModelAttribute("orderForm") OrderForm orderForm, final BindingResult bindingResult,
                                    @ModelAttribute("advancedSearchClinicForm")AdvancedSearchClinicForm advancedSearchClinicForm){
        if(bindingResult.hasErrors()){

            // passing all the data
            advancedSearchClinicForm.setOrderForm(orderForm);

            ModelAndView mav = new ModelAndView("/advanced-search-clinic");

            mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());
            mav.addObject("clinicsList",clinicService.getAll());
            mav.addObject("clinicUnselected",true);

            return mav;
        }

        if(!orderForm.getMedicId().equals(loggedUser().getId()))
            return new ModelAndView("redirect:/403");

        // create order successful
        Optional<Medic> medicOptional = medicService.findByUserId(orderForm.getMedicId());
        Optional<Clinic> clinicOptional = clinicService.findByUserId(orderForm.getClinicId());
        Optional<StudyType> studyTypeOptional = studyTypeService.findById(orderForm.getStudyId());

        if(!medicOptional.isPresent()){throw new MedicNotFoundException();}
        if(!clinicOptional.isPresent()){throw new ClinicNotFoundException();}
        if(!studyTypeOptional.isPresent()){throw new StudyTypeNotFoundException();}


        Order order = orderService.register(
                medicOptional.get(),
                new Date(System.currentTimeMillis()),
                clinicOptional.get(),
                orderForm.getPatientEmail(),
                orderForm.getPatientName(),
                studyTypeOptional.get(),
                orderForm.getDescription(),
                medicOptional.get().getIdentification_type(),
                medicOptional.get().getIdentification(),
                orderForm.getPatientInsurancePlan(),
                orderForm.getPatientInsuranceNumber());

        return new ModelAndView("redirect:view-study/" + urlEncoderService.encode(order.getOrder_id()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView notFound() {
        return new ModelAndView("redirect:/400");
    }


    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }

    private String studyNameFromOrderForm(OrderForm orderForm, Locale locale){
        if(studyTypeService.findById(orderForm.getStudyId()).isPresent())
            return studyTypeService.findById(orderForm.getStudyId()).get().getName();
        return messageSource.getMessage("create-order.unknownStudyType",null,locale);
    }

}
