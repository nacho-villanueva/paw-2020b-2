package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.annotations.MedicPlan;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;

public class PatientPostDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.patient.v1";

    // Fields
    @NotBlank(message = "Please provide a name.")
    private String name;

    @MedicPlan
    private MedicPlanDto medicPlanInfo;

    public PatientPostDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicPlan() {
        if (medicPlanInfo != null) {
            return medicPlanInfo.getPlan();
        }
        return null;
    }

    public String getMedicPlanNumber() {
        if (medicPlanInfo != null) {
            return medicPlanInfo.getNumber();
        }
        return null;
    }

    public MedicPlanDto getMedicPlanInfo() {
        return medicPlanInfo;
    }

    public void setMedicPlanInfo(MedicPlanDto medicPlanInfo) {
        this.medicPlanInfo = medicPlanInfo;
    }
}
