package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.webapp.form.validators.ValidDays;

public class ClinicHoursForm {

    @ValidDays
    Integer[] open_days;

    String[] opening_time;

    String[] closing_time;

    public ClinicHoursForm() {
        this.opening_time = new String[ClinicHours.getDaysOfWeek()];
        this.closing_time = new String[ClinicHours.getDaysOfWeek()];
    }


    public String[] getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String[] opening_time) {
        this.opening_time = opening_time;
    }

    public String[] getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String[] closing_time) {
        this.closing_time = closing_time;
    }

    public Integer[] getOpen_days() {
        return open_days;
    }

    public void setOpen_days(Integer[] open_days) {
        this.open_days = open_days;
    }
}
