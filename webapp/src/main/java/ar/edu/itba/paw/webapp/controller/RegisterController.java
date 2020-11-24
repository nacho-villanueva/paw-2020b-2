package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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

        ModelAndView mav = new ModelAndView("redirect:/?registrationSuccess=true");

        User newUser = us.register(registerUserForm.getEmail(),registerUserForm.getPasswordField().getPassword(), locale.toLanguageTag());

        //authWithoutPassword(newUser);
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

        Collection<MedicalField> knownFields = new HashSet<>();
        for (String medicalFieldName : applyMedicForm.getKnown_fields()) {
            knownFields.add(new MedicalField(medicalFieldName));
        }

        if(bindingResult.hasErrors()){
            ModelAndView mavError = new ModelAndView("complete-registration");
            mavError.addObject("registrationTab", "medic");
            mavError.addObject("registerPatientForm", new RegisterPatientForm());
            mavError.addObject("applyClinicForm", new ApplyClinicForm());

            knownFields.addAll(mfs.getAll());

            mavError.addObject("fieldsList", knownFields);
            mavError.addObject("studiesList", sts.getAll());
            return mavError;
        }

        byte[] fileBytes;
        try {
            fileBytes = applyMedicForm.getIdentification().getBytes();
        } catch (IOException e) {
            throw new UploadedFileFailedToLoadException();
        }

        Medic newMedic = ms.register(loggedUser(), applyMedicForm.getFullname(), applyMedicForm.getTelephone(),
                applyMedicForm.getIdentification().getContentType(), fileBytes, applyMedicForm.getLicenceNumber(), knownFields);

        mns.sendMedicApplicationValidatingMail(newMedic);
        authWithoutPassword(loggedUser());
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/apply-as-clinic", method = RequestMethod.POST)
    public ModelAndView applyClinic(@Valid @ModelAttribute("applyClinicForm") ApplyClinicForm applyClinicForm, BindingResult bindingResult){

        Collection<StudyType> availableStudies = new HashSet<>();
        for (String studyTypeName : applyClinicForm.getAvailableStudies()) {
            availableStudies.add(new StudyType(studyTypeName));
        }

        if(bindingResult.hasErrors()){
            ModelAndView mavError = new ModelAndView("complete-registration");
            mavError.addObject("registrationTab", "clinic");
            mavError.addObject("registerPatientForm", new RegisterPatientForm());
            mavError.addObject("applyMedicForm", new ApplyMedicForm());

            availableStudies.addAll(sts.getAll());

            mavError.addObject("fieldsList", mfs.getAll());
            mavError.addObject("studiesList", availableStudies);

            return mavError;
        }


        ClinicHours clinicHours = new ClinicHours();
        Set<Integer> daysSet = new HashSet<>(Arrays.asList(applyClinicForm.getClinicHoursForm().getOpen_days()));
        clinicHours.setDaysHours(daysSet,applyClinicForm.getClinicHoursForm().getOpening_time(),applyClinicForm.getClinicHoursForm().getClosing_time());

        Clinic newClinic = cs.register(loggedUser(), applyClinicForm.getName(), applyClinicForm.getTelephone(), availableStudies, new HashSet<>(Arrays.asList(applyClinicForm.getAcceptedPlans_List())),clinicHours);

        mns.sendClinicApplicationValidatingMail(newClinic);
        authWithoutPassword(loggedUser());
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

    @RequestMapping(value = "/user-verification", method = RequestMethod.GET)
    public ModelAndView confirmRegistration(@RequestParam(name = "token") String token) {
        Optional<VerificationToken> verificationToken = us.getVerificationToken(token);
        if(!verificationToken.isPresent()) {
            //Redirect to invalid token site (If they entered from this URL, there is no need to resend the link, they are probably probing or our DB connection is down)
            return new ModelAndView("redirect:/400");
        }

        User user = verificationToken.get().getUser();
        if(!user.isEnabled()) {
            user = us.verify(user);
            authWithoutPassword(user);
            return new ModelAndView("redirect:/complete-registration");
        }
        return new ModelAndView("redirect:/");
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
