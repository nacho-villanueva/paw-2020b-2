package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.ApplyClinicForm;
import ar.edu.itba.paw.webapp.form.ApplyMedicForm;
import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

@Controller
public class RegisterController {

    @Autowired
    private UserService us;

    @Autowired
    private MedicService ms;

    @Autowired
    private ClinicService cs;

    @Autowired
    private MedicalFieldService mfs;

    @Autowired
    private StudyTypeService sts;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("registerUserForm") RegisterUserForm registerUserForm){
        ModelAndView mav = new ModelAndView("redirect:/?registrationSuccess=true");

        User newUser = us.register(registerUserForm.getEmail(),registerUserForm.getPassword());
        //TODO Service: Generate LoginUserForm
        //TODO mav.addObject(loginUserForm);
        return mav;
    }

    @RequestMapping("/register-as-medic")
    public ModelAndView registerAsMedic(@ModelAttribute("applyMedicForm") ApplyMedicForm applyMedicForm){
        final ModelAndView mav = new ModelAndView("medic-registration");
        mav.addObject("applyMedicForm", applyMedicForm);
        mav.addObject("fieldsList", mfs.getAll());
        return mav;
    }

    @RequestMapping(value = "/apply-as-medic", method = RequestMethod.POST)
    public String applyMedic(@ModelAttribute("applyMedicForm") ApplyMedicForm applyMedicForm,
                                   @RequestParam("orderAttach") MultipartFile file){
        try {
            byte[] fileBytes = file.getBytes();
            Medic newMedic = ms.register(loggedUser(), applyMedicForm.getFullname(),
                    loggedUser().getEmail(), applyMedicForm.getTelephone(),
                    file.getContentType(), fileBytes, applyMedicForm.getLicence_number(),
                    true, applyMedicForm.getKnown_fields());

            return "redirect:/home";
        } catch (IOException e) {
            return "redirect:/index"; //TODO: RETURN 500 EXCEPTION PAGE
        }
    }

    @RequestMapping("/register-as-clinic")
    public ModelAndView registerAsClinic(@ModelAttribute("applyClinicForm") ApplyClinicForm applyClinicForm){
        final ModelAndView mav = new ModelAndView("clinic-registration");
        mav.addObject("applyClinicForm", applyClinicForm);
        mav.addObject("studiesList", sts.getAll());
        return mav;
    }

    @RequestMapping(value = "/apply-as-clinic", method = RequestMethod.POST)
    public String applyClinic(@ModelAttribute("applyClinicForm") ApplyClinicForm applyClinicForm){
        Clinic newClinic = cs.register(loggedUser(), applyClinicForm.getName(),
                loggedUser().getEmail(), applyClinicForm.getTelephone(),
                true, applyClinicForm.getAvailable_studies());

        return "redirect:/home";
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
