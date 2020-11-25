package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ValidClinicId;

public class ChangeOrderClinicForm {

    @ValidClinicId
    private int changeClinicId;

    public int getChangeClinicId() {
        return changeClinicId;
    }

    public void setChangeClinicId(int changeClinicId) {
        this.changeClinicId = changeClinicId;
    }
}
