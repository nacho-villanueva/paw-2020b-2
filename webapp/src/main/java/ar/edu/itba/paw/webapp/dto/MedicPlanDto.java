package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

public class MedicPlanDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.medic-plan.v1";

    // Variables
    public String plan;

    public String number;

    public MedicPlanDto() {
        // Use factory methods
    }

    public MedicPlanDto(String plan){
        this.plan = plan;
    }

    public MedicPlanDto(String plan,String number){
        this(plan);
        this.number = number;
    }

    // Getters&Setters
    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
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
        MedicPlanDto that = (MedicPlanDto) o;
        return Objects.equals(plan, that.plan) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plan, number);
    }
}
