package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

public class MedicPlanDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.medic-plan.v1";

    // Variables
    @NotBlank(message = "MedicPlanDto.plan.NotBlank")
    public String plan;

    public MedicPlanDto() {
        // Use factory methods
    }

    public MedicPlanDto(String plan){
        this.plan = plan;
    }

    // Getters&Setters
    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicPlanDto that = (MedicPlanDto) o;
        return Objects.equals(plan, that.plan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plan);
    }
}
