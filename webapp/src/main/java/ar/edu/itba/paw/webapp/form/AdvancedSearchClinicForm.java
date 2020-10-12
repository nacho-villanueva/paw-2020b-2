package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AdvancedSearchClinicForm {

    private String clinic_name;

    private String medical_study;

    private String medical_plan;

    // for time
    private boolean sundayOpens,mondayOpens,tuesdayOpens,wednesdayOpens,thursdayOpens,fridayOpens,saturdayOpens;

    @Pattern(regexp = "(^$|(?:[01]\\d|2[0-3]):(?:[0-5]\\d):(?:[0-5]\\d))")
    @Size(max = 8)
    private String sundayOpenTime,mondayOpenTime,tuesdayOpenTime,wednesdayOpenTime,thursdayOpenTime,fridayOpenTime,saturdayOpenTime;

    @Pattern(regexp = "(^$|24:00:00|(?!00:00:00)(?:[01]\\d|2[0-3]):(?:[0-5]\\d):(?:[0-5]\\d))")
    @Size(max = 8)
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
}
