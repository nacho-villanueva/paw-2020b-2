package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.PatientInfoFormHasNotNullName;
import ar.edu.itba.paw.webapp.form.validators.PatientInfoFormHasValidEmail;
import ar.edu.itba.paw.webapp.form.validators.PatientInfoFormHasValidPatient;
import ar.edu.itba.paw.webapp.form.validators.ValidStudyTypeId;

public class OrderWithoutClinicForm {

    private Integer medicId;

    @ValidStudyTypeId
    private Integer studyId;

    private String description;

    @PatientInfoFormHasValidEmail
    @PatientInfoFormHasValidPatient
    @PatientInfoFormHasNotNullName
    private PatientInfoForm patientInfo;

    public OrderWithoutClinicForm(Integer medicId, Integer studyId, String description,PatientInfoForm patientInfoForm) {
        this.medicId = medicId;
        this.studyId = studyId;
        this.description = description;
        this.patientInfo = patientInfoForm;
    }

    public OrderWithoutClinicForm(){
        this.patientInfo = new PatientInfoForm();
    }

    public Integer getMedicId() {
        return medicId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public String getDescription() {
        return description;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PatientInfoForm getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoForm patientInfo) {
        this.patientInfo = patientInfo;
    }
}
