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
    private Integer study_id, clinicId, medicId;

    public FilterForm() {
    }

    public FilterForm(Integer study_id, Integer clinicId, Integer medicId, String patient_email, String date){
        this.date = date;
        this.patient_email = patient_email;
        this.medicId = medicId;
        this.clinicId = clinicId;
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

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getMedicId() {
        return medicId;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }

    public void resetValues() {
        this.date = null;
        this.patient_email = null;
        this.medicId = -1;
        this.study_id = -1;
        this.clinicId =-1;
    }

    public HashMap<OrderService.Parameters, String> getParameters(){
        HashMap<OrderService.Parameters, String> parameters = new HashMap<>();
        if(this.date != null && !this.date.isEmpty())
            parameters.put(OrderService.Parameters.DATE, this.date);
        if(this.clinicId != null && this.clinicId != -1)
            parameters.put(OrderService.Parameters.CLINIC, this.clinicId.toString());
        if(this.medicId != null && this.medicId != -1)
            parameters.put(OrderService.Parameters.MEDIC, this.medicId.toString());
        if(this.study_id != null && this.study_id != -1)
            parameters.put(OrderService.Parameters.STUDYTYPE, this.study_id.toString());
        if(this.patient_email != null && !this.patient_email.isEmpty())
            parameters.put(OrderService.Parameters.PATIENT, this.patient_email);

        return parameters;
    }

    public void decodeForm(String uri) {

        MultiValueMap<String, String> p = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

        this.clinicId = decodeInt(decodeVal(p, "clinicId"));
        this.study_id = decodeInt(decodeVal(p, "study_id"));
        this.medicId = decodeInt(decodeVal(p, "medicId"));
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
