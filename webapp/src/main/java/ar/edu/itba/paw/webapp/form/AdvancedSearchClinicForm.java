package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.webapp.form.validators.ValidAvailabilityHours;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Time;


public class AdvancedSearchClinicForm {

    private String clinic_name;

    private String medical_study;

    private String medical_plan;

    // for time
    @ValidAvailabilityHours
    private ClinicHoursForm availableTime;

    private boolean[] isAvailable;


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
        resetValues();
    }

    public void resetValues(){
        clinic_name = null;
        medical_study=null;
        medical_plan=null;

        this.availableTime = new ClinicHoursForm();
        this.isAvailable = new boolean[ClinicHours.getDaysOfWeek()];
        for (int i = 0; i < ClinicHours.getDaysOfWeek(); i++) {
            this.isAvailable[i] = true;
        }

        // keep order info intact
    }

    public void decodeForm(String uri) {

        MultiValueMap<String, String> p = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

        this.clinic_name = decodeVal(p,"clinic_name");
        this.medical_study = decodeVal(p,"medical_study");
        this.medical_plan = decodeVal(p,"medical_plan");

        String[] auxOT = new String[ClinicHours.getDaysOfWeek()];
        String[] auxCT = new String[ClinicHours.getDaysOfWeek()];

        for (int i=0; i < ClinicHours.getDaysOfWeek(); i++){
            auxOT[i] = decodeVal(p,"availableTime.opening_time["+Integer.toString(i)+"]");
            auxCT[i] = decodeVal(p,"availableTime.closing_time["+Integer.toString(i)+"]");
        }
        this.availableTime.setOpening_time(auxOT);
        this.availableTime.setClosing_time(auxCT);

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

        String[] auxOT = this.availableTime.getOpening_time();
        String[] auxCT = this.availableTime.getClosing_time();
        String defaultFromTime = "00:00";
        String defaultToTime = "23:59";
        String timeSuffix = ":00";

        for(int day=0 ; day < ClinicHours.getDaysOfWeek(); day++){
            if(this.isAvailable[day])
                ret.setDayHour(day,
                        Time.valueOf(((auxOT[day] == null)?defaultFromTime:auxOT[day]).concat(timeSuffix)),
                        Time.valueOf(((auxCT[day] == null)?defaultToTime:auxCT[day]).concat(timeSuffix))
                );
        }

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
