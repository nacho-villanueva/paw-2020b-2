package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.FilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/my-studies")
    public ModelAndView myStudies(@RequestParam(value = "date", required = false) String dateString,
                                  @RequestParam(value = "clinic", required = false) String clinicString,
                                  @RequestParam(value = "medic", required = false) String medicString,
                                  @RequestParam(value = "study", required = false) String studyString,
                                  @RequestParam(value = "patient", required = false) String patientString,
                                  @ModelAttribute("filterForm") FilterForm filterForm){
        ModelAndView mav = new ModelAndView("my-studies");

        mav.addObject("filterForm", filterForm);
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

        listingSetup(mav, parameters);

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
    private void listingSetup(ModelAndView mav, HashMap<String, String> parameters){
        User user = loggedUser();
        Collection<Order> orders;
        Collection<Clinic> clinicsList = new ArrayList<>();
        Collection<Medic> medicsList = new ArrayList<>();

        //aca se estan haciendo cosas del estilo "cual es el dominio de este user"
        //capaz volar esta seccion a userservice
        if(user.isMedic() && !user.isVerifyingMedic() && medicService.findByUserId(user.getId()).isPresent()){
            orders = orderService.getAllAsMedic(user);
            orders.forEach(order -> clinicsList.add(order.getClinic()));
            medicsList.add(medicService.findByUserId(user.getId()).get());
        }else if(user.isClinic() && !user.isVerifyingClinic() && clinicService.findByUserId(user.getId()).isPresent()){
            orders = orderService.getAllAsClinic(user);
            orders.forEach(order -> medicsList.add(order.getMedic()));
            clinicsList.add(clinicService.findByUserId(user.getId()).get());
        }else{
            orders = orderService.getAllAsPatient(user);
            orders.forEach(order -> clinicsList.add(order.getClinic()));
            orders.forEach(order -> medicsList.add(order.getMedic()));
        }

        mav.addObject("medicsList", medicsList);
        mav.addObject("clinicsList", clinicsList);
        mav.addObject("studiesList", studyService.getAll());

        //clinic sea el user id
        //medic sea el user id
        //patient sea el patient_name
        //date sea un string yyyy-mm-dd
        //studytype sea el type id
        if(parameters.containsKey("c")){
            int aux = Integer.parseInt(parameters.get("c"));
            if(clinicService.findByUserId(aux).isPresent())
                orders.removeIf(order -> order.getClinic().getUser_id() != aux);
        }
        if(parameters.containsKey("m")){
            int aux = Integer.parseInt(parameters.get("m"));
            if(medicService.findByUserId(aux).isPresent())
                orders.removeIf(order -> order.getMedic().getUser_id() != aux);
        }
        if(parameters.containsKey("p")){
            orders.removeIf(order -> !order.getPatient_name().equals(parameters.get("p")));
        }
        if(parameters.containsKey("d")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try{
                dateFormat.parse(parameters.get("d").trim());
            }catch (ParseException pe){
                //what are you doing
            }
            orders.removeIf(order -> !order.getDate().equals(Date.valueOf(parameters.get("d"))));
        }
        if(parameters.containsKey("s")){
            int aux = Integer.parseInt(parameters.get("s"));
            if(studyService.findById(aux).isPresent())
                orders.removeIf(order -> order.getStudy().getId() != Integer.parseInt(parameters.get("s")));
        }

        HashMap<Long, String> encodeds = new HashMap<>();

        encoder(orders, encodeds);

        mav.addObject("ordersList", orders);
        mav.addObject("encodedList", encodeds);

    }

    private void encoder(Collection<Order> orders, HashMap<Long, String> encodeds){
        for(Order order : orders){
            encodeds.put(order.getOrder_id(), urlEncoderService.encode(order.getOrder_id()));
        }
    }

    @RequestMapping(value = "/filter-search", method = RequestMethod.POST)
    public String filterSearch(@ModelAttribute("filterForm") FilterForm filterForm){
        String out = "redirect:/my-studies?";
        if(filterForm.getDate() != null && !filterForm.getDate().isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try{
                dateFormat.parse(filterForm.getDate().trim());
            }catch (ParseException pe){
                //what are you doing
            }
            out += "date=" + filterForm.getDate() + "&";
        }
        if(filterForm.getClinic_id() != null && filterForm.getClinic_id() != -1){
            if(clinicService.findByUserId(filterForm.getClinic_id()).isPresent())
                out += "clinic=" + filterForm.getClinic_id().toString() + "&";
        }
        if(filterForm.getMedic_id() != null && filterForm.getMedic_id() != -1){
            if(medicService.findByUserId(filterForm.getMedic_id()).isPresent())
                out += "medic=" + filterForm.getMedic_id().toString() + "&";
        }
        if(filterForm.getPatient_name() != null && !filterForm.getPatient_name().isEmpty()){
            out += "patient=" + filterForm.getPatient_name() + "&";
        }
        if(filterForm.getStudy_id() != null && filterForm.getStudy_id() != -1){
            if(studyService.findById(filterForm.getStudy_id()).isPresent())
                out += "study=" + filterForm.getStudy_id().toString();
        }

        return out;
    }

}
