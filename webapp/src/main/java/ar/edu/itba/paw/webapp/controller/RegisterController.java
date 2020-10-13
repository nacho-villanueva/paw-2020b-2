package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

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

        User newUser = us.register(registerUserForm.getEmail(),registerUserForm.getPasswordField().getPassword(), locale.toLanguageTag());

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
    public ModelAndView applyMedic(@Valid @ModelAttribute("applyMedicForm") ApplyMedicForm applyMedicForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView mavError = new ModelAndView("complete-registration");
            mavError.addObject("tabRegistration", "medic");
            mavError.addObject("registerPatientForm", new RegisterPatientForm());
            mavError.addObject("applyClinicForm", new ApplyClinicForm());

            mavError.addObject("fieldsList", mfs.getAll());
            mavError.addObject("studiesList", sts.getAll());
            return mavError;
        }

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
                throw new StudyTypeNotFoundException();
        }

        Medic newMedic = ms.register(loggedUser(), applyMedicForm.getFullname(), applyMedicForm.getTelephone(),
                applyMedicForm.getIdentification().getContentType(), fileBytes, applyMedicForm.getLicence_number(), knownFields);

        authWithoutPassword(loggedUser());
        mns.sendMedicApplicationValidatingMail(newMedic);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/apply-as-clinic", method = RequestMethod.POST)
    public ModelAndView applyClinic(@Valid @ModelAttribute("applyClinicForm") ApplyClinicForm applyClinicForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView mavError = new ModelAndView("complete-registration");
            mavError.addObject("registrationTab", "clinic");
            mavError.addObject("registerPatientForm", new RegisterPatientForm());
            mavError.addObject("applyMedicForm", new ApplyMedicForm());

            mavError.addObject("fieldsList", mfs.getAll());
            mavError.addObject("studiesList", sts.getAll());

            return mavError;
        }

        ArrayList<StudyType> availableStudies = new ArrayList<>();

        for (Integer i : applyClinicForm.getAvailable_studies()) {

            Optional<StudyType> st = sts.findById(i);

            if (st.isPresent())
                availableStudies.add(st.get());
            else
                throw new StudyTypeNotFoundException();
        }

        Clinic newClinic = cs.register(loggedUser(), applyClinicForm.getName(), applyClinicForm.getTelephone(), availableStudies);

        authWithoutPassword(loggedUser());
        mns.sendClinicApplicationValidatingMail(newClinic);

        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.POST)
    public ModelAndView registerPatient(@Valid @ModelAttribute("registerPatientForm") RegisterPatientForm registerPatientForm, final BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView mavError = new ModelAndView("complete-registration");
            mavError.addObject("registrationTab", "patient");
            mavError.addObject("applyMedicForm", new ApplyMedicForm());
            mavError.addObject("applyClinicForm", new ApplyClinicForm());

            mavError.addObject("fieldsList", mfs.getAll());
            mavError.addObject("studiesList", sts.getAll());

            return mavError;
        }
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
