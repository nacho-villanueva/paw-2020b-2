package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UrlEncoderService;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ViewStudyController {

    @Autowired
    private UserService us;

    @Autowired
    private OrderService os;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @RequestMapping("/view-study/{encodedId}")
    public ModelAndView viewStudy(@PathVariable("encodedId") final String encodedId, @ModelAttribute("successMedicName") final String medicName) {
        ModelAndView mav;
        long id = urlEncoderService.decode(encodedId);
        Optional<Order> o = os.findById(id);
        Order aux;
        if(o.isPresent()) {
            mav = new ModelAndView("view-study");
            aux = o.get();
            mav.addObject("encodedId",encodedId);
            mav.addObject("id", id);
            mav.addObject("order", aux);
            mav.addObject("results", aux.getStudyResults());
            mav.addObject("loggedUser",loggedUser());
            if(medicName != null)
                mav.addObject("medicName",medicName);

        }else{
            mav = new ModelAndView("redirect:/404");
            // 404 go to
        }



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


}
