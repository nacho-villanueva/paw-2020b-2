package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.services.OrderService;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.validation.constraints.Min;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class FilterForm {

    @Email
    private String patient_email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    @Min(-1)
    private Integer study_id, clinic_id, medic_id;

    public FilterForm() {
    }

    public FilterForm(Integer study_id, Integer clinic_id, Integer medic_id, String patient_email, String date){
        this.date = date;
        this.patient_email = patient_email;
        this.medic_id = medic_id;
        this.clinic_id = clinic_id;
        this.study_id = study_id;
    }

    public String getPatient_email() { return patient_email; }
    public void setPatient_email(String s){
        patient_email = s;
    }

    public String getDate(){ return date; }
    public void setDate(String s){
        date = s;
    }

    public Integer getStudy_id() {
        return study_id;
    }

    public void setStudy_id(Integer study_id) {
        this.study_id = study_id;
    }

    public Integer getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(Integer clinic_id) {
        this.clinic_id = clinic_id;
    }

    public Integer getMedic_id() {
        return medic_id;
    }

    public void setMedic_id(Integer medic_id) {
        this.medic_id = medic_id;
    }

    public void resetValues() {
        this.date = null;
        this.patient_email = null;
        this.medic_id = -1;
        this.study_id = -1;
        this.clinic_id =-1;
    }

    public HashMap<OrderService.Parameters, String> getParameters(){
        HashMap<OrderService.Parameters, String> parameters = new HashMap<>();
        if(this.date != null && !this.date.isEmpty())
            parameters.put(OrderService.Parameters.DATE, this.date);
        if(this.clinic_id != null && this.clinic_id != -1)
            parameters.put(OrderService.Parameters.CLINIC, this.clinic_id.toString());
        if(this.medic_id != null && this.medic_id != -1)
            parameters.put(OrderService.Parameters.MEDIC, this.medic_id.toString());
        if(this.study_id != null && this.study_id != -1)
            parameters.put(OrderService.Parameters.STUDYTYPE, this.study_id.toString());
        if(this.patient_email != null && !this.patient_email.isEmpty())
            parameters.put(OrderService.Parameters.PATIENT, this.patient_email);

        return parameters;
    }

    public void decodeForm(String uri) {

        MultiValueMap<String, String> p = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

        this.clinic_id = decodeInt(decodeVal(p, "clinic_id"));
        this.study_id = decodeInt(decodeVal(p, "study_id"));
        this.medic_id = decodeInt(decodeVal(p, "medic_id"));
        this.patient_email = decodeVal(p, "patient_email");
        this.date = decodeVal(p, "date");
    }

    private String decodeVal(MultiValueMap<String, String> parameters, String key){
        String enc = StandardCharsets.UTF_8.name();
        try {
            String val = parameters.getFirst(key);
            if(val == null || val.trim().isEmpty())
                return null;

            return UriUtils.decode(val.replace('+',' '),enc).trim();
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return parameters.getFirst(key);
    }

    private int decodeInt(String decodedVal){
        if(decodedVal == null){
            return -1;
        }else{
            return Integer.parseInt(decodedVal);
        }
    }
}
