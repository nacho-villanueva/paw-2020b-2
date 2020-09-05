package ar.edu.itba.paw.model;

public class Medic {
    private long mn;
    private String fullname, email, phonenumber;

    public Medic(final long mn, final String fullname, final String email, final String phonenumber) {
        this.mn = mn;
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public long getLicenceNumber(){
        return mn;
    }
    public String getFullname(){
        return fullname;
    }
}
