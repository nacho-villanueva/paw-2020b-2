package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "medical_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "medical_orders_id_seq")
    @SequenceGenerator(sequenceName = "medical_orders_id_seq", name = "medical_orders_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long order_id;

    @ManyToOne
    @JoinColumn(name = "medic_id", nullable = false, referencedColumnName = "user_id")
    private Medic medic;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false, referencedColumnName = "user_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private StudyType study;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "identification_type", nullable = false)
    private String identificationType;

    @Column(name = "identification", nullable = false, length = 32000000)  //Aprox 30Mb max file
    private byte[] identification;

    @Column(name="medic_plan")
    private String patient_insurance_plan;

    @Column(name = "medic_plan_number")
    private String patient_insurance_number;

    @Column(name="patient_email", nullable = false)
    private String patient_email;

    @Column(name = "patient_name", nullable = false)
    private String patient_name;

    @OneToMany(mappedBy = "order")
    private Collection<Result> study_results;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> shared_with;

    protected Order() {
        //Just for hibernate
    }

    public Order(final Medic medic,
                 final Date date,
                 final Clinic clinic,
                 final StudyType study,
                 final String description,
                 final String identificationType,
                 final byte[] identification,
                 final String patient_insurance_plan,
                 final String patient_insurance_number,
                 final String patient_email,
                 final String patient_name) {
        this.medic = medic;
        this.date = date;
        this.clinic = clinic;
        this.study = study;
        this.description = description;
        this.identificationType = identificationType;
        this.identification = identification;
        this.patient_insurance_plan = patient_insurance_plan;
        this.patient_insurance_number = patient_insurance_number;
        this.patient_email = patient_email;
        this.patient_name = patient_name;
    }

    public Order(final long order_id, final Medic medic, final Date date, final Clinic clinic, final StudyType study, final String description, final String identificationType, final byte[] identification, final String patient_insurance_plan, final String patient_insurance_number, final String patient_email, final String patient_name) {
        this.order_id = order_id;
        this.medic = medic;
        this.date = date;
        this.clinic = clinic;
        this.study = study;
        this.description = description;
        this.identificationType = identificationType;
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

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
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

    public String getIdentificationType() {
        return identificationType;
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

    public Set<User> getShared_with() {
        return shared_with;
    }

    public void setShared_with(Set<User> shared_with) {
        this.shared_with = shared_with;
    }

    public void addToShared_with(User user){
        this.shared_with.add(user);
    }
}
