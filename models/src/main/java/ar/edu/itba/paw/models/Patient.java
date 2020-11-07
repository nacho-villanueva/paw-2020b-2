package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    private Integer user_id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;
    @Column
    private String medic_plan;
    @Column
    private String medic_plan_number;

    protected Patient() {
        //Just for hibernate
    }

    public Patient(final User user, final String name) {
        this.user = user;
        this.name = name;
    }

    public Patient(final User user, final String name, final String medic_plan, final String medic_plan_number) {
        this(user,name);
        this.medic_plan = medic_plan;
        this.medic_plan_number = medic_plan_number;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedic_plan() {
        return medic_plan;
    }

    public void setMedic_plan(String medic_plan) {
        this.medic_plan = medic_plan;
    }

    public String getMedic_plan_number() {
        return medic_plan_number;
    }

    public void setMedic_plan_number(String medic_plan_number) {
        this.medic_plan_number = medic_plan_number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
