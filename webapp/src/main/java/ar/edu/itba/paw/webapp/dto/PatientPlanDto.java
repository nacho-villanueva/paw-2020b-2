package ar.edu.itba.paw.webapp.dto;

import javax.validation.Valid;
import java.util.Objects;

public class PatientPlanDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.medic-plan.v1";

    // Variables
    @Valid
    public MedicPlanDto plan;

    public String number;

    public PatientPlanDto() {
        // Use factory methods
    }

    public PatientPlanDto(MedicPlanDto plan){
        this.plan = plan;
    }

    public PatientPlanDto(MedicPlanDto plan, String number){
        this(plan);
        this.number = number;
    }

    // Getters&Setters
    public MedicPlanDto getPlan() {
        return plan;
    }

    public void setPlan(MedicPlanDto plan) {
        this.plan = plan;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientPlanDto that = (PatientPlanDto) o;
        return Objects.equals(plan, that.plan) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plan, number);
    }
}
