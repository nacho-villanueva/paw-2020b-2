package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.*;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private MedicalFieldService medicalFieldService;

    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDetailsService uds;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getProfile(@RequestParam(required = false,name = "editSuccess") final Integer editSuccess, Locale locale){
        final ModelAndView mav = new ModelAndView("profile-view");
        mav.addObject("loggedUser",loggedUser());

        int userId = loggedUser().getId();

        if(editSuccess != null){

            String message = null;

            switch (editSuccess){
                case User.UNDEFINED_ROLE_ID:
                    message = messageSource.getMessage("role.user",null,locale);
                    break;
                case User.PATIENT_ROLE_ID:
                    message = messageSource.getMessage("role.patient",null,locale);
                    break;
                case User.MEDIC_ROLE_ID:
                    message = messageSource.getMessage("role.medic",null,locale);
                    break;
                case User.CLINIC_ROLE_ID:
                    message = messageSource.getMessage("role.clinic",null,locale);
                    break;
                default:
                    //
            }

            if (message != null)
                mav.addObject("editSuccess",message);
        }

        addUser(mav, locale);

        return mav;
    }

    @RequestMapping(value = "/edit/user/email",method = RequestMethod.GET)
    public ModelAndView editProfileUserGet(@ModelAttribute("editUserEmailForm") EditUserEmailForm editUserEmailForm, Locale locale){
        final ModelAndView mav = new ModelAndView("profile-edit");

        mav.addObject("role","user");
        mav.addObject("loggedUser",loggedUser());
        addUserRole(mav);

        editUserEmailForm.setEmail(loggedUser().getEmail());

        return mav;
    }

    @RequestMapping(value = "/edit/user/email", method = RequestMethod.POST)
    public ModelAndView editProfileUserPost(@Valid @ModelAttribute("editUserEmailForm") final EditUserEmailForm editUserEmailForm, final BindingResult errors,Locale locale){
        if(errors.hasErrors()){

            ModelAndView mav = new ModelAndView("profile-edit");
            mav.addObject("role","user");
            mav.addObject("loggedUser",loggedUser());
            addUserRole(mav);

            mav.addObject("errorAlert",messageSource.getMessage("profile-edit.body.errorAlert.formErrors",null,locale));

            return mav;
        }

        User user = userService.updateEmail(loggedUser(),editUserEmailForm.getEmail());
        updateUserContext(user);

        ModelAndView mav = new ModelAndView("redirect:/profile");

        mav.addObject("editSuccess",User.UNDEFINED_ROLE_ID);

        return mav;
    }

    @RequestMapping(value = "/edit/user/pass",method = RequestMethod.GET)
    public ModelAndView editProfileUserGet(@ModelAttribute("editUserPasswordForm")EditUserPasswordForm editUserPasswordForm,Locale locale){
        final ModelAndView mav = new ModelAndView("profile-edit");

        mav.addObject("role","user");
        mav.addObject("loggedUser",loggedUser());
        addUserRole(mav);

        return mav;
    }

    @RequestMapping(value = "/edit/user/pass", method = RequestMethod.POST)
    public ModelAndView editProfileUserPost(@Valid @ModelAttribute("editUserPasswordForm") final EditUserPasswordForm editUserPasswordForm, final BindingResult errors, Locale locale){
        if(errors.hasErrors()){

            ModelAndView mav = new ModelAndView("profile-edit");
            mav.addObject("role","user");
            mav.addObject("loggedUser",loggedUser());
            addUserRole(mav);

            mav.addObject("errorAlert",messageSource.getMessage("profile-edit.body.errorAlert.formErrors",null,locale));

            return mav;
        }

        userService.updatePassword(loggedUser(),editUserPasswordForm.getNewPassword().getPassword());

        ModelAndView mav = new ModelAndView("redirect:/profile");

        mav.addObject("editSuccess",User.UNDEFINED_ROLE_ID);

        return mav;
    }

    @RequestMapping(value = "/edit/patient",method = RequestMethod.GET)
    public ModelAndView editProfilePatientGet(@ModelAttribute("editPatientForm") EditPatientForm editPatientForm){

        User user = loggedUser();

        final ModelAndView mav = new ModelAndView("profile-edit");

        mav.addObject("role","patient");

        Optional<Patient> patientOptional = patientService.findByUser_id(user.getId());
        if(patientOptional.isPresent()){
            Patient patient = patientOptional.get();
            mav.addObject("patient", patient);
            editPatientForm.setFull_name(patient.getName());
            editPatientForm.setMedical_plan(patient.getMedic_plan());
            editPatientForm.setMedical_plan_number(patient.getMedic_plan_number());
        }else{
            throw new PatientNotFoundException();
        }

        return mav;
    }

    @RequestMapping(value = "/edit/patient",method = RequestMethod.POST)
    public ModelAndView editProfilePatientPost(@Valid @ModelAttribute("editPatientForm") final EditPatientForm editPatientForm, final BindingResult errors, Locale locale){
        if(errors.hasErrors()){

            ModelAndView mav = new ModelAndView("profile-edit");
            mav.addObject("role","patient");
            addUserRole(mav);

            mav.addObject("errorAlert",messageSource.getMessage("profile-edit.body.errorAlert.formErrors",null,locale));

            return mav;
        }

        ModelAndView mav = new ModelAndView("redirect:/profile");

        patientService.updatePatientInfo(loggedUser(),editPatientForm.getFull_name(),editPatientForm.getMedical_plan(),editPatientForm.getMedical_plan_number());

        mav.addObject("editSuccess",User.PATIENT_ROLE_ID);

        return mav;
    }

    @RequestMapping(value = "/edit/medic",method = RequestMethod.GET)
    public ModelAndView editProfileMedicGet(@ModelAttribute("editMedicForm") EditMedicForm editMedicForm){

        User user = loggedUser();

        final ModelAndView mav = new ModelAndView("profile-edit");

        mav.addObject("role","medic");

        Optional<Medic> medicOptional = medicService.findByUserId(user.getId());
        if(medicOptional.isPresent()){
            Medic medic = medicOptional.get();
            mav.addObject("medic", medic);
            editMedicForm.setFull_name(medic.getName());
            editMedicForm.setTelephone(medic.getTelephone());
            editMedicForm.setLicence_number(medic.getLicence_number());

            ArrayList<Integer> knownFieldsList = new ArrayList<>();
            for (MedicalField mf : medic.getMedical_fields()) {
                knownFieldsList.add(mf.getId());
            }
            Integer[] knownFields = knownFieldsList.toArray(new Integer[knownFieldsList.size()]);
            editMedicForm.setKnown_fields(knownFields);

            mav.addObject("selectedFields", Arrays.stream(editMedicForm.getKnown_fields()).mapToInt(Integer::intValue).toArray());
            mav.addObject("fieldsList",medicalFieldService.getAll());
        }else{
            throw new MedicNotFoundException();
        }

        return mav;
    }

    @RequestMapping(value = "/edit/medic",method = RequestMethod.POST)
    public ModelAndView editProfileMedicPost(@Valid @ModelAttribute("editMedicForm") final EditMedicForm editMedicForm, final BindingResult errors, Locale locale){
        if(errors.hasErrors()){

            ModelAndView mav = new ModelAndView("profile-edit");
            mav.addObject("role","medic");
            addUserRole(mav);

            mav.addObject("selectedFields", Arrays.stream(editMedicForm.getKnown_fields()).mapToInt(Integer::intValue).toArray());
            mav.addObject("fieldsList",medicalFieldService.getAll());

            mav.addObject("errorAlert",messageSource.getMessage("profile-edit.body.errorAlert.formErrors",null,locale));

            return mav;
        }

        ModelAndView mav = new ModelAndView("redirect:/profile");

        Medic medic;
        Optional<Medic> medicOptional=medicService.findByUserId(loggedUser().getId());
        if( medicOptional.isPresent()){
            medic = medicOptional.get();
        }else{
            throw new MedicNotFoundException();
        }

        ArrayList<MedicalField> knownFields = new ArrayList<>();
        for (Integer i : editMedicForm.getKnown_fields()) {

            Optional<MedicalField> mf = medicalFieldService.findById(i);

            if (mf.isPresent())
                knownFields.add(mf.get());
            else
                throw new MedicalFieldNotFoundException();
        }

        String newContentType = medic.getIdentification_type();
        byte[] newContent = medic.getIdentification();
        if(!editMedicForm.getIdentification().isEmpty()){
            newContentType = editMedicForm.getIdentification().getContentType();
            try {
                newContent = editMedicForm.getIdentification().getBytes();
            } catch (IOException e) {
                throw new UploadedFileFailedToLoadException();
            }
        }

        medicService.updateMedicInfo(loggedUser(),editMedicForm.getFull_name(),editMedicForm.getTelephone(),
                newContentType, newContent, editMedicForm.getLicence_number(),knownFields,medic.isVerified());


        mav.addObject("editSuccess",User.MEDIC_ROLE_ID);

        return mav;
    }

    @RequestMapping(value = "/edit/clinic",method = RequestMethod.GET)
    public ModelAndView editProfileClinicGet(@ModelAttribute("editClinicForm")EditClinicForm editClinicForm){

        User user = loggedUser();

        final ModelAndView mav = new ModelAndView("profile-edit");

        mav.addObject("role","clinic");

        Optional<Clinic> clinicOptional = clinicService.findByUserId(user.getId());
        if(clinicOptional.isPresent()){
            Clinic clinic = clinicOptional.get();
            mav.addObject("clinic", clinic);
            editClinicForm.setFull_name(clinic.getName());
            editClinicForm.setTelephone(clinic.getTelephone());

            editClinicForm.getClinicHoursForm().setClosing_time(clinic.getHours().getClose_hours_asString());
            editClinicForm.getClinicHoursForm().setOpening_time(clinic.getHours().getOpen_hours_asString());
            editClinicForm.getClinicHoursForm().setOpen_days(clinic.getHours().getDays_asIntArray());

            editClinicForm.setAccepted_plans(clinic.getAccepted_plans().toArray(new String[0]));

            ArrayList<Integer> availableStudiesList = new ArrayList<>();
            for (StudyType studyType : clinic.getMedical_studies()) {
                availableStudiesList.add(studyType.getId());
            }
            Integer[] availableStudies = availableStudiesList.toArray(new Integer[0]);
            editClinicForm.setAvailable_studies(availableStudies);


            mav.addObject("selectedStudies", Arrays.stream(editClinicForm.getAvailable_studies()).mapToInt(Integer::intValue).toArray());
            mav.addObject("studiesList",studyTypeService.getAll());
        }else{
            throw new ClinicNotFoundException();
        }

        return mav;
    }

    @RequestMapping(value = "/edit/clinic",method = RequestMethod.POST)
    public ModelAndView editProfileClinicPost(@Valid @ModelAttribute("editClinicForm") final EditClinicForm editClinicForm, final BindingResult errors, Locale locale){
        if(errors.hasErrors()){

            ModelAndView mav = new ModelAndView("profile-edit");
            mav.addObject("role","clinic");
            addUserRole(mav);

            mav.addObject("selectedStudies", Arrays.stream(editClinicForm.getAvailable_studies()).mapToInt(Integer::intValue).toArray());
            mav.addObject("studiesList",studyTypeService.getAll());

            mav.addObject("errorAlert",messageSource.getMessage("profile-edit.body.errorAlert.formErrors",null,locale));

            return mav;
        }

        ModelAndView mav = new ModelAndView("redirect:/profile");

        Clinic clinic;
        Optional<Clinic> clinicOptional=clinicService.findByUserId(loggedUser().getId());
        if( clinicOptional.isPresent()){
            clinic = clinicOptional.get();
        }else{
            throw new ClinicNotFoundException();
        }

        ArrayList<StudyType> availableStudies = new ArrayList<>();
        for (Integer i : editClinicForm.getAvailable_studies()) {

            Optional<StudyType> studyType = studyTypeService.findById(i);

            if (studyType.isPresent())
                availableStudies.add(studyType.get());
            else
                throw new StudyTypeNotFoundException();
        }

        ClinicHours clinicHours = new ClinicHours();
        Set<Integer> daysSet = new HashSet<>(Arrays.asList(editClinicForm.getClinicHoursForm().getOpen_days()));
        clinicHours.setDaysHours(daysSet,editClinicForm.getClinicHoursForm().getOpening_time(),editClinicForm.getClinicHoursForm().getClosing_time());

        clinicService.updateClinicInfo(loggedUser(),editClinicForm.getFull_name(),editClinicForm.getTelephone(),availableStudies,new HashSet<>(Arrays.asList(editClinicForm.getAccepted_plans_List())),clinicHours,clinic.isVerified());

        mav.addObject("editSuccess",User.CLINIC_ROLE_ID);

        return mav;
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }

    public void updateUserContext(User user){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void addUserRole(ModelAndView mav){

        switch (loggedUser().getRole()){
            case User.PATIENT_ROLE_ID:
                mav.addObject("patient",true);
                break;
            case User.MEDIC_ROLE_ID:
                mav.addObject("medic",true);
                break;
            case User.CLINIC_ROLE_ID:
                mav.addObject("clinic",true);
                break;
            default:
                //
        }
    }

    private void addUser(ModelAndView mav, Locale locale){

        User user = loggedUser();

        if(user!=null){
            mav.addObject("loggedUser",user);

            switch (user.getRole()){
                case User.PATIENT_ROLE_ID:
                    Optional<Patient> patientOptional = patientService.findByUser_id(user.getId());
                    if(patientOptional.isPresent()){
                        mav.addObject("patient", patientOptional.get());
                    }else{
                        throw new MedicNotFoundException();
                    }
                    break;
                case User.MEDIC_ROLE_ID:
                    Optional<Medic> medicOptional = medicService.findByUserId(user.getId());
                    if(medicOptional.isPresent()){
                        mav.addObject("medic", medicOptional.get());
                    }else{
                        throw new PatientNotFoundException();
                    }
                    break;
                case User.CLINIC_ROLE_ID:
                    Optional<Clinic> clinicOptional = clinicService.findByUserId(user.getId());
                    if(clinicOptional.isPresent()){
                        Clinic clinic = clinicOptional.get();
                        mav.addObject("clinic", clinic);

                        mav.addObject("daysOfWeek",ClinicHours.getDaysOfWeek());

                        HashMap<String, String[]> openDayHour = new HashMap<>();

                        int maxDays = ClinicHours.getDaysOfWeek();
                        for(int i = 0; i < maxDays; i++){
                            if(clinic.getHours().getDays()[i]){
                                String[] val = {clinic.getHours().getOpen_hours_asString()[i],clinic.getHours().getClose_hours_asString()[i]};
                                openDayHour.put(String.valueOf(i), val);
                            }
                        }

                        mav.addObject("openDayHour", openDayHour);
                    }else{
                        throw new ClinicNotFoundException();
                    }
                    break;
                default:
                    //
            }
        }
    }

}