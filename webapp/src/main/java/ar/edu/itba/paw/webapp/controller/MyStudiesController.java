package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.FilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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

        HashMap<OrderService.Parameters, String> parameters = new HashMap<>();
        if(dateString != null && !dateString.isEmpty())
            parameters.put(OrderService.Parameters.DATE, dateString);
        if(clinicString != null && !clinicString.isEmpty())
            parameters.put(OrderService.Parameters.CLINIC, clinicString);
        if(medicString != null && !medicString.isEmpty())
            parameters.put(OrderService.Parameters.MEDIC, medicString);
        if(studyString != null && !studyString.isEmpty())
            parameters.put(OrderService.Parameters.STUDYTYPE, studyString);
        if(patientString != null && !patientString.isEmpty())
            parameters.put(OrderService.Parameters.PATIENT, patientString);

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


    private void listingSetup(ModelAndView mav, HashMap<OrderService.Parameters, String> parameters){
        User user = loggedUser();

        //clinic sea el user id
        //medic sea el user id
        //patient sea el user id
        //date sea un string yyyy-mm-dd
        //studytype sea el type id

        Collection<Order> orders = orderService.filterOrders(user, parameters);
        Collection<Clinic> clinicsList = new ArrayList<>();
        Collection<Medic> medicsList = new ArrayList<>();

        if(user.isMedic() && !user.isVerifyingMedic() && medicService.findByUserId(user.getId()).isPresent()){
            for (Order order: orders) {
                if(!clinicsList.contains(order.getClinic())){
                    clinicsList.add(order.getClinic());
                }
            }
            medicsList.add(medicService.findByUserId(user.getId()).get());
        }else if(user.isClinic() && !user.isVerifyingClinic() && clinicService.findByUserId(user.getId()).isPresent()){
            orders.forEach(order -> medicsList.add(order.getMedic()));
            for (Order order: orders) {
                if(!medicsList.contains(order.getMedic())){
                    medicsList.add(order.getMedic());
                }
            }
            clinicsList.add(clinicService.findByUserId(user.getId()).get());
        }else{
            for (Order order: orders) {
                if(!medicsList.contains(order.getMedic())){
                    medicsList.add(order.getMedic());
                }
                if(!clinicsList.contains(order.getClinic())){
                    clinicsList.add(order.getClinic());
                }

            }
        }

        mav.addObject("medicsList", medicsList);
        mav.addObject("clinicsList", clinicsList);
        mav.addObject("studiesList", studyService.getAll());

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

    @RequestMapping(value = "/filter-search", method = RequestMethod.GET)
    public String filterSearch(@Valid @ModelAttribute("filterForm") FilterForm filterForm, final BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "my-studies";
        }else{
            String out = "redirect:/my-studies?";
            if(filterForm.getDate() != null && !filterForm.getDate().isEmpty()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                try{
                    dateFormat.parse(filterForm.getDate().trim());
                }catch (ParseException pe){
                    return "my-studies";
                }
                out += "date=" + filterForm.getDate() + "&";
            }
            if(filterForm.getClinic_id() != null && filterForm.getClinic_id() != -1){
                if(clinicService.findByUserId(filterForm.getClinic_id()).isPresent()) {
                    out += "clinic=" + filterForm.getClinic_id().toString() + "&";
                }
            }
            if(filterForm.getMedic_id() != null && filterForm.getMedic_id() != -1){
                if(medicService.findByUserId(filterForm.getMedic_id()).isPresent()) {
                    out += "medic=" + filterForm.getMedic_id().toString() + "&";
                }
            }
            if(filterForm.getPatient_email() != null && !filterForm.getPatient_email().isEmpty()){
                if(userService.findByEmail(filterForm.getPatient_email()).isPresent()) {
                    out += "patient=" + userService.findByEmail(filterForm.getPatient_email()).get().getId() + "&";
                }
            }
            if(filterForm.getStudy_id() != null && filterForm.getStudy_id() != -1){
                if(studyService.findById(filterForm.getStudy_id()).isPresent()) {
                    out += "study=" + filterForm.getStudy_id().toString();
                }
            }

            return out;
        }
    }

}
