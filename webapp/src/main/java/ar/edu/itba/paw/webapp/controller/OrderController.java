package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.model.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping(value = "/create-order")
public class OrderController {

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private StudyTypeService studyService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService us;

    @Autowired
    private ValidationService vs;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getOrderCreationForm() {
        final ModelAndView mav = new ModelAndView("create-order");
        Medic m = null;
        if(medicService.findByUserId(loggedUser().getId()).isPresent())
            m = medicService.findByUserId(loggedUser().getId()).get();
        mav.addObject("loggedMedic", m);
        mav.addObject("studiesList", studyService.getAll());
        mav.addObject("clinicsList", clinicService.getAll());
        mav.addObject("orderForm", new OrderForm());
        return mav;
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createOrder(@ModelAttribute OrderForm orderForm, @RequestParam("orderAttach") MultipartFile file, BindingResult bindingResult) {
        //TODO: change for proper validations
        if(!vs.isValidEmail(orderForm.getPatientEmail())
                || !vs.isValidMedicPlan(orderForm.getPatient_insurance_plan())
                || !vs.isValidMedicPlanNumber(orderForm.getPatient_insurance_number())
                || !vs.isValidName(orderForm.getPatientName())) {
            return "/create-order";
        }

        if (bindingResult.hasErrors() && !medicService.findByUserId(loggedUser().getId()).isPresent()) {

            return "/create-order"; // TODO: RETURN VALIDATION ERRORS

        } else {
            try {
                orderForm.setMedicId(medicService.findByUserId(loggedUser().getId()).get().getUser_id());
                byte[] fileBytes = file.getBytes();
                long id = orderFormService.HandleOrderForm(orderForm, fileBytes, file.getContentType());
                return "redirect:view-study/" + urlEncoderService.encode(id);
            } catch (IOException e) {
                return "redirect:index"; //TODO: RETURN 500 EXCEPTION PAGE
            }

        }

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request" + e.getMessage());
    }

}
