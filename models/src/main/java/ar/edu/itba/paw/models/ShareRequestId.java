package ar.edu.itba.paw.models;

import java.io.Serializable;
import java.util.Objects;

public class ShareRequestId implements Serializable {
    private int medic;
    private int studyType;
    private String patientEmail;

    protected ShareRequestId(){
        // For hibernate.
    }

    public ShareRequestId(int medic, String patientEmail, int studyType) {
        this.medic = medic;
        this.patientEmail = patientEmail;
        this.studyType = studyType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.medic;
        hash = 83 * hash + Objects.hashCode(this.patientEmail);
        hash = 83 * hash + studyType;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ShareRequestId other = (ShareRequestId) obj;
        return medic == other.medic && patientEmail.equals(other.patientEmail) && studyType == other.studyType;
    }
}
