package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.persistence.OrderDao;
import ar.edu.itba.paw.persistence.ResultDao;
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
    private OrderDao orderDao;

    //@Autowired
    //private ResultDao resultDao;

    @RequestMapping(value = "/order/{id}/identification", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getOrderIdentification(@PathVariable long id) {

        ResponseEntity<byte[]> responseEntity;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        Optional<Order> orderOpt = orderDao.findById(id);

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

    //@RequestMapping(value = "/result/{id}/identification", method = RequestMethod.GET)

    //@RequestMapping(value = "/result/{id}/result-data", method = RequestMethod.GET)

}
