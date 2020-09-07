package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.OrderFormService;
import ar.edu.itba.paw.model.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/create-order")
public class OrderController {

    @Autowired
    private OrderFormService orderFormService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getOrderCreationForm() {
        final ModelAndView mav = new ModelAndView("create-order");

        Map<Long, String> medicsList = new HashMap<>();
        medicsList.put(1L, "Dr. Pepito");
        medicsList.put(2L, "Dr. Garcia");
        medicsList.put(3L, "Dr. Perez");
        medicsList.put(4L, "Dr. Gonzalez");
        mav.addObject("medicsList", medicsList); // TODO: CONSEGUIR LISTA DE DOCTORES

        mav.addObject("studiesList", new String[] {"Colonoscopy", "X-ray", "Blood Test", "MRI"}); // TODO: CONSEGUIR LISTA DE STUDIOS

        Map<Long, String> clinicsList = new HashMap<>();
        clinicsList.put(1L, "Laboratorio Idalgo");
        clinicsList.put(2L, "Zona Vital");
        clinicsList.put(3L, "Diagnostico Maipu");
        clinicsList.put(4L, "Central Lab");
        mav.addObject("clinicsList", clinicsList); // TODO: CONSEGUIR LISTA DE CLINICAS
        mav.addObject("orderForm", new OrderForm());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createOrder(@ModelAttribute OrderForm orderForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            // TODO: RETORNAR ERROR PAGE
            return "index";

        } else {

            String id = orderFormService.HandleOrderForm(orderForm, null); //TODO: ADD IDENTIFICATION FILE
            return "view-study/" + id;
        }

    }

}
