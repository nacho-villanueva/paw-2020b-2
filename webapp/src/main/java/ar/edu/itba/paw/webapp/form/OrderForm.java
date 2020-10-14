package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ValidClinicId;

public class OrderForm extends  OrderWithoutClinicForm{

    @ValidClinicId
    private Integer clinicId;

    public OrderForm(){ }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }
}
