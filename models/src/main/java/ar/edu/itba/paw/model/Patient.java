package ar.edu.itba.paw.model;

public class Patient {
    private int user_id;
    private String email;
    private String name;
    private String medic_plan;
    private String medic_plan_number;

    /* package */ Patient() {
        //Just for hibernate
    }

    public Patient(final int user_id, final String email, final String name, final String medic_plan, final String medic_plan_number) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.medic_plan = medic_plan;
        this.medic_plan_number = medic_plan_number;
    }

    public Patient(final int user_id, final String email, final String name) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() { return name; }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
}
