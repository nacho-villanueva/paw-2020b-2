package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.StudyTypeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UploadedFileFailedToLoadException;
import ar.edu.itba.paw.webapp.form.ApplyClinicForm;
import ar.edu.itba.paw.webapp.form.ApplyMedicForm;
import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private MailNotificationService mns;

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
    public String applyMedic(@ModelAttribute("applyMedicForm") ApplyMedicForm applyMedicForm){
        byte[] fileBytes;
        try {
            fileBytes = applyMedicForm.getIdentification().getBytes();
        } catch (IOException e) {
            throw new UploadedFileFailedToLoadException();
        }
        ArrayList<MedicalField> knownFields = new ArrayList<>();
        for(Integer i : applyMedicForm.getKnown_fields()){

            Optional<MedicalField> mf = mfs.findById(i);

            if(mf.isPresent())
                knownFields.add(mf.get());
            else
                throw new StudyTypeNotFoundException();
        }

        Medic newMedic = ms.register(loggedUser(), applyMedicForm.getFullname(),
                applyMedicForm.getEmail(), applyMedicForm.getTelephone(),
                applyMedicForm.getIdentification().getContentType(), fileBytes, applyMedicForm.getLicence_number(),
                true, knownFields);

        // TODO: sendMedicApplicationValidatingMail(newMedic);

        return "redirect:/home";
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

        ArrayList<StudyType> availableStudies = new ArrayList<>();
        for(Integer i : applyClinicForm.getAvailable_studies()){

            Optional<StudyType> st = sts.findById(i);

            if(st.isPresent())
                availableStudies.add(st.get());
            else
                throw new StudyTypeNotFoundException();
        }

        Clinic newClinic = cs.register(loggedUser(), applyClinicForm.getName(),
                applyClinicForm.getEmail(), applyClinicForm.getTelephone(),
                true, availableStudies);

        // TODO: sendClinicApplicationValidatingMail(newClinic);

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
