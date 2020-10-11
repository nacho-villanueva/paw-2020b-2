package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.MedicalField;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.services.MedicalFieldService;
import ar.edu.itba.paw.services.StudyTypeService;
import ar.edu.itba.paw.webapp.form.CreateTypeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class CreateTypesController {

    @Autowired
    private StudyTypeService sts;

    @Autowired
    private MedicalFieldService mfs;

    @RequestMapping(value = "/create-type", method = RequestMethod.GET)
    public ModelAndView getCreateTypes(@ModelAttribute("createTypeForm") CreateTypeForm createTypeForm,
                                       @RequestParam(value = "s", required = false) Integer creationSuccesful){
        ModelAndView mav = new ModelAndView("create-type");
        mav.addObject("createTypeForm", createTypeForm);
        mav.addObject("success", creationSuccesful);
        return mav;
    }

    @RequestMapping(value = "/create-type", method = RequestMethod.POST)
    public ModelAndView postCreateTypes(@Valid @ModelAttribute("createTypeForm")CreateTypeForm createTypeForm, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ModelAndView("create-type");

        if(createTypeForm.getType() == 0){
            sts.register(createTypeForm.getName());
        }
        else {
            mfs.register(createTypeForm.getName());
        }

        return new ModelAndView("redirect:/create-type?s=" + createTypeForm.getType());
    }

}
