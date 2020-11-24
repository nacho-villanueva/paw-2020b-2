package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Result;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.webapp.exceptions.OrderNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UploadedFileFailedToLoadException;
import ar.edu.itba.paw.webapp.form.ResultForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private UserService userService;

    private long orderId;

    @RequestMapping("/upload-result/{encodedId}")
    public ModelAndView uploadResult(@PathVariable("encodedId") final String encodedId, @ModelAttribute("resultForm") ResultForm resultForm) {
        return createUploadResultView(encodedId, resultForm);
    }


    //

    @RequestMapping(value = "/upload-result/{encodedId}", method = RequestMethod.POST)
    public ModelAndView submit(@PathVariable("encodedId") final String encodedId, @Valid @ModelAttribute ResultForm resultForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return createUploadResultView(encodedId, resultForm);
        }
        else{
            try{
                byte[] signBytes = resultForm.getSign().getBytes();
                //for each of the files uploaded as results, it registers a different result in the db
                for(MultipartFile file: resultForm.getFiles()){
                    byte[] fileBytes = file.getBytes();

                    Result result = resultService.register(orderId,
                            file.getContentType(),
                            fileBytes,
                            resultForm.getSign().getContentType(),
                            signBytes,
                            new Date(System.currentTimeMillis()),
                            resultForm.getResponsible_name(),
                            resultForm.getResponsible_licenceNumber());
                }
                return new ModelAndView("redirect:/view-study/" + urlEncoderService.encode(orderId));
            } catch (IOException e){
                throw new UploadedFileFailedToLoadException();
            }
        }
    }

    public ModelAndView createUploadResultView(final String encodedId, final ResultForm resultForm){
        ModelAndView mav;
        long id = urlEncoderService.decode(encodedId);
        Optional<Order> o = os.findById(id);
        Order aux;
        
        if(!o.isPresent())
            throw new OrderNotFoundException();

        mav = new ModelAndView("upload-result");
        aux = o.get();
        mav.addObject("encodedId", encodedId);
        mav.addObject("id", id);
        mav.addObject("order", aux);
        mav.addObject("resultForm", resultForm);
        orderId = id;
        return mav;
    }

}
