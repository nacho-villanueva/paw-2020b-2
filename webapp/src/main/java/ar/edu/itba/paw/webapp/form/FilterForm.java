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
    private String patientEmail;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    @Min(-1)
    private Integer studyId, clinicId, medicId;

    public FilterForm() {
    }

    public FilterForm(Integer studyId, Integer clinicId, Integer medicId, String patientEmail, String date){
        this.date = date;
        this.patientEmail = patientEmail;
        this.medicId = medicId;
        this.clinicId = clinicId;
        this.studyId = studyId;
    }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String s){
        patientEmail = s;
    }

    public String getDate(){ return date; }
    public void setDate(String s){
        date = s;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
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
        this.patientEmail = null;
        this.medicId = -1;
        this.studyId = -1;
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
        if(this.studyId != null && this.studyId != -1)
            parameters.put(OrderService.Parameters.STUDYTYPE, this.studyId.toString());
        if(this.patientEmail != null && !this.patientEmail.isEmpty())
            parameters.put(OrderService.Parameters.PATIENT, this.patientEmail);

        return parameters;
    }

    public void decodeForm(String uri) {

        MultiValueMap<String, String> p = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

        this.clinicId = decodeInt(decodeVal(p, "clinicId"));
        this.studyId = decodeInt(decodeVal(p, "studyId"));
        this.medicId = decodeInt(decodeVal(p, "medicId"));
        this.patientEmail = decodeVal(p, "patientEmail");
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
