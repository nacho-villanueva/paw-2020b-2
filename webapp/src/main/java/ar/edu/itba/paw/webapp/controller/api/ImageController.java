package ar.edu.itba.paw.webapp.controller.api;

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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResultService resultService;

    @RequestMapping(value = "/study/{encodedId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getOrderIdentification(@PathVariable("encodedId") final String encodedId, @RequestParam(value = "attr", required = false) final String attribute) {

        ResponseEntity<byte[]> responseEntity;

        byte[] media = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        HttpStatus responseStatus;

        if(attribute==null)
            return new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);

        long id = urlEncoderService.decode(encodedId);

        Optional<Order> orderOpt = orderService.findById(id);

        if(orderOpt.isPresent()){
            // image present
            Order order = orderOpt.get();

            switch (attribute){
                case "identification":
                    headers.setContentType(MediaType.parseMediaType(order.getIdentification_type()));
                    media = order.getIdentification();
                    responseStatus = HttpStatus.OK;
                    break;
                default:
                    responseStatus = HttpStatus.BAD_REQUEST;
            }

        }else{
            // not present
            responseStatus = HttpStatus.NOT_FOUND;
        }

        responseEntity = new ResponseEntity<>(media, headers, responseStatus);

        return responseEntity;
    }

    @RequestMapping(value = "/result/{encodedId}/{resultId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getResultIdentification(@PathVariable("encodedId") final String encodedId, @PathVariable("resultId") final long resultId, @RequestParam(value = "attr", required = false) final String attribute) {

        ResponseEntity<byte[]> responseEntity;

        byte[] media = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        HttpStatus responseStatus;

        if(attribute==null)
            return new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);

        Optional<Result> resultOpt = resultService.findById(resultId);

        long id = urlEncoderService.decode(encodedId);

        if(resultOpt.isPresent() && resultOpt.get().getOrder_id()==id){
            // image present
            Result result = resultOpt.get();

            switch (attribute){
                case "identification":
                    headers.setContentType(MediaType.parseMediaType(result.getIdentification_type()));
                    media = result.getIdentification();
                    responseStatus = HttpStatus.OK;
                    break;
                case "result-data":
                    headers.setContentType(MediaType.parseMediaType(result.getData_type()));
                    media = result.getData();
                    responseStatus = HttpStatus.OK;
                    break;
                default:
                    responseStatus = HttpStatus.BAD_REQUEST;
            }

        }else{
            // not present
            responseStatus = HttpStatus.NOT_FOUND;
        }

        responseEntity = new ResponseEntity<>(media, headers, responseStatus);

        return responseEntity;
    }

}
