package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.MedicalFieldNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.StudyTypeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UploadedFileFailedToLoadException;
import ar.edu.itba.paw.webapp.form.ApplyClinicForm;
import ar.edu.itba.paw.webapp.form.ApplyMedicForm;
import ar.edu.itba.paw.webapp.form.RegisterPatientForm;
import ar.edu.itba.paw.webapp.form.RegisterUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.Console;
import java.io.IOException;
import java.util.*;

@Controller
public class RegisterController {

    @Autowired
    private UserDetailsService uds;

    @Autowired
    private UserService us;

    @Autowired
    private MedicService ms;

    @Autowired
    private ClinicService cs;

    @Autowired
    private PatientService ps;

    @Autowired
    private MedicalFieldService mfs;

    @Autowired
    private StudyTypeService sts;

    @Autowired
    private MailNotificationService mns;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("registerUserForm") RegisterUserForm registerUserForm, final BindingResult errors, Locale locale){
        if(errors.hasErrors()){
            ModelAndView registrationErrors = new ModelAndView("index");
            registrationErrors.addObject("registration", true);
            return registrationErrors;
        }

        ModelAndView mav = new ModelAndView("redirect:/home");

        User newUser = us.register(registerUserForm.getEmail(),registerUserForm.getPassword(), locale.toLanguageTag());

        authWithoutPassword(newUser);
        return mav;
    }

    @RequestMapping(value = "/complete-registration", method = RequestMethod.GET)
    public ModelAndView completeRegistration(){
        final ModelAndView mav = new ModelAndView("complete-registration");
        mav.addObject("registerPatientForm", new RegisterPatientForm());
        mav.addObject("applyMedicForm", new ApplyMedicForm());
        mav.addObject("applyClinicForm", new ApplyClinicForm());

        mav.addObject("fieldsList", mfs.getAll());
        mav.addObject("studiesList", sts.getAll());
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
        for (Integer i : applyMedicForm.getKnown_fields()) {

            Optional<MedicalField> mf = mfs.findById(i);

            if (mf.isPresent())
                knownFields.add(mf.get());
            else
                throw new MedicalFieldNotFoundException();
        }

        Medic newMedic = ms.register(loggedUser(), applyMedicForm.getFullname(), applyMedicForm.getTelephone(),
                applyMedicForm.getIdentification().getContentType(), fileBytes, applyMedicForm.getLicence_number(), knownFields);

        authWithoutPassword(loggedUser());
        mns.sendMedicApplicationValidatingMail(newMedic);
        return "redirect:/home";
    }

    @RequestMapping(value = "/apply-as-clinic", method = RequestMethod.POST)
    public ModelAndView applyClinic(@Valid @ModelAttribute("applyClinicForm") ApplyClinicForm applyClinicForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            final ModelAndView mav = new ModelAndView("complete-registration");
            mav.addObject("registerPatientForm", new RegisterPatientForm());
            mav.addObject("applyMedicForm", new ApplyMedicForm());

            mav.addObject("fieldsList", mfs.getAll());
            mav.addObject("studiesList", sts.getAll());
            return mav;
        }


        ArrayList<StudyType> availableStudies = new ArrayList<>();

        for (Integer i : applyClinicForm.getAvailable_studies()) {

            Optional<StudyType> st = sts.findById(i);

            if (st.isPresent())
                availableStudies.add(st.get());
            else
                throw new StudyTypeNotFoundException();
        }

        ClinicHours clinicHours = new ClinicHours();
        clinicHours.setDays(applyClinicForm.getOpen_days());
        clinicHours.setOpen_hours(applyClinicForm.getOpening_time());
        clinicHours.setClose_hours(applyClinicForm.getClosing_time());

        Clinic newClinic = cs.register(loggedUser(), applyClinicForm.getName(), applyClinicForm.getTelephone(), availableStudies, new HashSet<>(Arrays.asList(applyClinicForm.getAccepted_plans_List())),clinicHours);

        authWithoutPassword(loggedUser());
        mns.sendClinicApplicationValidatingMail(newClinic);

        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.POST)
    public ModelAndView registerPatient(@ModelAttribute("registerPatientForm") RegisterPatientForm registerPatientForm){
        ps.register(loggedUser(), registerPatientForm.getFullname(),
                registerPatientForm.getMedical_insurance_plan(), registerPatientForm.getMedical_insurance_number());
        authWithoutPassword(loggedUser());
        return new ModelAndView("redirect:/home");
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }

    public void authWithoutPassword(User user){
        UserDetails userDetails = uds.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
