package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.ClinicHours;
import ar.edu.itba.paw.services.ClinicService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.form.AdvancedSearchClinicForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Time;
import java.util.Collection;

@Controller
@RequestMapping(value="advanced-search/clinic")
public class AdvancedSearchClinicController {

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private ClinicService clinicService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAllClinics(@ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm) {


        final ModelAndView mav = new ModelAndView("advanced-search-clinic");

        mav.addObject("studiesList",studyTypeService.getAll());

        mav.addObject("clinicsList",clinicService.getAll());

        return mav;
    }

    @RequestMapping(method = RequestMethod.GET,params = {"submit=search"})
    public ModelAndView getClinicsWithSearch(@Valid @ModelAttribute("advancedSearchClinicForm") AdvancedSearchClinicForm advancedSearchClinicForm, BindingResult bindingResult) {


        final ModelAndView mav = new ModelAndView("advanced-search-clinic");

        Collection<Clinic> clinicsList = clinicService.getAll();

        if(!bindingResult.hasErrors()){
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
        }else{
            mav.addObject("errorAlert",true);
        }



        mav.addObject("studiesList",studyTypeService.getAll());

        mav.addObject("clinicsList",clinicsList);

        return mav;
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
