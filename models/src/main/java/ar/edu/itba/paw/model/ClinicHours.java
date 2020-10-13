package ar.edu.itba.paw.model;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean validInput(int day_of_week, Time open_time, Time close_time) {
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

    public Integer[] getDays_asIntArray(){
        List<Integer> days = new ArrayList<Integer>();
        for(int i = 0; i < getDays().length; i++){
            if(getDays()[i])
                days.add(i);
        }
        return days.toArray(new Integer[0]);
    }

    public Time[] getClose_hours() {
        return close_hours;
    }

    public String[] getClose_hours_asString(){
        return timeToStringArray(getClose_hours());
    }

    public Time[] getOpen_hours() {
        return open_hours;
    }

    public String[] getOpen_hours_asString(){

        return timeToStringArray(getOpen_hours());
    }

    private String[] timeToStringArray(Time[] time){
        String[] stime = new String[time.length];
        DateFormat format = new SimpleDateFormat("HH:mm");
        for(int i = 0; i < time.length; i++){
            if(time[i] != null){
                stime[i] = format.format(time[i].getTime());
            }
        }
        return stime;
    }

    public void setOpen_hours(String[] hours){
        for(int i = 0; i < DAYS_OF_WEEK; i++){
            if(!hours[i].equals("") && hours[i] != null)
                open_hours[i] = Time.valueOf(hours[i]+":00");
        }
    }

    public void setClose_hours(String[] hours){
        for(int i = 0; i < DAYS_OF_WEEK; i++){
            if(!hours[i].equals("") && hours[i] != null)
                close_hours[i] = Time.valueOf(hours[i]+":00");
        }
    }

    public void setDays(Integer[] daysList){
        for(Integer i : daysList)
            days[i] = true;
    }
}
