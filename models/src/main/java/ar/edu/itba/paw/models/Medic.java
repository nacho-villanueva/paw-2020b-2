package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "medics")
public class Medic {

    @Id //just to asign pk to medic
    private Integer userId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable=false)
    private String name;

    @Column
    private String telephone;

    @Column(name = "identification_type",nullable=false)
    private String identificationType;

    @Column(nullable=false, length = 32000000)  //Aprox 30Mb max file
    private byte[] identification;

    @Column(name = "licence_number",nullable=false)
    private String licenceNumber;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="medic_medical_fields",
            joinColumns = @JoinColumn(name="medic_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name="field_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"medic_id","field_id"}))
    private Collection<MedicalField> medicalFields;

    @Column(nullable=false)
    private boolean verified;


    protected Medic() {
        //Just for hibernate
    }

    public Medic(final User user, final String name, final String telephone, final String identificationType, final byte[] identification, final String licenceNumber, final boolean verified, final Collection<MedicalField> medicalFields) {
        this(user,name,telephone,identificationType,identification,licenceNumber,verified);
        this.medicalFields = medicalFields;
    }

    public Medic(final User user, final String name, final String telephone, final String identificationType, final byte[] identification, final String licenceNumber, final boolean verified) {
        this(user,name,licenceNumber);
        this.telephone = telephone;
        this.identificationType = identificationType;
        this.identification = identification;
        this.verified = verified;
        this.medicalFields = new ArrayList<>();
    }

    //Basic constructor for medics objects inside orders
    public Medic(final User user, final String name, final String licenceNumber) {
        this.user = user;
        this.name = name;
        this.licenceNumber = licenceNumber;
        this.verified = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    //legacy setter for mail
    public void setEmail(String email) {
        this.user.setEmail(email);
    }

    public void setName(String name) {
        this.name = name;
    }

    //legacy setter for id
    public void setUserId(int userId) {
        this.user.setId(userId);
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public void setMedicalFields(Collection<MedicalField> medicalFields) {
        this.medicalFields = medicalFields;
    }

    //legacy getter for id
    public int getUserId() {
        return user.getId();
    }

    public String getName() {
        return name;
    }

    //legacy getter for mail
    public String getEmail() {
        return  user.getEmail();
    }

    public String getTelephone() {
        return telephone;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public Collection<MedicalField> getMedicalFields() {
        return medicalFields;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public byte[] getIdentification() {
        return identification;
    }

    public void setIdentification(byte[] identification) {
        this.identification = identification;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
