package ar.edu.itba.paw.persistence;

import java.sql.Time;

class DayHours {

    private int day_of_week;
    private Time open_time;
    private Time close_time;

    public DayHours(final int day_of_week, final Time open_time, final Time close_time) {
        this.day_of_week = day_of_week;
        this.open_time = open_time;
        this.close_time = close_time;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public Time getOpen_time() {
        return open_time;
    }

    public void setOpen_time(Time open_time) {
        this.open_time = open_time;
    }

    public Time getClose_time() {
        return close_time;
    }

    public void setClose_time(Time close_time) {
        this.close_time = close_time;
    }
}
