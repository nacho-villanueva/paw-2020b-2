package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.form.OrderWithoutClinicForm;
import ar.edu.itba.paw.webapp.form.RequestOrdersForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class RequestOrdersController {

    @Autowired
    private StudyTypeService studyTypeService;

    @RequestMapping(value = "/request-orders", method = RequestMethod.GET)
    public ModelAndView requestOrders(@ModelAttribute("requestOrdersForm") RequestOrdersForm requestOrdersForm){
        final ModelAndView mav = new ModelAndView("request-studies");
        mav.addObject("studiesList", studyTypeService.getAll());
        return mav;
    }

}
