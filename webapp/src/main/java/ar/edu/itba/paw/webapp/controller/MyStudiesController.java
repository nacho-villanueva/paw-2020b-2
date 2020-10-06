package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class MyStudiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private StudyTypeService studyService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private PatientService patientService;  //TO DO: Los medicos pueden filtrar por pacientes?
                                            // entonces necesitamos un pt.getAll()
    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/my-studies")
    public ModelAndView myStudies(@RequestParam(value = "date", required = false) String dateString,
                                  @RequestParam(value = "clinic", required = false) String clinicString,
                                  @RequestParam(value = "medic", required = false) String medicString,
                                  @RequestParam(value = "study", required = false) String studyString,
                                  @RequestParam(value = "patient", required = false) String patientString){
        ModelAndView mav = new ModelAndView("my-studies");

        HashMap<String, String> parameters = new HashMap<>();

        if(dateString != null && !dateString.isEmpty())
            parameters.put("d", dateString);
        if(clinicString != null && !clinicString.isEmpty())
            parameters.put("c", clinicString);
        if(medicString != null && !medicString.isEmpty())
            parameters.put("m", medicString);
        if(studyString != null && !studyString.isEmpty())
            parameters.put("s", studyString);
        if(patientString != null && !patientString.isEmpty())
            parameters.put("p", patientString);

        mav = listingSetup(mav, parameters);

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

    //we can use loggedUser to check if they are medic or clinic or patient
    //now that a user can only be one of these
    private ModelAndView listingSetup(ModelAndView mav, HashMap<String, String> parameters){
        User user = loggedUser();
        if(user.isClinic() && !user.isVerifyingClinic() && clinicService.findByUserId(user.getId()).isPresent()){
            Collection<Clinic> onlyUser = new ArrayList<>();
            onlyUser.add(clinicService.findByUserId(user.getId()).get());
            mav.addObject("clinicsList", onlyUser);
        }else{
            mav.addObject("clinicsList", clinicService.getAll());
        }

        if(user.isMedic() && !user.isVerifyingMedic() && medicService.findByUserId(user.getId()).isPresent()){
            Collection<Medic> onlyUser = new ArrayList<>();
            onlyUser.add(medicService.findByUserId(user.getId()).get());
            mav.addObject("medicsList", onlyUser);
        }else{
            mav.addObject("medicsList", medicService.getAll());
        }
        /*
        **  FUTURE CODE
        **
        if(!user.isClinic() && !user.isMedic()){
            Collection<Patient> onlyUser = new ArrayList<>();
            onlyUser.add(patientService.findByUserId(user.getId()).get());
            mav.addObject("patientsList", onlyUser);
        }else{
            mav.addObject("patientsList", patientService.getAll());
        }
        */
        mav.addObject("studiesList", studyService.getAll());

        Collection<Order> orders;
        HashMap<Long, String> encodeds = new HashMap<>();
        List<String> params = new ArrayList<>();
        params.add(parameters.getOrDefault("d", null));
        params.add(parameters.getOrDefault("c", null));
        params.add(parameters.getOrDefault("m", null));
        params.add(parameters.getOrDefault("s", null));
        params.add(parameters.getOrDefault("p", null));
        /*
        ** FUTURE CODE
        **
        orders = orderService.getAllDCMSP(params);
         */
        encodeds = encoder(orders, encodeds);

        mav.addObject("ordersList", orders);
        mav.addObject("encodedList", encodeds);

        return mav;
    }

    private HashMap<Long, String> encoder(Collection<Order> orders, HashMap<Long, String> encodeds){
        for(Order order : orders){
            encodeds.put(order.getOrder_id(), urlEncoderService.encode(order.getOrder_id()));
        }
        return encodeds;
    }
}
