package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.ClinicHours;

public class ClinicHoursForm {
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
}
