package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.ClinicHours;
import ar.edu.itba.paw.webapp.form.validators.ClinicHoursIsValid;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Time;

@ClinicHoursIsValid.List(value = {
        @ClinicHoursIsValid(day = 0, isOpen = "sundayOpens", openTime = "sundayOpenTime", closeTime = "sundayCloseTime"),
        @ClinicHoursIsValid(day = 1, isOpen = "mondayOpens", openTime = "mondayOpenTime", closeTime = "mondayCloseTime"),
        @ClinicHoursIsValid(day = 2, isOpen = "tuesdayOpens", openTime = "tuesdayOpenTime", closeTime = "tuesdayCloseTime"),
        @ClinicHoursIsValid(day = 3, isOpen = "wednesdayOpens", openTime = "wednesdayOpenTime", closeTime = "wednesdayCloseTime"),
        @ClinicHoursIsValid(day = 4, isOpen = "thursdayOpens", openTime = "thursdayOpenTime", closeTime = "thursdayCloseTime"),
        @ClinicHoursIsValid(day = 5, isOpen = "fridayOpens", openTime = "fridayOpenTime", closeTime = "fridayCloseTime"),
        @ClinicHoursIsValid(day = 6, isOpen = "saturdayOpens", openTime = "saturdayOpenTime", closeTime = "saturdayCloseTime")
})
public class AdvancedSearchClinicForm {

    private String clinic_name;

    private String medical_study;

    private String medical_plan;

    // for time
    private boolean sundayOpens,mondayOpens,tuesdayOpens,wednesdayOpens,thursdayOpens,fridayOpens,saturdayOpens;

    @Pattern(regexp = "(^$|(?:[01]\\d|2[0-3]):(?:[0-5]\\d))")
    @Size(max = 5)
    private String sundayOpenTime,mondayOpenTime,tuesdayOpenTime,wednesdayOpenTime,thursdayOpenTime,fridayOpenTime,saturdayOpenTime;

    @Pattern(regexp = "(^$|24:00|(?!00:00)(?:[01]\\d|2[0-3]):(?:[0-5]\\d))")
    @Size(max = 5)
    private String sundayCloseTime,mondayCloseTime,tuesdayCloseTime,wednesdayCloseTime,thursdayCloseTime,fridayCloseTime,saturdayCloseTime;


    // to Save order info
    //  should not be needed for regular searches
    private Integer medicId;
    private Integer studyId;
    private String description;
    private String patient_insurance_plan;
    private String patient_insurance_number;
    private String patientEmail;
    private String patientName;

    public AdvancedSearchClinicForm() {
        sundayOpens = mondayOpens = tuesdayOpens = wednesdayOpens = thursdayOpens = fridayOpens = saturdayOpens = false;
    }

    public void resetValues(){
        clinic_name = null;
        medical_study=null;
        medical_plan=null;

        sundayOpens = mondayOpens = tuesdayOpens = wednesdayOpens = thursdayOpens = fridayOpens = saturdayOpens = false;
        sundayOpenTime = mondayOpenTime = tuesdayOpenTime = wednesdayOpenTime = thursdayOpenTime = fridayOpenTime = saturdayOpenTime = null;
        sundayCloseTime = mondayCloseTime = tuesdayCloseTime = wednesdayCloseTime = thursdayCloseTime = fridayCloseTime = saturdayCloseTime = null;

        // keep order info intact
    }

    public void decodeForm(String uri) {

        MultiValueMap<String, String> p = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

        this.clinic_name = decodeVal(p,"clinic_name");
        this.medical_study = decodeVal(p,"medical_study");
        this.medical_plan = decodeVal(p,"medical_plan");

        this.sundayOpenTime = decodeVal(p,"sundayOpenTime");
        this.sundayCloseTime = decodeVal(p, "sundayCloseTime");
        this.mondayOpenTime = decodeVal(p,"mondayOpenTime");
        this.mondayCloseTime = decodeVal(p, "mondayCloseTime");
        this.tuesdayOpenTime = decodeVal(p,"tuesdayOpenTime");
        this.tuesdayCloseTime = decodeVal(p, "tuesdayCloseTime");
        this.wednesdayOpenTime = decodeVal(p,"wednesdayOpenTime");
        this.wednesdayCloseTime = decodeVal(p, "wednesdayCloseTime");
        this.thursdayOpenTime = decodeVal(p,"thursdayOpenTime");
        this.thursdayCloseTime = decodeVal(p, "thursdayCloseTime");
        this.fridayOpenTime = decodeVal(p,"fridayOpenTime");
        this.fridayCloseTime = decodeVal(p, "fridayCloseTime");
        this.saturdayOpenTime = decodeVal(p,"saturdayOpenTime");
        this.saturdayCloseTime = decodeVal(p, "saturdayCloseTime");

        // orderFormData
        // medicId is Integer
        // clinicId is Integer
        this.description = decodeVal(p,"description");
        this.patient_insurance_plan = decodeVal(p, "patient_insurance_plan");
        this.patient_insurance_number = decodeVal(p, "patient_insurance_number");
        this.patientEmail = decodeVal(p, "patientEmail");
        this.patientName = decodeVal(p, "patientName");
    }

