package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.ClinicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.MedicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.StudyTypeNotFoundException;
import ar.edu.itba.paw.webapp.form.AdvancedSearchClinicForm;
import ar.edu.itba.paw.webapp.form.OrderForm;
import ar.edu.itba.paw.webapp.form.OrderWithoutClinicForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping(value = "/create-order")
public class OrderController {

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService us;

    @RequestMapping(method = RequestMethod.GET)
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

    @RequestMapping(method = RequestMethod.POST, params={"submit=toClinicSelection"})
    public ModelAndView getClinicAdvancedSearch(@Valid @ModelAttribute("orderWithoutClinicForm") OrderWithoutClinicForm orderWithoutClinicForm, final BindingResult bindingResult,
                                                @ModelAttribute("advancedSearchClinicForm")AdvancedSearchClinicForm advancedSearchClinicForm,
                                                @ModelAttribute("orderForm") OrderForm orderForm){
        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("create-order");

            Optional<Medic> m = medicService.findByUserId(loggedUser().getId());
            if(m.isPresent())
                mav.addObject("loggedMedic", m.get());
            else
                throw new MedicNotFoundException();

            mav.addObject("studiesList", studyTypeService.getAll());
            return mav;
        }

        ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        // passing all the data
        advancedSearchClinicForm.setMedicId(orderWithoutClinicForm.getMedicId());
        advancedSearchClinicForm.setStudyId(orderWithoutClinicForm.getStudyId());
        advancedSearchClinicForm.setDescription(orderWithoutClinicForm.getDescription());
        advancedSearchClinicForm.setPatient_insurance_plan(orderWithoutClinicForm.getPatient_insurance_plan());
        advancedSearchClinicForm.setPatient_insurance_number(orderWithoutClinicForm.getPatient_insurance_number());
        advancedSearchClinicForm.setPatientEmail(orderWithoutClinicForm.getPatientEmail());
        advancedSearchClinicForm.setPatientName(orderWithoutClinicForm.getPatientName());

        orderForm.setMedicId(orderWithoutClinicForm.getMedicId());
        orderForm.setStudyId(orderWithoutClinicForm.getStudyId());
        orderForm.setDescription(orderWithoutClinicForm.getDescription());
        orderForm.setPatient_insurance_plan(orderWithoutClinicForm.getPatient_insurance_plan());
        orderForm.setPatient_insurance_number(orderWithoutClinicForm.getPatient_insurance_number());
        orderForm.setPatientEmail(orderWithoutClinicForm.getPatientEmail());
        orderForm.setPatientName(orderWithoutClinicForm.getPatientName());


        mav.addObject("studiesList",studyTypeService.getAll());

        mav.addObject("clinicsList",clinicService.getAll());


