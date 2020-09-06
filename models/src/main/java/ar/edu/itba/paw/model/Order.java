package ar.edu.itba.paw.model;

import java.sql.Date;
import java.util.Collection;

public class Order {
    private Medic medic;
    private Date date;
    private Clinic clinic;
    private String study;
    private String description;
    private byte[] identification;
    private String patient_insurance_plan;
    private String patient_insurance_number;
    private Patient patient;
    private Collection<Result> study_results;

    public Order(final Medic medic, final Date date, final Clinic clinic, final String study, final String description, final byte[] identification, final String patient_insurance_plan, final String patient_insurance_number, final Patient patient, final Collection<Result> study_results) {
        this.medic = medic;
        this.date = date;
        this.clinic = clinic;
        this.study = study;
        this.description = description;
        this.identification = identification;
        this.patient_insurance_plan = patient_insurance_plan;
        this.patient_insurance_number = patient_insurance_number;
        this.patient = patient;
        this.study_results = study_results;
    }

    public Medic getMedic() {
        return medic;
    }

    public Date getDate() {
        return date;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public String getStudy() {
        return study;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getIdentification() {
        return identification;
    }

    public String getPatient_insurance_plan() {
        return patient_insurance_plan;
    }

    public String getPatient_insurance_number() {
        return patient_insurance_number;
    }

    public Patient getPatient() {
        return patient;
    }
}
