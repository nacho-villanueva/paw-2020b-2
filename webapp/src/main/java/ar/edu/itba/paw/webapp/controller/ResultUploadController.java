package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Result;
import ar.edu.itba.paw.services.ResultService;
import ar.edu.itba.paw.services.UrlEncoderService;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.webapp.exceptions.UploadedFileFailedToLoadException;
import ar.edu.itba.paw.webapp.form.ResultForm;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

@Controller
public class ResultUploadController {

    //necesitamos una forma de conocer el order number para le que se cargan los resultados

    @Autowired
    private OrderService os;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private ValidationService vs;

    @Autowired
    private ResultService resultService;


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
    public ModelAndView submit(@Valid @ModelAttribute ResultForm resultForm, BindingResult bindingResult, @RequestParam("files") MultipartFile[] files, @RequestParam("sign") MultipartFile sign) {
        if(bindingResult.hasErrors()) {
            return new ModelAndView("index");
        }
        else{
            try{
                byte[] signBytes = sign.getBytes();
                //for each of the files uploaded as results, it registers a different result in the db
                for(MultipartFile file: files){
                    byte[] fileBytes = file.getBytes();

                    Result result = resultService.register(orderId,
                            file.getContentType(),
                            fileBytes,
                            sign.getContentType(),
                            signBytes,
                            new Date(System.currentTimeMillis()),
                            resultForm.getResponsible_name(),
                            resultForm.getResponsible_licence_number());
                }
                return new ModelAndView("redirect:view-study/" + urlEncoderService.encode(orderId));
            } catch (IOException e){
                throw new UploadedFileFailedToLoadException();
            }
        }
    }

}
