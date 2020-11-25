package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.MedicIdIsValid;
import ar.edu.itba.paw.webapp.form.validators.PatientMailIsValid;
import ar.edu.itba.paw.webapp.form.validators.ValidStudyTypeId;

public class RequestOrdersForm {

    @MedicIdIsValid
    private Integer medicId;

    @PatientMailIsValid
    private String patientEmail;

    @ValidStudyTypeId
    private Integer studyTypeId;

    public RequestOrdersForm() {
    }

    public Integer getMedicId() {
        return medicId;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientMail) {
        this.patientEmail = patientMail;
    }

    public Integer getStudyTypeId() {
        return studyTypeId;
    }

    public void setStudyTypeId(Integer studyTypeId) {
        this.studyTypeId = studyTypeId;
    }
}
