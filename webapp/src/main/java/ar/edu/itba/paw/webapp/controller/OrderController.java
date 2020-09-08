package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.OrderFormService;
import ar.edu.itba.paw.model.OrderForm;
import ar.edu.itba.paw.persistence.ClinicDao;
import ar.edu.itba.paw.persistence.MedicDao;
import ar.edu.itba.paw.persistence.StudyTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/create-order")
public class OrderController {

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private ClinicDao clinicDao;

    @Autowired
    private MedicDao medicDao;

    @Autowired
    private StudyTypeDao studyTypeDao;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getOrderCreationForm() {
        final ModelAndView mav = new ModelAndView("create-order");

        mav.addObject("medicsList", medicDao.getAll());

        mav.addObject("studiesList", studyTypeDao.getAll());

        mav.addObject("clinicsList", clinicDao.getAll());
        mav.addObject("orderForm", new OrderForm());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createOrder(@ModelAttribute OrderForm orderForm, @RequestParam("orderAttach") MultipartFile file, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            // TODO: RETORNAR ERROR PAGE
            return "index";

        } else {
            try {
                byte[] fileBytes = file.getBytes();
                String id = orderFormService.HandleOrderForm(orderForm, fileBytes, file.getContentType()).toString();
                return "redirect:view-study/" + id;
            } catch (IOException e) {
                return "index"; //TODO: RETURN EXCEPTION PAGE
            }

        }

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request" + e.getMessage());
    }

}
