package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.service.OrderService;
import ar.edu.itba.paw.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ViewStudyController {

    @Autowired
    private OrderService os;


    @RequestMapping("/view-study/{orderId}")
    public ModelAndView uploadResults(@PathVariable("orderId") final long id) {
        ModelAndView mav;
        Optional<Order> o = os.findById(id);
        Order aux;
        if(o.isPresent()) {
            mav = new ModelAndView("view-study");
            aux = o.get();
            mav.addObject("id", id);
            mav.addObject("order", aux);
            mav.addObject("results", aux.getStudy_results());
        }else{
            mav = new ModelAndView("redirect:/404");
            // 404 go to
        }

        return mav;
    }

}
