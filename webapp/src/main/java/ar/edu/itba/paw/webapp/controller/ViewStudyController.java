package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.OrderService;
import ar.edu.itba.paw.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewStudyController {

    @Autowired
    private OrderService os;

    @Autowired
    private ResultService rs;

    @RequestMapping("/view-study/{orderId}")
    public ModelAndView uploadResults(@PathVariable("orderId") final long id) {
        final ModelAndView mav = new ModelAndView("view-study");
        mav.addObject("id", id);
        mav.addObject("order", os.findById(id));
        mav.addObject("results", rs.findByOrderId(id));
        return mav;
    }

}
