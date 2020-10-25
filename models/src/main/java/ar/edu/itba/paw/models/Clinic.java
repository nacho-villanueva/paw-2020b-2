package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Id //just to asign pk to clinic
    private int user_id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name",nullable=false)
    private String name;

    @Column(name="telephone",nullable=false)
    private String telephone;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="clinic_available_studies",
            joinColumns = @JoinColumn(name="clinic_id"),
            inverseJoinColumns = @JoinColumn(name="study_id"))
    private Collection<StudyType> medical_studies;

    //TODO map clinic hours
    private ClinicHours hours;

    @ElementCollection
    @CollectionTable(name="clinic_accepted_plans", joinColumns=@JoinColumn(name="clinic_id"))
    private Set<String> accepted_plans;

    @Column(name="verified",nullable=false)
    private boolean verified;

    /* package */ Clinic() {
        //Just for Hibernate
    }

    //TODO: legacy constructors adapted for clinicjdbcdao, may be removed
    public Clinic(final int user_id, final String name, final String email, final String telephone, final Collection<StudyType> medical_studies, final ClinicHours hours, final Set<String> accepted_plans, final boolean verified) {
        this.user = new User();
        this.user.setId(user_id);
        this.user.setEmail(email);
        this.name = name;
        this.telephone = telephone;
        this.medical_studies = medical_studies;
        this.hours = hours;
        this.accepted_plans = accepted_plans;
        this.verified = verified;
    }

    public Clinic(final int user_id, final String name, final String email, final String telephone, final boolean verified) {
        this.user = new User();
        this.user.setId(user_id);
        this.user.setEmail(email);
        this.name = name;
        this.telephone = telephone;
        this.medical_studies = new ArrayList<>();
        this.hours = new ClinicHours();
        this.accepted_plans = new HashSet<>();
        this.verified = verified;
    }

    //Basic constructor for clinic objects inside orders
    public Clinic(final int user_id, final String name, final String email) {
        this.user = new User();
        this.user.setId(user_id);
        this.user.setEmail(email);
        this.name = name;
    }
    // remove until here

    public Clinic(final User user, final String name, final String telephone, final Collection<StudyType> medical_studies, final ClinicHours hours, final Set<String> accepted_plans, final boolean verified) {
        this.user = user;
        this.name = name;
        this.telephone = telephone;
        this.medical_studies = medical_studies;
        this.hours = hours;
        this.accepted_plans = accepted_plans;
        this.verified = verified;
    }

    public Clinic(final User user, final String name, final String telephone, final boolean verified) {
        this.user = user;
        this.name = name;
        this.telephone = telephone;
        this.medical_studies = new ArrayList<>();
        this.hours = new ClinicHours();
        this.accepted_plans = new HashSet<>();
        this.verified = verified;
    }

    //Basic constructor for clinic objects inside orders
    public Clinic(final User user, final String name) {
        this.user = user;
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUser_id(int user_id) {
        this.user.setId(user_id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.user.setEmail(email);
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
        return user.getId();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return user.getEmail();
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
