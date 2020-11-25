package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.webapp.form.constraintGroups.AdvancedSearchGroup;
import ar.edu.itba.paw.webapp.form.validators.ValidAvailabilityHours;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;


public class AdvancedSearchClinicForm {

    private String clinicName;

    private String medicalStudy;

    private String medicalPlan;

    // for time
    @ValidAvailabilityHours(groups = AdvancedSearchGroup.class)
    private ClinicHoursForm availableTime;

    private boolean[] isAvailable;


    // to Save order info
    //  should not be needed for regular searches
    private OrderForm orderForm;

    public AdvancedSearchClinicForm() {
        this.orderForm = new OrderForm();
        resetValues();
    }

    public void resetValues(){
        clinicName = null;
        medicalStudy =null;
        medicalPlan =null;

        this.availableTime = new ClinicHoursForm();
        this.isAvailable = new boolean[ClinicHours.getDaysOfWeek()];
        for (int i = 0; i < ClinicHours.getDaysOfWeek(); i++) {
            this.isAvailable[i] = true;
        }

        // keep order info intact
    }

    public void decodeForm(String uri) {

        MultiValueMap<String, String> p = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

        this.clinicName = decodeVal(p,"clinicName");
        this.medicalStudy = decodeVal(p,"medicalStudy");
        this.medicalPlan = decodeVal(p,"medicalPlan");

        String[] auxOT = new String[ClinicHours.getDaysOfWeek()];
        String[] auxCT = new String[ClinicHours.getDaysOfWeek()];

        for (int i=0; i < ClinicHours.getDaysOfWeek(); i++){
            auxOT[i] = decodeVal(p,"availableTime.openingTime["+Integer.toString(i)+"]");
            auxCT[i] = decodeVal(p,"availableTime.closingTime["+Integer.toString(i)+"]");
        }
        this.availableTime.setOpeningTime(auxOT);
        this.availableTime.setClosingTime(auxCT);

        // orderFormData
        // medicId is Integer
        // clinicId is Integer
        this.orderForm.setDescription(decodeVal(p,"orderForm.description"));
        //patient Data
        this.orderForm.setPatientInsurancePlan(decodeVal(p, "orderForm.patientInsurancePlan"));
        this.orderForm.setPatientInsuranceNumber(decodeVal(p, "orderForm.patientInsuranceNumber"));
        this.orderForm.setPatientEmail(decodeVal(p, "orderForm.patientEmail"));
        this.orderForm.setPatientName(decodeVal(p, "orderForm.patientName"));
    }

    public ClinicHours getClinicHours(){
        ClinicHours ret = new ClinicHours();

        String[] auxOT = this.availableTime.getOpeningTime();
        String[] auxCT = this.availableTime.getClosingTime();
        String defaultFromTime = "00:00";
        String defaultToTime = "23:59";
        String timeSuffix = ":00";

        for(int day=0 ; day < ClinicHours.getDaysOfWeek(); day++){
            if(this.isAvailable[day])
                ret.setDayHour(day,
                        LocalTime.parse(((auxOT[day] == null)?defaultFromTime:auxOT[day]).concat(timeSuffix)),
                        LocalTime.parse(((auxCT[day] == null)?defaultToTime:auxCT[day]).concat(timeSuffix))
                );
        }

        return ret;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getMedicalStudy() {
        return medicalStudy;
    }

    public void setMedicalStudy(String medicalStudy) {
        this.medicalStudy = medicalStudy;
    }

    public String getMedicalPlan() {
        return medicalPlan;
    }

    public void setMedicalPlan(String medicalPlan) {
        this.medicalPlan = medicalPlan;
    }

    public ClinicHoursForm getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(ClinicHoursForm availableTime) {
        this.availableTime = availableTime;
    }

    public boolean[] getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean[] isAvailable) {
        this.isAvailable = isAvailable;
    }

    public OrderForm getOrderForm() {
        return orderForm;
    }

    public void setOrderForm(OrderForm orderForm) {
        this.orderForm = orderForm;
    }

    private String decodeVal(MultiValueMap<String, String> parameters, String key){
        String enc = StandardCharsets.UTF_8.name();
        try {
            String val = parameters.getFirst(UriUtils.encode(key,enc));
            if(val == null || val.trim().isEmpty())
                return null;

            return UriUtils.decode(val.replace('+',' '),enc).trim();
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return parameters.getFirst(key);
    }
}
