package ar.edu.itba.paw.webapp.form;

public class ClinicHoursForm {
    String[] opening_time;
    String[] closing_time;

    public ClinicHoursForm() {
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
