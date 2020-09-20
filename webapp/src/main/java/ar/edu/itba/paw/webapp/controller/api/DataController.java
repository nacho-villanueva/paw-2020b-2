package ar.edu.itba.paw.webapp.controller.api;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.services.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private ClinicService clinicService;

    @RequestMapping(value = "/clinic/get-clinics-by-medical-study", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Clinic> getClinicsByMedicalStudy(@RequestParam(value = "study", required = false) String studyString){

        if(studyString == null || studyString.isEmpty())
            return clinicService.getAllClinics();

        int studyType_id;

        try{
            studyType_id = Integer.parseInt(studyString);
        }catch (NumberFormatException e){
            return null;
        }

        return clinicService.getByStudyTypeId(studyType_id);
    }
}
