package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.MailNotificationService;
import ar.edu.itba.paw.service.ResultFormService;
import ar.edu.itba.paw.service.UrlEncoderService;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.ResultForm;
import ar.edu.itba.paw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

@Controller
public class ResultUploadController {

    //necesitamos una forma de conocer el order number para le que se cargan los resultados

    @Autowired
    private OrderService os;

    @Autowired
    private ResultFormService resultFormService;

    @Autowired
    private UrlEncoderService urlEncoderService;

    private long orderId;

    @RequestMapping("/upload-result/{encodedId}")
    public ModelAndView uploadResult(@PathVariable("encodedId") final String encodedId) {
        ModelAndView mav;
        long id = urlEncoderService.decode(encodedId);
        Optional<Order> o = os.findById(id);
        Order aux;
        if(o.isPresent()) {
            mav = new ModelAndView("upload-result");
            aux = o.get();
            mav.addObject("id", id);
            mav.addObject("order", aux);
            mav.addObject("resultForm", new ResultForm());
            orderId = id;
        }else{
            mav = new ModelAndView("redirect:/404");
            // 404 go to
        }

        return mav;
    }


    //

    @RequestMapping(value = "/result-uploaded", method = RequestMethod.POST)
    public String submit(@ModelAttribute ResultForm resultForm, @RequestParam("files") MultipartFile[] files, @RequestParam("sign") MultipartFile sign, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "index"; //There should be a way to validate and show errors on the form...
        }else{
            try{
                byte[] signBytes = sign.getBytes();
                //for each of the files uploaded as results, it registers a different result in the db
                for(MultipartFile file: files){
                    byte[] fileBytes = file.getBytes();
                    resultFormService.HandleOrderForm(resultForm, signBytes, sign.getContentType(), fileBytes, file.getContentType(), orderId);
                }
                return "redirect:view-study/" + urlEncoderService.encode(orderId);
            }catch (IOException e){
                return "redirect:index"; //TODO: RETURN 500 EXCEPTION PAGE
            }
        }
    }

}
