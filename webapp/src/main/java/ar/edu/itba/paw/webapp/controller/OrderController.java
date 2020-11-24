package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.ClinicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.MedicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.StudyTypeNotFoundException;
import ar.edu.itba.paw.webapp.form.AdvancedSearchClinicForm;
import ar.edu.itba.paw.webapp.form.OrderForm;
import ar.edu.itba.paw.webapp.form.OrderWithoutClinicForm;
import ar.edu.itba.paw.webapp.form.PatientInfoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public ModelAndView getOrderCreationForm(@ModelAttribute("orderWithoutClinicForm") OrderWithoutClinicForm orderWithoutClinicForm) {
        final ModelAndView mav = new ModelAndView("create-order");
        Optional<Medic> m = medicService.findByUserId(loggedUser().getId());
        if (m.isPresent()){
            mav.addObject("loggedMedic", m.get());
        }else
            throw new MedicNotFoundException();
        orderWithoutClinicForm.setMedicId(loggedUser().getId());
        mav.addObject("studiesList", studyTypeService.getAll());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=back","submit!=toClinicSelection","submit!=create-order"})
    public ModelAndView getOrderCreationFormBack(@ModelAttribute("orderWithoutClinicForm") OrderWithoutClinicForm orderWithoutClinicForm) {
        final ModelAndView mav = new ModelAndView("create-order");
        Optional<Medic> m = medicService.findByUserId(loggedUser().getId());
        if (m.isPresent()){
            mav.addObject("loggedMedic", m.get());
        }else
            throw new MedicNotFoundException();
        orderWithoutClinicForm.setMedicId(loggedUser().getId());
        mav.addObject("studiesList", studyTypeService.getAll());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=toClinicSelection"})
    public ModelAndView getClinicAdvancedSearch(@Valid @ModelAttribute("orderWithoutClinicForm") OrderWithoutClinicForm orderWithoutClinicForm, final BindingResult bindingResult,
                                                @ModelAttribute("advancedSearchClinicForm")AdvancedSearchClinicForm advancedSearchClinicForm,
                                                @ModelAttribute("orderForm") OrderForm orderForm, Locale locale){
        if (bindingResult.hasErrors()) {
            ModelAndView errorMav = new ModelAndView("create-order");
            if(medicService.findByUserId(loggedUser().getId()).isPresent())
                errorMav.addObject("loggedMedic", medicService.findByUserId(loggedUser().getId()).get());
            errorMav.addObject("studiesList", studyTypeService.getAll());
            errorMav.addObject("clinicsList", clinicService.getAll());
            return errorMav;
        }

        ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        fillPatientInfo(orderWithoutClinicForm.getPatientInfo());

        // passing all the data
        advancedSearchClinicForm.setMedicId(orderWithoutClinicForm.getMedicId());
        advancedSearchClinicForm.setStudyId(orderWithoutClinicForm.getStudyId());
        advancedSearchClinicForm.setDescription(orderWithoutClinicForm.getDescription());
        advancedSearchClinicForm.setPatientInfo(orderWithoutClinicForm.getPatientInfo());

        orderForm.setMedicId(orderWithoutClinicForm.getMedicId());
        orderForm.setStudyId(orderWithoutClinicForm.getStudyId());
        orderForm.setDescription(orderWithoutClinicForm.getDescription());
        orderForm.setPatientInfo(orderWithoutClinicForm.getPatientInfo());

        advancedSearchClinicForm.setMedicalPlan(orderForm.getPatientInfo().getInsurancePlan());
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
                                             @ModelAttribute("orderForm") OrderForm orderForm, @ModelAttribute("orderWithoutClinicForm") OrderWithoutClinicForm orderWithoutClinicForm,
                                             HttpServletRequest httpServletRequest, Locale locale) {

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();
        advancedSearchClinicForm.decodeForm(uri);

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");
        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());

        orderForm.setMedicId(advancedSearchClinicForm.getMedicId());
        orderForm.setStudyId(advancedSearchClinicForm.getStudyId());
        orderForm.setDescription(advancedSearchClinicForm.getDescription());
        orderForm.setPatientInfo(advancedSearchClinicForm.getPatientInfo());

        orderWithoutClinicForm.setMedicId(advancedSearchClinicForm.getMedicId());
        orderWithoutClinicForm.setStudyId(advancedSearchClinicForm.getStudyId());
        orderWithoutClinicForm.setDescription(advancedSearchClinicForm.getDescription());
        orderWithoutClinicForm.setPatientInfo(advancedSearchClinicForm.getPatientInfo());

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
                                                  @ModelAttribute("orderForm") OrderForm orderForm, @ModelAttribute("orderWithoutClinicForm") OrderWithoutClinicForm orderWithoutClinicForm,
                                                  HttpServletRequest httpServletRequest, Locale locale) {

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();
        advancedSearchClinicForm.decodeForm(uri);

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());

        advancedSearchClinicForm.resetValues();

        orderForm.setMedicId(advancedSearchClinicForm.getMedicId());
        orderForm.setStudyId(advancedSearchClinicForm.getStudyId());
        orderForm.setDescription(advancedSearchClinicForm.getDescription());
        orderForm.setPatientInfo(advancedSearchClinicForm.getPatientInfo());

        orderWithoutClinicForm.setMedicId(advancedSearchClinicForm.getMedicId());
        orderWithoutClinicForm.setStudyId(advancedSearchClinicForm.getStudyId());
        orderWithoutClinicForm.setDescription(advancedSearchClinicForm.getDescription());
        orderWithoutClinicForm.setPatientInfo(advancedSearchClinicForm.getPatientInfo());

        advancedSearchClinicForm.setMedicalPlan(orderForm.getPatientInfo().getInsurancePlan());
        advancedSearchClinicForm.setMedicalStudy(studyNameFromOrderForm(orderForm,locale));
        Collection<Clinic> clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinicName(),
                advancedSearchClinicForm.getClinicHours(),
                advancedSearchClinicForm.getMedicalPlan(),
                advancedSearchClinicForm.getMedicalStudy());

        mav.addObject("clinicsList", clinicsList);

        mav.addObject("studyName", studyNameFromOrderForm(orderForm,locale));

        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=create-order","submit!=back","submit!=toClinicSelection"})
    public ModelAndView createOrder(@Valid @ModelAttribute("orderForm") OrderForm orderForm, final BindingResult bindingResult,
                                    @ModelAttribute("advancedSearchClinicForm")AdvancedSearchClinicForm advancedSearchClinicForm,
                                    @ModelAttribute("orderWithoutClinicForm") OrderWithoutClinicForm orderWithoutClinicForm){
        if(bindingResult.hasErrors()){

            // passing all the data
            advancedSearchClinicForm.setMedicId(orderForm.getMedicId());
            advancedSearchClinicForm.setStudyId(orderForm.getStudyId());
            advancedSearchClinicForm.setDescription(orderForm.getDescription());
            advancedSearchClinicForm.setPatientInfo(orderForm.getPatientInfo());

            orderWithoutClinicForm.setMedicId(orderForm.getMedicId());
            orderWithoutClinicForm.setStudyId(orderForm.getStudyId());
            orderWithoutClinicForm.setDescription(orderForm.getDescription());
            orderWithoutClinicForm.setPatientInfo(orderForm.getPatientInfo());

            ModelAndView mav = new ModelAndView("/advanced-search-clinic");

            mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());
            mav.addObject("clinicsList",clinicService.getAll());
            mav.addObject("clinicUnselected",true);

            return mav;
        }

        // create order successful
        Optional<Medic> medicOptional = medicService.findByUserId(orderForm.getMedicId());
        Optional<Clinic> clinicOptional = clinicService.findByUserId(orderForm.getClinicId());
        Optional<StudyType> studyTypeOptional = studyTypeService.findById(orderForm.getStudyId());

        if(!medicOptional.isPresent()){throw new MedicNotFoundException();}
        if(!clinicOptional.isPresent()){throw new ClinicNotFoundException();}
        if(!studyTypeOptional.isPresent()){throw new StudyTypeNotFoundException();}

        PatientInfoForm patientInfo = orderForm.getPatientInfo();

        fillPatientInfo(patientInfo);

        Order order = orderService.register(
                medicOptional.get(),
                new Date(System.currentTimeMillis()),
                clinicOptional.get(),
                patientInfo.getEmail(),
                patientInfo.getName(),
                studyTypeOptional.get(),
                orderForm.getDescription(),
                medicOptional.get().getIdentificationType(),
                medicOptional.get().getIdentification(),
                patientInfo.getInsurancePlan(),
                patientInfo.getInsuranceNumber());

        return new ModelAndView("redirect:view-study/" + urlEncoderService.encode(order.getOrderId()));
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

    private void fillPatientInfo(PatientInfoForm patientInfo){
        if(patientInfo.getExistingPatient()){
            Optional<Patient> patientOptional = patientService.findByEmail(patientInfo.getEmail());
            if(!patientOptional.isPresent()){throw new PatientNotFoundException();}

            Patient patient = patientOptional.get();

            if(patientInfo.getName()==null || patientInfo.getName().equals("")){patientInfo.setName(patient.getName());}
            if(patientInfo.getInsurancePlan()==null || patientInfo.getInsurancePlan().equals("")){patientInfo.setInsurancePlan(patient.getMedicPlan());}
            if(patientInfo.getInsuranceNumber()==null || patientInfo.getInsuranceNumber().equals("")){patientInfo.setInsuranceNumber(patient.getMedicPlanNumber());}
        }
    }
}