    public ClinicHours getClinicHours(){
        ClinicHours ret = new ClinicHours();

        String defaultOpenTime = "00:00";
        String defaultCloseTime = "24:00";

        if(this.isSundayOpens())
            ret.setDayHour(ClinicHours.SUNDAY,
                    Time.valueOf((this.sundayOpenTime == null?defaultOpenTime:this.sundayOpenTime).concat(":00")),
                    Time.valueOf((this.sundayCloseTime == null?defaultCloseTime:this.sundayCloseTime).concat(":00")));
        if(this.isMondayOpens())
            ret.setDayHour(ClinicHours.MONDAY,
                    Time.valueOf((this.mondayOpenTime == null?defaultOpenTime:this.mondayOpenTime).concat(":00")),
                    Time.valueOf((this.mondayCloseTime== null?defaultCloseTime:this.mondayCloseTime).concat(":00")));
        if(this.isTuesdayOpens())
            ret.setDayHour(ClinicHours.TUESDAY,
                    Time.valueOf((this.tuesdayOpenTime== null?defaultOpenTime:this.tuesdayOpenTime).concat(":00")),
                    Time.valueOf((this.tuesdayCloseTime== null?defaultCloseTime:this.tuesdayCloseTime).concat(":00")));
        if(this.isWednesdayOpens())
            ret.setDayHour(ClinicHours.WEDNESDAY,
                    Time.valueOf((this.wednesdayOpenTime== null?defaultOpenTime:this.wednesdayOpenTime).concat(":00")),
                    Time.valueOf((this.wednesdayCloseTime== null?defaultCloseTime:this.wednesdayCloseTime).concat(":00")));
        if(this.isThursdayOpens())
            ret.setDayHour(ClinicHours.THURSDAY,
                    Time.valueOf((this.thursdayOpenTime== null?defaultOpenTime:this.thursdayOpenTime).concat(":00")),
                    Time.valueOf((this.thursdayCloseTime== null?defaultCloseTime:this.thursdayCloseTime).concat(":00")));
        if(this.isFridayOpens())
            ret.setDayHour(ClinicHours.FRIDAY,
                    Time.valueOf((this.fridayOpenTime== null?defaultOpenTime:this.fridayOpenTime).concat(":00")),
                    Time.valueOf((this.fridayCloseTime== null?defaultCloseTime:this.fridayCloseTime).concat(":00")));
        if(this.isSaturdayOpens())
            ret.setDayHour(ClinicHours.SATURDAY,
                    Time.valueOf((this.saturdayOpenTime== null?defaultOpenTime:this.saturdayOpenTime).concat(":00")),
                    Time.valueOf((this.saturdayCloseTime== null?defaultCloseTime:this.saturdayCloseTime).concat(":00")));

        return ret;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getMedical_study() {
        return medical_study;
    }

    public void setMedical_study(String medical_study) {
        this.medical_study = medical_study;
    }

    public String getMedical_plan() {
        return medical_plan;
    }

    public void setMedical_plan(String medical_plan) {
        this.medical_plan = medical_plan;
    }

    public boolean isSundayOpens() {
        return sundayOpens;
    }

    public void setSundayOpens(boolean sundayOpens) {
        this.sundayOpens = sundayOpens;
    }

    public boolean isMondayOpens() {
        return mondayOpens;
    }

    public void setMondayOpens(boolean mondayOpens) {
        this.mondayOpens = mondayOpens;
    }

    public boolean isTuesdayOpens() {
        return tuesdayOpens;
    }

    public void setTuesdayOpens(boolean tuesdayOpens) {
        this.tuesdayOpens = tuesdayOpens;
    }

    public boolean isWednesdayOpens() {
        return wednesdayOpens;
    }

    public void setWednesdayOpens(boolean wednesdayOpens) {
        this.wednesdayOpens = wednesdayOpens;
    }

    public boolean isThursdayOpens() {
        return thursdayOpens;
    }

    public void setThursdayOpens(boolean thursdayOpens) {
        this.thursdayOpens = thursdayOpens;
    }

    public boolean isFridayOpens() {
        return fridayOpens;
    }

    public void setFridayOpens(boolean fridayOpens) {
        this.fridayOpens = fridayOpens;
    }

    public boolean isSaturdayOpens() {
        return saturdayOpens;
    }

    public void setSaturdayOpens(boolean saturdayOpens) {
        this.saturdayOpens = saturdayOpens;
    }

    public String getSundayOpenTime() {
        return sundayOpenTime;
    }

    public void setSundayOpenTime(String sundayOpenTime) {
        this.sundayOpenTime = sundayOpenTime;
    }

    public String getMondayOpenTime() {
        return mondayOpenTime;
    }

    public void setMondayOpenTime(String mondayOpenTime) {
        this.mondayOpenTime = mondayOpenTime;
    }

    public String getTuesdayOpenTime() {
        return tuesdayOpenTime;
    }

    public void setTuesdayOpenTime(String tuesdayOpenTime) {
        this.tuesdayOpenTime = tuesdayOpenTime;
    }

    public String getWednesdayOpenTime() {
        return wednesdayOpenTime;
    }

    public void setWednesdayOpenTime(String wednesdayOpenTime) {
        this.wednesdayOpenTime = wednesdayOpenTime;
    }

    public String getThursdayOpenTime() {
        return thursdayOpenTime;
    }

    public void setThursdayOpenTime(String thursdayOpenTime) {
        this.thursdayOpenTime = thursdayOpenTime;
    }

    public String getFridayOpenTime() {
        return fridayOpenTime;
    }

    public void setFridayOpenTime(String fridayOpenTime) {
        this.fridayOpenTime = fridayOpenTime;
    }

    public String getSaturdayOpenTime() {
        return saturdayOpenTime;
    }

    public void setSaturdayOpenTime(String saturdayOpenTime) {
        this.saturdayOpenTime = saturdayOpenTime;
    }

    public String getSundayCloseTime() {
        return sundayCloseTime;
    }

    public void setSundayCloseTime(String sundayCloseTime) {
        this.sundayCloseTime = sundayCloseTime;
    }

    public String getMondayCloseTime() {
        return mondayCloseTime;
    }

    public void setMondayCloseTime(String mondayCloseTime) {
        this.mondayCloseTime = mondayCloseTime;
    }

    public String getTuesdayCloseTime() {
        return tuesdayCloseTime;
    }

    public void setTuesdayCloseTime(String tuesdayCloseTime) {
        this.tuesdayCloseTime = tuesdayCloseTime;
    }

    public String getWednesdayCloseTime() {
        return wednesdayCloseTime;
    }

    public void setWednesdayCloseTime(String wednesdayCloseTime) {
        this.wednesdayCloseTime = wednesdayCloseTime;
    }

    public String getThursdayCloseTime() {
        return thursdayCloseTime;
    }

    public void setThursdayCloseTime(String thursdayCloseTime) {
        this.thursdayCloseTime = thursdayCloseTime;
    }

    public String getFridayCloseTime() {
        return fridayCloseTime;
    }

    public void setFridayCloseTime(String fridayCloseTime) {
        this.fridayCloseTime = fridayCloseTime;
    }

    public String getSaturdayCloseTime() {
        return saturdayCloseTime;
    }

    public void setSaturdayCloseTime(String saturdayCloseTime) {
        this.saturdayCloseTime = saturdayCloseTime;
    }

    public Integer getMedicId() {
        return medicId;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatient_insurance_plan() {
        return patient_insurance_plan;
    }

    public void setPatient_insurance_plan(String patient_insurance_plan) {
        this.patient_insurance_plan = patient_insurance_plan;
    }

    public String getPatient_insurance_number() {
        return this.patient_insurance_number;
    }

    public void setPatient_insurance_number(String patient_insurance_number) {
        this.patient_insurance_number = patient_insurance_number;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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
}
