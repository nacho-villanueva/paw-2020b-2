package ar.edu.itba.paw.webapp.controller.api;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;

@Controller
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private ClinicService clinicService;

    @RequestMapping(value = "/clinic/get-clinics-by-medical-study", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Collection<Clinic>> getClinicsByMedicalStudy(@RequestParam(value = "study", required = false) String studyString){

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        if(studyString == null || studyString.isEmpty())
            return new ResponseEntity<>(clinicService.getAll(),headers,HttpStatus.OK);

        int studyType_id;

        try{
            studyType_id = Integer.parseInt(studyString);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(clinicService.getByStudyTypeId(studyType_id),headers,HttpStatus.OK);
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @RequestMapping(value= "/orders/get-order-by", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<HashMap<Order, String>> getOrdersByParams(@RequestParam(value = "date", required = false) String dateString,
                                                                    @RequestParam(value = "clinic", required = false) String clinicString,
                                                                    @RequestParam(value = "medic", required = false) String medicString,
                                                                    @RequestParam(value = "study", required = false) String studyString,
                                                                    @RequestParam(value = "patient", required = false) String patientString){
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        Collection<Order> orders = orderService.getAllDCMSP(
                Date.valueOf(dateString),
                clinicService.findByUserId(Integer.parseInt(clinicString)),
                medicService.findByUserId(Integer.parseInt(medicString)),
                Integer.parseInt(studyString),
                patientService.findByUser_id(Integer.parseInt(patientString))
                );


        //siempre voy a mandar al menos uno de estos 3: medic, clinic, patient
        //necesitaria unos metodos en OrderService que sean capaces de hacer queries por estos 5 parametros

        return new ResponseEntity<>(encoder(orders), headers, HttpStatus.OK);
    }

    private HashMap<Order, String> encoder(Collection<Order> orders){
        HashMap<Order, String> encodeds = new HashMap<>();
        for(Order order : orders){
            encodeds.put(order, urlEncoderService.encode(order.getOrder_id()));
        }
        return encodeds;
    }
}
