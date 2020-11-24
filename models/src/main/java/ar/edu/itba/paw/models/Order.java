package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "medical_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "medical_orders_id_seq")
    @SequenceGenerator(sequenceName = "medical_orders_id_seq", name = "medical_orders_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "medic_id", nullable = false, referencedColumnName = "user_id")
    private Medic medic;

    @Column(name = "date", nullable = false)
    private LocalDate date;

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
    private String patientInsurancePlan;

    @Column(name = "medic_plan_number")
    private String patientInsuranceNumber;

    @Column(name="patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @OneToMany(mappedBy = "order")
    private Collection<Result> studyResults;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> sharedWith;

    protected Order() {
        //Just for hibernate
    }

    public Order(final Medic medic,
                 final LocalDate date,
                 final Clinic clinic,
                 final StudyType study,
                 final String description,
                 final String identificationType,
                 final byte[] identification,
                 final String patientInsurancePlan,
                 final String patientInsuranceNumber,
                 final String patientEmail,
                 final String patientName) {
        this.medic = medic;
        this.date = date;
        this.clinic = clinic;
        this.study = study;
        this.description = description;
        this.identificationType = identificationType;
        this.identification = identification;
        this.patientInsurancePlan = patientInsurancePlan;
        this.patientInsuranceNumber = patientInsuranceNumber;
        this.patientEmail = patientEmail;
        this.patientName = patientName;
    }

    public Order(final long orderId, final Medic medic, final LocalDate date, final Clinic clinic, final StudyType study, final String description, final String identificationType, final byte[] identification, final String patientInsurancePlan, final String patientInsuranceNumber, final String patientEmail, final String patientName) {
        this.orderId = orderId;
        this.medic = medic;
        this.date = date;
        this.clinic = clinic;
        this.study = study;
        this.description = description;
        this.identificationType = identificationType;
        this.identification = identification;
        this.patientInsurancePlan = patientInsurancePlan;
        this.patientInsuranceNumber = patientInsuranceNumber;
        this.patientEmail = patientEmail;
        this.patientName = patientName;
        this.studyResults = new ArrayList<>();
    }

    public void setStudyResults(Collection<Result> studyResults) {
        this.studyResults = studyResults;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public void setDate(LocalDate date) {
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

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setPatientInsuranceNumber(String patientInsuranceNumber) {
        this.patientInsuranceNumber = patientInsuranceNumber;
    }

    public void setPatientInsurancePlan(String patientInsurancePlan) {
        this.patientInsurancePlan = patientInsurancePlan;
    }

    public void setStudy(StudyType study) {
        this.study = study;
    }

    public long getOrderId() {
        return orderId;
    }

    public Medic getMedic() {
        return medic;
    }

    public LocalDate getDate() {
        return date;
    }

    public Date getLegacyDate(){
        return Date.from(getDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
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

    public String getPatientInsurancePlan() {
        return patientInsurancePlan;
    }

    public String getPatientInsuranceNumber() {
        return patientInsuranceNumber;
    }

    public Collection<Result> getStudyResults() {
        return studyResults;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Set<User> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(Set<User> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public void addToSharedWith(User user){
        this.sharedWith.add(user);
    }
}
