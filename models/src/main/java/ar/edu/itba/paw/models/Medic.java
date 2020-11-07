package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "medics")
public class Medic {

    @Id //just to asign pk to medic
    private Integer user_id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable=false)
    private String name;

    @Column
    private String telephone;

    @Column(nullable=false)
    private String identification_type;

    @Column(nullable=false, length = 32000000)  //Aprox 30Mb max file
    private byte[] identification;

    @Column(nullable=false)
    private String licence_number;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="medic_medical_fields",
            joinColumns = @JoinColumn(name="medic_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name="field_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"medic_id","field_id"}))
    private Collection<MedicalField> medical_fields;

    @Column(nullable=false)
    private boolean verified;


    protected Medic() {
        //Just for hibernate
    }

    public Medic(final User user, final String name, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified, final Collection<MedicalField> medical_fields) {
        this(user,name,telephone,identification_type,identification,licence_number,verified);
        this.medical_fields = medical_fields;
    }

    public Medic(final User user, final String name, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified) {
        this(user,name,licence_number);
        this.telephone = telephone;
        this.identification_type = identification_type;
        this.identification = identification;
        this.verified = verified;
        this.medical_fields = new ArrayList<>();
    }

    //Basic constructor for medics objects inside orders
    public Medic(final User user, final String name, final String licence_number) {
        this.user = user;
        this.name = name;
        this.licence_number = licence_number;
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
    public void setUser_id(int user_id) {
        this.user.setId(user_id);
    }

    public void setLicence_number(String licence_number) {
        this.licence_number = licence_number;
    }

    public void setMedical_fields(Collection<MedicalField> medical_fields) {
        this.medical_fields = medical_fields;
    }

    //legacy getter for id
    public int getUser_id() {
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

    public String getLicence_number() {
        return licence_number;
    }

    public Collection<MedicalField> getMedical_fields() {
        return medical_fields;
    }

    public String getIdentification_type() {
        return identification_type;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
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
