package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping(value="/my-orders")
public class MyOrdersController {

    @Autowired
    private UserService userService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView myOrders(@ModelAttribute("filterForm") FilterForm filterForm){
        ModelAndView mav = new ModelAndView("my-studies");

        filterForm.resetValues();

        mav.addObject("filterForm", filterForm);

        Map<OrderService.Parameters, String> parameters = filterForm.getParameters();

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


    private void listingSetup(ModelAndView mav, Map<OrderService.Parameters, String> parameters){
        User user = loggedUser();

        Collection<Order> orders = orderService.filterOrders(user, parameters);
        Collection<Clinic> clinicsList = new ArrayList<>();
        Collection<Medic> medicsList = new ArrayList<>();
        Collection<StudyType> studiesList = new ArrayList<>();

        if(user.isMedic() && !user.isVerifying() && medicService.findByUserId(user.getId()).isPresent()){
            medicsList.add(medicService.findByUserId(user.getId()).get());
            for (Order order: orders) {
                if(!clinicsList.stream().anyMatch(clinic -> clinic.getUserId() == order.getClinic().getUserId())){
                    clinicsList.add(order.getClinic());
                }
            }

        }else if(user.isClinic() && !user.isVerifying() && clinicService.findByUserId(user.getId()).isPresent()){
            clinicsList.add(clinicService.findByUserId(user.getId()).get());
            for (Order order: orders) {
                if(!medicsList.stream().anyMatch(medic -> medic.getUserId() == order.getMedic().getUserId())){
                    medicsList.add(order.getMedic());
                }
            }

        }else if(user.isPatient()) {
            for (Order order : orders) {
                if(!medicsList.stream().anyMatch(medic -> medic.getUserId() == order.getMedic().getUserId())){
                    medicsList.add(order.getMedic());
                }
                if(!clinicsList.stream().anyMatch(clinic -> clinic.getUserId() == order.getClinic().getUserId())){
                    clinicsList.add(order.getClinic());
                }

            }
        }

        for (Order order: orders) {
            if(!studiesList.stream().anyMatch(study -> study.getId() == order.getStudy().getId())){
                studiesList.add(order.getStudy());
            }
        }

        mav.addObject("loggedUser", user);
        mav.addObject("medicsList", medicsList);
        mav.addObject("clinicsList", clinicsList);
        mav.addObject("studiesList", studiesList);

        Map<Long, String> encodeds = new HashMap<>();

        encoder(orders, encodeds);

        mav.addObject("ordersList", orders);
        mav.addObject("encodedList", encodeds);

    }

    private void encoder(Collection<Order> orders, Map<Long, String> encodeds){
        for(Order order : orders){
            encodeds.put(order.getOrderId(), urlEncoderService.encode(order.getOrderId()));
        }
    }

    @RequestMapping(method = RequestMethod.GET,params = {"submit=search"})
    public ModelAndView filterSearch(@Valid @ModelAttribute("filterForm") FilterForm filterForm, final BindingResult bindingResult,
                               HttpServletRequest httpServletRequest){

        String uri = httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString();

        filterForm.decodeForm(uri);

        ModelAndView mav = new ModelAndView("my-studies");

        if (bindingResult.hasErrors()) {
            mav.addObject("errorAlert",true);
        } else {

            Map<OrderService.Parameters, String> parameters = filterForm.getParameters();

            listingSetup(mav, parameters);

        }

        return mav;
    }

    private String decode(String value){
        try {
            return UriUtils.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        return value;
    }

}
