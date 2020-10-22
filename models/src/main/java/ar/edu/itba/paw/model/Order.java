package ar.edu.itba.paw.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

public class Order {
    private long order_id;
    private Medic medic;
    private Date date;
    private Clinic clinic;
    private StudyType study;
    private String description;
    private String identification_type;
    private byte[] identification;
    private String patient_insurance_plan;
    private String patient_insurance_number;
    private String patient_email;
    private String patient_name;
    private Collection<Result> study_results;

    /* package */ Order() {
        //Just for hibernate
    }

    public Order(final long order_id, final Medic medic, final Date date, final Clinic clinic, final StudyType study, final String description, final String identification_type, final byte[] identification, final String patient_insurance_plan, final String patient_insurance_number, final String patient_email, final String patient_name, final Collection<Result> study_results) {
        this.order_id = order_id;
        this.medic = medic;
        this.date = date;
        this.clinic = clinic;
        this.study = study;
        this.description = description;
        this.identification_type = identification_type;
        this.identification = identification;
        this.patient_insurance_plan = patient_insurance_plan;
        this.patient_insurance_number = patient_insurance_number;
        this.patient_email = patient_email;
        this.patient_name = patient_name;
        this.study_results = study_results;
    }

    public Order(final long order_id,final Medic medic, final Date date, final Clinic clinic, final StudyType study, final String description, final String identification_type, final byte[] identification, final String patient_insurance_plan, final String patient_insurance_number, final String patient_email, final String patient_name) {
        this.order_id = order_id;
        this.medic = medic;
        this.date = date;
        this.clinic = clinic;
        this.study = study;
        this.description = description;
        this.identification_type = identification_type;
        this.identification = identification;
        this.patient_insurance_plan = patient_insurance_plan;
        this.patient_insurance_number = patient_insurance_number;
        this.patient_email = patient_email;
        this.patient_name = patient_name;
        this.study_results = new ArrayList<>();
    }

    public void setStudy_results(Collection<Result> study_results) {
        this.study_results = study_results;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdentification(byte[] identification) {
        this.identification = identification;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public void setPatient_insurance_number(String patient_insurance_number) {
        this.patient_insurance_number = patient_insurance_number;
    }

    public void setPatient_insurance_plan(String patient_insurance_plan) {
        this.patient_insurance_plan = patient_insurance_plan;
    }

    public void setStudy(StudyType study) {
        this.study = study;
    }

    public long getOrder_id() {
        return order_id;
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

    public StudyType getStudy() {
        return study;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentification_type() {
        return identification_type;
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

    public Collection<Result> getStudy_results() {
        return study_results;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }
}
