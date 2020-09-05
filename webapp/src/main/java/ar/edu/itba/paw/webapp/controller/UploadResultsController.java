package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadResultsController {

    private MedicalOrder mo;

    @RequestMapping("/upload-results/{orderId}")
    public ModelAndView uploadResults(@PathVariable("orderId") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("order", mo.findById(id));
        return mav;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String submit(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
        modelMap.addAttribute("file", file);
        return "fileUploadView";
    }
}
