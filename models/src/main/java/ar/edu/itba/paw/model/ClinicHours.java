package ar.edu.itba.paw.model;

import java.sql.Time;

public class ClinicHours {
    private static final int DAYS_OF_WEEK = 7;
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    private final boolean[] days = new boolean[DAYS_OF_WEEK];
    private final Time[] open_hours = new Time[DAYS_OF_WEEK];
    private final Time[] close_hours = new Time[DAYS_OF_WEEK];

    public ClinicHours() {
        for (int i = 0; i < DAYS_OF_WEEK; i++) {
            this.days[i] = false;
        }
    }

    public void setDayHour(final int day_of_week, final Time open_time, final Time close_time) {
        if(validInput(day_of_week,open_time,close_time)) {
            this.days[day_of_week] = true;
            this.open_hours[day_of_week] = open_time;
            this.close_hours[day_of_week] = close_time;
        }
    }

    private boolean validInput(int day_of_week, Time open_time, Time close_time) {
        if (open_time == null || close_time == null) {
            return false;
        }
        switch (day_of_week) {
            case SUNDAY:
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
            case SATURDAY:
                return true;
            default:
        }
        return false;
    }

    public boolean[] getDays() {
        return days;
    }

    public Time[] getClose_hours() {
        return close_hours;
    }

    public Time[] getOpen_hours() {
        return open_hours;
    }
}