        return mav;
    }

    @RequestMapping(method = RequestMethod.GET,params = {"submit=search"})
    public ModelAndView getOrderCreationForm(@Valid @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm, BindingResult bindingResult,
                                             @ModelAttribute("orderForm") OrderForm orderForm) {

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        mav.addObject("studiesList",studyTypeService.getAll());

        orderForm.setMedicId(advancedSearchClinicForm.getMedicId());
        orderForm.setStudyId(advancedSearchClinicForm.getStudyId());
        orderForm.setDescription(advancedSearchClinicForm.getDescription());
        orderForm.setPatient_insurance_plan(advancedSearchClinicForm.getPatient_insurance_plan());
        orderForm.setPatient_insurance_number(advancedSearchClinicForm.getPatient_insurance_number());
        orderForm.setPatientEmail(advancedSearchClinicForm.getPatientEmail());
        orderForm.setPatientName(advancedSearchClinicForm.getPatientName());

        Collection<Clinic> clinicsList;
        if(bindingResult.hasErrors()){
            mav.addObject("clinicsList",clinicService.getAll());

            mav.addObject("errorAlert",true);

            return mav;
        }

        if(advancedSearchClinicForm.getClinic_name().equals(""))
            advancedSearchClinicForm.setClinic_name(null);
        if(advancedSearchClinicForm.getMedical_plan().equals(""))
            advancedSearchClinicForm.setMedical_plan(null);
        if(advancedSearchClinicForm.getMedical_study().equals(""))
            advancedSearchClinicForm.setMedical_study(null);

        clinicsList = clinicService.searchClinicsBy(advancedSearchClinicForm.getClinic_name(),
                getClinicHours(advancedSearchClinicForm),
                advancedSearchClinicForm.getMedical_plan(),
                advancedSearchClinicForm.getMedical_study());

        mav.addObject("clinicsList", clinicsList);
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET,params = {"submit=reset"})
    public ModelAndView getOrderCreationFormReset(@ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm,
                                             @ModelAttribute("orderForm") OrderForm orderForm) {

        final ModelAndView mav = new ModelAndView("/advanced-search-clinic");

        mav.addObject("studiesList",studyTypeService.getAll());

        advancedSearchClinicForm.setClinic_name(null);
        advancedSearchClinicForm.setMedical_plan(null);
        advancedSearchClinicForm.setMedical_study(null);

        orderForm.setMedicId(advancedSearchClinicForm.getMedicId());
        orderForm.setStudyId(advancedSearchClinicForm.getStudyId());
        orderForm.setDescription(advancedSearchClinicForm.getDescription());
        orderForm.setPatient_insurance_plan(advancedSearchClinicForm.getPatient_insurance_plan());
        orderForm.setPatient_insurance_number(advancedSearchClinicForm.getPatient_insurance_number());
        orderForm.setPatientEmail(advancedSearchClinicForm.getPatientEmail());
        orderForm.setPatientName(advancedSearchClinicForm.getPatientName());

        mav.addObject("clinicsList", clinicService.getAll());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params={"submit=create-order"})
    public ModelAndView createOrder(@Valid @ModelAttribute("orderForm") OrderForm orderForm, final BindingResult bindingResult,
                                    @ModelAttribute("advancedSearchClinicForm")AdvancedSearchClinicForm advancedSearchClinicForm){
        if(bindingResult.hasErrors()){

            // passing all the data
            advancedSearchClinicForm.setMedicId(orderForm.getMedicId());
            advancedSearchClinicForm.setStudyId(orderForm.getStudyId());
            advancedSearchClinicForm.setDescription(orderForm.getDescription());
            advancedSearchClinicForm.setPatient_insurance_plan(orderForm.getPatient_insurance_plan());
            advancedSearchClinicForm.setPatient_insurance_number(orderForm.getPatient_insurance_number());
            advancedSearchClinicForm.setPatientEmail(orderForm.getPatientEmail());
            advancedSearchClinicForm.setPatientName(orderForm.getPatientName());

            ModelAndView mav = new ModelAndView("/advanced-search-clinic");

            mav.addObject("studiesList",studyTypeService.getAll());

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

        Order order = orderService.register(
                medicOptional.get(),
                new Date(System.currentTimeMillis()),
                clinicOptional.get(),
                orderForm.getPatientName(),
                orderForm.getPatientEmail(),
                studyTypeOptional.get(),
                orderForm.getDescription(),
                medicOptional.get().getIdentification_type(),
                medicOptional.get().getIdentification(),
                orderForm.getPatient_insurance_plan(),
                orderForm.getPatient_insurance_number());

        return new ModelAndView("redirect:view-study/" + urlEncoderService.encode(order.getOrder_id()));
    }


    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }

    private ClinicHours getClinicHours(AdvancedSearchClinicForm f){
        ClinicHours ret = new ClinicHours();

        if(f.isSundayOpens())
            ret.setDayHour(0, Time.valueOf(f.getSundayOpenTime()),Time.valueOf(f.getSundayCloseTime()));
        if(f.isMondayOpens())
            ret.setDayHour(1, Time.valueOf(f.getMondayOpenTime()),Time.valueOf(f.getMondayCloseTime()));
        if(f.isTuesdayOpens())
            ret.setDayHour(2, Time.valueOf(f.getTuesdayOpenTime()),Time.valueOf(f.getTuesdayCloseTime()));
        if(f.isWednesdayOpens())
            ret.setDayHour(3, Time.valueOf(f.getWednesdayOpenTime()),Time.valueOf(f.getWednesdayCloseTime()));
        if(f.isThursdayOpens())
            ret.setDayHour(4, Time.valueOf(f.getThursdayOpenTime()),Time.valueOf(f.getThursdayCloseTime()));
        if(f.isFridayOpens())
            ret.setDayHour(5, Time.valueOf(f.getFridayOpenTime()),Time.valueOf(f.getFridayCloseTime()));
        if(f.isSaturdayOpens())
            ret.setDayHour(6, Time.valueOf(f.getSaturdayOpenTime()),Time.valueOf(f.getSaturdayCloseTime()));

        return ret;
    }
}
