package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ClinicIdIsValid;

public class ChangeOrderClinicForm {

    @ClinicIdIsValid
    private int changeClinicId;

    public int getChangeClinicId() {
        return changeClinicId;
    }

    public void setChangeClinicId(int changeClinicId) {
        this.changeClinicId = changeClinicId;
    }
}
