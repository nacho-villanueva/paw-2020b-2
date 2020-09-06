package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResultUploadController {


    @RequestMapping("/upload-result")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("upload-result");
        return mav;
    }


    //

    @RequestMapping(value = "/result-uploaded", method = RequestMethod.POST)
    public String submit(@RequestParam("files") MultipartFile[] files, @RequestParam("sign") MultipartFile sign, ModelMap modelMap) {

        // TODO: validations for submitted data

        // TODO: upload files to the database in this step

        modelMap.addAttribute("files", files);
        modelMap.addAttribute("sign", sign);
        return "result-uploaded";
    }
}
