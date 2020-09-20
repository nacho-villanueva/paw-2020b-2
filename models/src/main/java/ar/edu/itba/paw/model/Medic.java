package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Medic {
    private int user_id;
    private String name;
    private String email;
    private String telephone;
    private String identification_type;
    private byte[] identification;
    private String licence_number;
    private Collection<MedicalField> medical_fields;
    private boolean verified;

    public Medic() {

    }

    public Medic(final int user_id, final String name, final String email, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified, final Collection<MedicalField> medical_fields) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.identification_type = identification_type;
        this.identification = identification;
        this.licence_number = licence_number;
        this.verified = verified;
        this.medical_fields = medical_fields;
    }

    public Medic(final int user_id, final String name, final String email, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final boolean verified) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.identification_type = identification_type;
        this.identification = identification;
        this.licence_number = licence_number;
        this.verified = verified;
        this.medical_fields = new ArrayList<>();
    }

    //Basic constructor for medics objects inside orders
    public Medic(final int user_id, final String name, final String email, final String licence_number) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.licence_number = licence_number;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setLicence_number(String licence_number) {
        this.licence_number = licence_number;
    }

    public void setMedical_fields(Collection<MedicalField> medical_fields) {
        this.medical_fields = medical_fields;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return  email;
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
