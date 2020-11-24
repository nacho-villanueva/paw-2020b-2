package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Id //just to asign pk to clinic
    private Integer userId;

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
    private Collection<StudyType> medicalStudies;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "clinic")
    private Collection<ClinicDayHours> hours;

    @ElementCollection
    @CollectionTable(name="clinic_accepted_plans", joinColumns=@JoinColumn(name="clinic_id", referencedColumnName = "user_id"))
    @Column(name="medic_plan", nullable = false)
    private Set<String> acceptedPlans;

    @Column(name="verified",nullable=false)
    private boolean verified;

    protected Clinic() {
        //Just for Hibernate
        this.verified = false;
    }

    public Clinic(final User user, final String name, final String telephone, final Collection<StudyType> medicalStudies, final ClinicHours hours, final Set<String> acceptedPlans, final boolean verified) {
        this(user,name,telephone,medicalStudies,acceptedPlans,verified);
        this.hours = hours.createClinicDayHoursCollection();
    }

    public Clinic(final User user, final String name, final String telephone, final Collection<StudyType> medicalStudies, final Set<String> acceptedPlans, final boolean verified) {
        this(user,name,telephone,verified);
        this.medicalStudies = medicalStudies;
        this.acceptedPlans = acceptedPlans;
    }


    public Clinic(final User user, final String name, final String telephone, final boolean verified) {
        this(user,name);
        this.telephone = telephone;
        this.medicalStudies = new ArrayList<>();
        this.hours = new ArrayList<>();
        this.acceptedPlans = new HashSet<>();
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

    public void setUserId(int userId) {
        this.user.setId(userId);
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

    public void setMedicalStudies(Collection<StudyType> medicalStudies) {
        this.medicalStudies = medicalStudies;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getUserId() {
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

    public Collection<StudyType> getMedicalStudies() {
        return medicalStudies;
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
        Set<Integer> newDays = new HashSet<>();
        hours.forEach(clinicDayHours -> {
            newDays.add(clinicDayHours.getDayOfWeek());
        });
        Set<ClinicDayHours> toDelete = new HashSet<>();
        Map<Integer, ClinicDayHours> toUpdate = new HashMap<>();

        //We go through the old days, and if they are on the new days as well then we gotta update, else we gotta delete them
        this.hours.forEach(clinicDayHours -> {
            if(newDays.contains(clinicDayHours.getDayOfWeek())) {
                toUpdate.put(clinicDayHours.getDayOfWeek(),clinicDayHours);
                newDays.remove(clinicDayHours.getDayOfWeek());   //Since we found out it's on both new and old days, then it's no longer a new day
            } else {
                toDelete.add(clinicDayHours);
            }
        });

        //We delete those that need to be deleted
        toDelete.forEach(clinicDayHours -> {
            this.hours.remove(clinicDayHours);
        });

        //We update those that need to be updated and add those that need to be added
        hours.forEach(newClinicDayHours -> {
            if(toUpdate.containsKey(newClinicDayHours.getDayOfWeek())) {
                ClinicDayHours oldHoursRef = toUpdate.get(newClinicDayHours.getDayOfWeek());
                oldHoursRef.setOpenTime(newClinicDayHours.getOpenTime());
                oldHoursRef.setCloseTime(newClinicDayHours.getCloseTime());
            } else {
                newClinicDayHours.setClinicId(this.user.getId());
                this.hours.add(newClinicDayHours);
            }
        });
    }

    public Set<String> getAcceptedPlans() {
        return acceptedPlans;
    }

    public void setAcceptedPlans(Set<String> acceptedPlans) {
        this.acceptedPlans = acceptedPlans;
    }
}
