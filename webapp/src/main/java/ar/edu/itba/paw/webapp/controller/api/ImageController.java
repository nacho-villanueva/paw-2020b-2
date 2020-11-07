package ar.edu.itba.paw.webapp.controller.api;

import ar.edu.itba.paw.models.Media;
import ar.edu.itba.paw.services.ImageService;
import ar.edu.itba.paw.services.UrlEncoderService;
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
    private ImageService imageService;

    @RequestMapping(value = "/medic/{medicId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getMedicImage(@PathVariable("medicId") final int medicId, @RequestParam(value = "attr", required = false) final String attribute) {

        ResponseEntity<byte[]> responseEntity;

        byte[] media = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        HttpStatus responseStatus;

        if(attribute==null)
            return new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);

        Optional<Media> imageOptional = imageService.getMedicImage(medicId,attribute);

        if(imageOptional.isPresent()){
            // image present
            Media image = imageOptional.get();

            headers.setContentType(MediaType.parseMediaType(image.getContentType()));
            media = image.getFile();
            responseStatus = HttpStatus.OK;

        }else{
            // not present
            responseStatus = HttpStatus.NOT_FOUND;
        }

        responseEntity = new ResponseEntity<>(media, headers, responseStatus);

        return responseEntity;
    }

    @RequestMapping(value = "/study/{encodedId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getOrderImage(@PathVariable("encodedId") final String encodedId, @RequestParam(value = "attr", required = false) final String attribute) {

        ResponseEntity<byte[]> responseEntity;

        byte[] media = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        HttpStatus responseStatus;

        if(attribute==null)
            return new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);

        long id = urlEncoderService.decode(encodedId);

        Optional<Media> imageOptional = imageService.getOrderImage(id,attribute);

        if(imageOptional.isPresent()){
            // image present
            Media image = imageOptional.get();

            headers.setContentType(MediaType.parseMediaType(image.getContentType()));
            media = image.getFile();
            responseStatus = HttpStatus.OK;

        }else{
            // not present
            responseStatus = HttpStatus.NOT_FOUND;
        }

        responseEntity = new ResponseEntity<>(media, headers, responseStatus);

        return responseEntity;
    }

    @RequestMapping(value = "/result/{encodedId}/{resultId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getResultImage(@PathVariable("encodedId") final String encodedId, @PathVariable("resultId") final long resultId, @RequestParam(value = "attr", required = false) final String attribute) {

        ResponseEntity<byte[]> responseEntity;

        byte[] media = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        HttpStatus responseStatus;

        if(attribute==null)
            return new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);

        long id = urlEncoderService.decode(encodedId);

        Optional<Media> imageOptional = imageService.getResultImage(resultId,id,attribute);

        if(imageOptional.isPresent()){
            // image present
            Media image = imageOptional.get();

            headers.setContentType(MediaType.parseMediaType(image.getContentType()));
            media = image.getFile();
            responseStatus = HttpStatus.OK;

        }else{
            // not present
            responseStatus = HttpStatus.NOT_FOUND;
        }

        responseEntity = new ResponseEntity<>(media, headers, responseStatus);

        return responseEntity;
    }

}
