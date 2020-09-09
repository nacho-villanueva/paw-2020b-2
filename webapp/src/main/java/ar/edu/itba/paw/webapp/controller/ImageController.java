package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.UrlEncoderService;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.Result;
import ar.edu.itba.paw.services.OrderService;
import ar.edu.itba.paw.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
@RequestMapping("/assets/image")
public class ImageController {

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResultService resultService;

    @RequestMapping(value = "/order/{encodedId}/identification", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getOrderIdentification(@PathVariable("encodedId") final String encodedId) {

        ResponseEntity<byte[]> responseEntity;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        long id = urlEncoderService.decode(encodedId);

        Optional<Order> orderOpt = orderService.findById(id);

        if(orderOpt.isPresent()){
            // image present

            Order order = orderOpt.get();

            headers.setContentType(MediaType.parseMediaType(order.getIdentification_type()));
            byte[] media = order.getIdentification();

            responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);

        }else{
            // not present
            responseEntity = new ResponseEntity<>(null,headers,HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/result/{encodedId}/{resultId}/identification", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getResultIdentification(@PathVariable("encodedId") final String encodedId, @PathVariable("resultId") final long resultId) {

        ResponseEntity<byte[]> responseEntity;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        Optional<Result> resultOpt = resultService.findById(resultId);

        long id = urlEncoderService.decode(encodedId);


        if(resultOpt.isPresent() && resultOpt.get().getOrder_id()==id){
            // image present

            Result result = resultOpt.get();

            headers.setContentType(MediaType.parseMediaType(result.getIdentification_type()));
            byte[] media = result.getIdentification();

            responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);

        }else{
            // not present
            responseEntity = new ResponseEntity<>(null,headers,HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/result/{encodedId}/{resultId}/result-data", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getResultResultData(@PathVariable("encodedId") final String encodedId, @PathVariable("resultId") final long resultId) {

        ResponseEntity<byte[]> responseEntity;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        Optional<Result> resultOpt = resultService.findById(resultId);

        long id = urlEncoderService.decode(encodedId);

        if(resultOpt.isPresent() && resultOpt.get().getOrder_id()==id){
            // image present

            Result result = resultOpt.get();

            headers.setContentType(MediaType.parseMediaType(result.getData_type()));
            byte[] media = result.getData();

            responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);

        }else{
            // not present
            responseEntity = new ResponseEntity<>(null,headers,HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

}
