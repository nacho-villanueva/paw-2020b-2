package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.webapp.form.validators.ValidDays;

public class ClinicHoursForm {

    @ValidDays
    Integer[] openDays;

    String[] openingTime;

    String[] closingTime;

    public ClinicHoursForm() {
        this.openingTime = new String[ClinicHours.getDaysOfWeek()];
        this.closingTime = new String[ClinicHours.getDaysOfWeek()];
    }


    public String[] getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String[] openingTime) {
        this.openingTime = openingTime;
    }

    public String[] getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String[] closingTime) {
        this.closingTime = closingTime;
    }

    public Integer[] getOpenDays() {
        return openDays;
    }

    public void setOpenDays(Integer[] openDays) {
        this.openDays = openDays;
    }
}
