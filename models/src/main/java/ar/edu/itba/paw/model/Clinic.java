package ar.edu.itba.paw.model;

import java.util.*;

public class Clinic {
    private int user_id;
    private String name;
    private String email;
    private String telephone;
    private Collection<StudyType> medical_studies;
    private ClinicHours hours;
    private Set<String> accepted_plans;
    private boolean verified;

    /* package */ Clinic() {
        //Just for Hibernate
    }

    public Clinic(final int user_id, final String name, final String email, final String telephone, final Collection<StudyType> medical_studies, final ClinicHours hours, final Set<String> accepted_plans, final boolean verified) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.medical_studies = medical_studies;
        this.hours = hours;
        this.accepted_plans = accepted_plans;
        this.verified = verified;
    }

    public Clinic(final int user_id, final String name, final String email, final String telephone, final boolean verified) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.medical_studies = new ArrayList<>();
        this.hours = new ClinicHours();
        this.accepted_plans = new HashSet<>();
        this.verified = verified;
    }

    //Basic constructor for clinic objects inside orders
    public Clinic(final int user_id, final String name, final String email) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setMedical_studies(Collection<StudyType> medical_studies) {
        this.medical_studies = medical_studies;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Collection<StudyType> getMedical_studies() {
        return medical_studies;
    }

    public boolean isVerified() {
        return verified;
    }

    public ClinicHours getHours() {
        return hours;
    }

    public void setHours(ClinicHours hours) {
        this.hours = hours;
    }

    public Set<String> getAccepted_plans() {
        return accepted_plans;
    }

    public void setAccepted_plans(Set<String> accepted_plans) {
        this.accepted_plans = accepted_plans;
    }
}
