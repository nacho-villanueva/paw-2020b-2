package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("registerUserForm") RegisterUserForm registerUserForm){
        ModelAndView mav = new ModelAndView("redirect:/login");
        //TODO Service: Register User
        //TODO Service: Generate LoginUserForm
        //TODO mav.addObject(loginUserForm);
        return mav;
    }
}
