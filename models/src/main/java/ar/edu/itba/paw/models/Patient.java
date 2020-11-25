package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    private Integer userId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;
    @Column(name = "medic_plan")
    private String medicPlan;
    @Column(name = "medic_plan_number")
    private String medicPlanNumber;

    protected Patient() {
        //Just for hibernate
    }

    public Patient(final User user, final String name) {
        this.user = user;
        this.name = name;
    }

    public Patient(final User user, final String name, final String medicPlan, final String medicPlanNumber) {
        this(user,name);
        this.medicPlan = medicPlan;
        this.medicPlanNumber = medicPlanNumber;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicPlan() {
        return medicPlan;
    }

    public void setMedicPlan(String medicPlan) {
        this.medicPlan = medicPlan;
    }

    public String getMedicPlanNumber() {
        return medicPlanNumber;
    }

    public void setMedicPlanNumber(String medicPlanNumber) {
        this.medicPlanNumber = medicPlanNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
