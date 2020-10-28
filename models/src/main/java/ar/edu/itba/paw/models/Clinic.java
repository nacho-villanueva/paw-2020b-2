package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Id //just to asign pk to clinic
    private Integer user_id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "name",nullable=false)
    private String name;

    @Column(name="telephone")
    private String telephone;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="clinic_available_studies",
            joinColumns = @JoinColumn(name="clinic_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name="study_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"clinic_id","study_id"}))
    private Collection<StudyType> medical_studies;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "clinic")
    private Collection<ClinicDayHours> hours;

    @ElementCollection
    @CollectionTable(name="clinic_accepted_plans", joinColumns=@JoinColumn(name="clinic_id", referencedColumnName = "user_id"))
    @Column(name="medic_plan", nullable = false)
    private Set<String> accepted_plans;

    @Column(name="verified",nullable=false)
    private boolean verified;

    protected Clinic() {
        //Just for Hibernate
        this.verified = false;
    }

    public Clinic(final User user, final String name, final String telephone, final Collection<StudyType> medical_studies, final ClinicHours hours, final Set<String> accepted_plans, final boolean verified) {
        this(user,name,telephone,medical_studies,accepted_plans,verified);
        this.hours = hours.createClinicDayHoursCollection();
    }

    public Clinic(final User user, final String name, final String telephone, final Collection<StudyType> medical_studies, final Set<String> accepted_plans, final boolean verified) {
        this(user,name,telephone,verified);
        this.medical_studies = medical_studies;
        this.accepted_plans = accepted_plans;
    }


    public Clinic(final User user, final String name, final String telephone, final boolean verified) {
        this(user,name);
        this.telephone = telephone;
        this.medical_studies = new ArrayList<>();
        this.hours = new ArrayList<>();
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
        return new ClinicHours(hours);
    }

    public void setHours(ClinicHours hours) {
        this.setHours(hours.createClinicDayHoursCollection(this.user.getId()));
    }

    public void setHours(Collection<ClinicDayHours> hours) {
        //First we check which entries need to be removed, then we add the new ones and update the ones that got modified

        //We will find entries to be removed by the day of the week
        Set<Integer> new_days = new HashSet<>();
        hours.forEach(clinicDayHours -> {
            new_days.add(clinicDayHours.getDay_of_week());
        });
        Set<ClinicDayHours> to_delete = new HashSet<>();
        Map<Integer, ClinicDayHours> to_update = new HashMap<>();

        //We go through the old days, and if they are on the new days as well then we gotta update, else we gotta delete them
        this.hours.forEach(clinicDayHours -> {
            if(new_days.contains(clinicDayHours.getDay_of_week())) {
                to_update.put(clinicDayHours.getDay_of_week(),clinicDayHours);
                new_days.remove(clinicDayHours.getDay_of_week());   //Since we found out it's on both new and old days, then it's no longer a new day
            } else {
                to_delete.add(clinicDayHours);
            }
        });

        //We delete those that need to be deleted
        to_delete.forEach(clinicDayHours -> {
            this.hours.remove(clinicDayHours);
        });

        //We update those that need to be updated and add those that need to be added
        hours.forEach(newClinicDayHours -> {
            if(to_update.containsKey(newClinicDayHours.getDay_of_week())) {
                ClinicDayHours oldHoursRef = to_update.get(newClinicDayHours.getDay_of_week());
                oldHoursRef.setOpen_time(newClinicDayHours.getOpen_time());
                oldHoursRef.setClose_time(newClinicDayHours.getClose_time());
            } else {
                newClinicDayHours.setClinic_id(this.user.getId());
                this.hours.add(newClinicDayHours);
            }
        });
    }

    public Set<String> getAccepted_plans() {
        return accepted_plans;
    }

    public void setAccepted_plans(Set<String> accepted_plans) {
        this.accepted_plans = accepted_plans;
    }
}
