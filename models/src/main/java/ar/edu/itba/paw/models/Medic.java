package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "medics")
public class Medic {

    @Id //just to asign pk to medic
    private int user_id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name",nullable=false)
    private String name;

    @Column(name="telephone",nullable=false)
    private String telephone;

    @Column(name="identification_type",nullable=false)
    private String identification_type;

    @Column(name="identification",nullable=false)
    private byte[] identification;

    @Column(name="licence_number",nullable=false)
    private String licence_number;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="medic_medical_fields",
            joinColumns = @JoinColumn(name="medic_id"),
            inverseJoinColumns = @JoinColumn(name="field_id"))
    private Collection<MedicalField> medical_fields;

    @Column(name="verifed",nullable=false)
    private boolean verified;


    /* package */ Medic() {
        //Just for hibernate
    }

    //TODO: legacy constructors adapted for medicjdbcdao, may be removed
    public Medic(final int user_id, final String name, final String email, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified) {
        this.user = new User();
        this.user.setId(user_id);
        this.user.setEmail(email);
        this.name = name;
        this.telephone = telephone;
        this.identification_type = identification_type;
        this.identification = identification;
        this.licence_number = licence_number;
        this.verified = verified;
    }
    public Medic(final int user_id, final String name, final String email, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified, final Collection<MedicalField> medical_fields) {
        this.user = new User();
        this.user.setId(user_id);
        this.user.setEmail(email);
        this.name = name;
        this.telephone = telephone;
        this.identification_type = identification_type;
        this.identification = identification;
        this.licence_number = licence_number;
        this.verified = verified;
        this.medical_fields = medical_fields;
    }
    //Basic constructor for medics objects inside orders
    public Medic(final int user_id, final String name, final String email, final String licence_number) {
        this.user = new User();
        this.user.setId(user_id);
        this.user.setEmail(email);
        this.name = name;
        this.licence_number = licence_number;
    }
    // remove until here

    public Medic(final User user, final String name, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified, final Collection<MedicalField> medical_fields) {
        this.user = user;
        this.name = name;
        this.telephone = telephone;
        this.identification_type = identification_type;
        this.identification = identification;
        this.licence_number = licence_number;
        this.verified = verified;
        this.medical_fields = medical_fields;
    }

    public Medic(final User user, final String name, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified) {
        this.user = user;
        this.name = name;
        this.telephone = telephone;
        this.identification_type = identification_type;
        this.identification = identification;
        this.licence_number = licence_number;
        this.verified = verified;
        this.medical_fields = new ArrayList<>();
    }

    //Basic constructor for medics objects inside orders
    public Medic(final User user, final String name, final String email, final String licence_number) {
        this.user = user;
        this.name = name;
        this.licence_number = licence_number;
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
