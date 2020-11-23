package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ClinicIdIsValid;

public class OrderForm extends  OrderWithoutClinicForm{

    @ClinicIdIsValid
    private Integer clinicId;

    public OrderForm(){ }

    public OrderForm(Integer medicId, Integer studyId, String description, PatientInfoForm patientInfoForm, Integer clinicId) {
        super(medicId, studyId, description, patientInfoForm);
        this.clinicId = clinicId;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }
}
