package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.services.MedicService;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.UrlEncoderService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.MedicNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.OrderNotFoundException;
import ar.edu.itba.paw.webapp.form.ShareOrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;
import java.util.function.Consumer;

@Controller
public class ShareOrderController {

    @Autowired
    private MedicService ms;

    @Autowired
    private UserService us;

    @Autowired
    private UrlEncoderService ues;

    @Autowired
    private OrderService os;

    @RequestMapping(value = "/share-order/{encodedId}", method = RequestMethod.GET)
    public ModelAndView getShareOrder(@PathVariable("encodedId") final String encodedId, @Valid @ModelAttribute("shareOrderForm")ShareOrderForm shareOrderForm){
        final Optional<Order> maybeOrder = os.findById(ues.decode(encodedId));
        Order order;
        if(maybeOrder.isPresent())
            order = maybeOrder.get();
        else
            throw new OrderNotFoundException();

        if(!order.getPatient_email().equals(loggedUser().getEmail()))
            return new ModelAndView("redirect:/");

        ModelAndView mav = new ModelAndView("share-order");
        mav.addObject("orderType", order.getStudy().getName());
        mav.addObject("shareOrderForm", shareOrderForm);
        mav.addObject("encondedId", encodedId);
        return mav;
    }

    @RequestMapping(value = "/share-order/{encodedId}", method = RequestMethod.POST)
    public ModelAndView postShareOrder(@PathVariable("encodedId") final String encodedId, @Valid @ModelAttribute("shareOrderForm")ShareOrderForm shareOrderForm, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        final Optional<Order> maybeOrder = os.findById(ues.decode(encodedId));
        Order order;
        if(maybeOrder.isPresent())
            order = maybeOrder.get();
        else
            throw new OrderNotFoundException();

        if(!order.getPatient_email().equals(loggedUser().getEmail()))
            return new ModelAndView("redirect:/");

        if(bindingResult.hasErrors()){
            ModelAndView mav = new ModelAndView("share-order");
            mav.addObject("orderType", order.getStudy().getName());
            return mav;
        }

        ModelAndView viewStudyMav = new ModelAndView("redirect:/view-study/" + encodedId);

        final Optional<User> maybeUser = us.findByEmail(shareOrderForm.getMedicEmail());
        final Optional<Medic> maybeMedic;
        if(maybeUser.isPresent())
            maybeMedic = ms.findByUserId(maybeUser.get().getId());
        else
            throw new MedicNotFoundException();

        if(maybeMedic.isPresent()) {
            redirectAttributes.addFlashAttribute("successMedicName", maybeMedic.get().getName());
        }
        else
            throw new MedicNotFoundException();

        //os.shareToMedic(maybeOrder.get(), maybeMedic.get());

        return viewStudyMav;
    }

    @ModelAttribute
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = us.findByEmail(auth.getName());
        //LOGGER.debug("Logged user is {}", user);
        //TODO: see more elegant solution
        return user.orElse(null);
    }


}
