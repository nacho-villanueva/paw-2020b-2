package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.MedicPlan;
import ar.edu.itba.paw.webapp.dto.annotations.PatientPlan;
import org.hibernate.validator.constraints.NotBlank;

public class PatientPostDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.patient.v1";

    // Fields
    @NotBlank(message = "Please provide a name.")
    private String name;

    @PatientPlan
    private PatientPlanDto patientPlanInfo;

    public PatientPostDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MedicPlan getMedicPlan() {
        String planName = getMedicPlanName();
        if(planName != null) {
            return new MedicPlan(planName);
        }
        return null;
    }

    public String getMedicPlanName() {
        if (patientPlanInfo != null && patientPlanInfo.getPlan() != null) {
            return patientPlanInfo.getPlan().getName();
        }
        return null;
    }

    public String getMedicPlanNumber() {
        if (patientPlanInfo != null) {
            return patientPlanInfo.getNumber();
        }
        return null;
    }

    public PatientPlanDto getPatientPlanInfo() {
        return patientPlanInfo;
    }

    public void setPatientPlanInfo(PatientPlanDto patientPlanInfo) {
        this.patientPlanInfo = patientPlanInfo;
    }
}
