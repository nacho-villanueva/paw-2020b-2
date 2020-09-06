package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

    @Autowired
    private UserService us;
    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findById(1));
        return mav;
    }
    /*
    //url/view-study/asdas

    @RequestMapping("/view-study/{studyId}")
    public ModelAndView viewStudy(@PathVariable("studyId") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findById(id));
        return mav;
    }

    //url/?studyId=asdas
    @RequestMapping("/")
    public ModelAndView viewStudy2(@RequestParam(value = "studyId", required = false, defaultValue = "0") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findById(id));
        return mav;
    }
*/
//url/view-study/asdas

    @RequestMapping("/view-study-test/{studyId}")
    public ModelAndView viewStudy(@PathVariable("studyId") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findById(id));
        return mav;
    }


}
