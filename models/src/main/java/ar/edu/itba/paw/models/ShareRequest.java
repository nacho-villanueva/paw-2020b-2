package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@IdClass(ShareRequestId.class)
public class ShareRequest {

    @Id
    @ManyToOne
    @JoinColumn(nullable = false)
    private Medic medic;

    @Id
    @ManyToOne
    @JoinColumn(nullable = false)
    private StudyType studyType;

    @Id
    @Column(nullable = false)
    private String patientEmail;

    public ShareRequest(Medic medic, String patientEmail, StudyType studyType) {
        this.medic = medic;
        this.studyType = studyType;
        this.patientEmail = patientEmail;
    }

    protected ShareRequest() {
        //Just for hibernate
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patient_email) {
        this.patientEmail = patient_email;
    }

    public StudyType getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyType studyType) {
        this.studyType = studyType;
    }

}


