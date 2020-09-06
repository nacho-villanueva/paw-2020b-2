package ar.edu.itba.paw.model;

import java.util.Date;

public class Order {

    private long id;
    private Date date;
    private String patientname, medicplan, medicplannumber;
    private String ordertext;
    private Medic medic;
    private Object signature;



    public Order (final long id){
        this.id = id;
        //hardcodeo
        this.date = new Date();
        this.patientname = "Dario Solanum";
        this.medicplan="Galera Plata";
        this.medicplannumber = "60-671952-01";
        this.ordertext = "Ecografia abdominal";
        this.medic = new Medic(241351,"Dr. Cesar Aritmetica Sr.", "cearitm@mediconotfake.org", "+54 11 8461-1351");
        this.signature = null;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getPatientname() {
        return patientname;
    }

    public String getMedicplan() {
        return medicplan;
    }

    public void setMedicplan(String medicplan) {
        this.medicplan = medicplan;
    }

    public String getMedicplannumber() {
        return medicplannumber;
    }

    public void setMedicplannumber(String medicplannumber) {
        this.medicplannumber = medicplannumber;
    }

    public String getOrdertext() {
        return ordertext;
    }

    public void setOrdertext(String ordertext) {
        this.ordertext = ordertext;
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public Object getSignature() {
        return signature;
    }

    public void setSignature(Object signature) {
        this.signature = signature;
    }

}
