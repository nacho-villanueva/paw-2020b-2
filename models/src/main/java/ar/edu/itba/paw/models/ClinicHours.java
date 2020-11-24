package ar.edu.itba.paw.models;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ClinicHours {
    private static final int DAYS_OF_WEEK = 7;
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    private static final String HOUR_REGEX = "^((?:2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]|24:00:00)$";
    private final Pattern HOUR_PATTERN = Pattern.compile(HOUR_REGEX);

    private final boolean[] days = new boolean[DAYS_OF_WEEK];
    private final Time[] openHours = new Time[DAYS_OF_WEEK];
    private final Time[] closeHours = new Time[DAYS_OF_WEEK];

    //Default constructor needed for hibernate, if functionality changes DO NOT delete constructor, just change from public to /* package */
    public ClinicHours() {
        for (int i = 0; i < DAYS_OF_WEEK; i++) {
            this.days[i] = false;
        }
    }

    public ClinicHours(Collection<ClinicDayHours> clinicDayHoursCollection){

        for (ClinicDayHours cdh: clinicDayHoursCollection) {
            if(cdh.getDayOfWeek()<DAYS_OF_WEEK){
                setDayHour(cdh.getDayOfWeek(),cdh.getOpenTime(),cdh.getCloseTime());
            }
        }
    }

    public Collection<ClinicDayHours> createClinicDayHoursCollection(){
        Collection<ClinicDayHours> clinicDayHoursCollection = new ArrayList<>();

        for(int i = 0; i < DAYS_OF_WEEK; i++){
            if(days[i])
                clinicDayHoursCollection.add(new ClinicDayHours(i,openHours[i],closeHours[i]));
        }

        return clinicDayHoursCollection;
    }

    public Collection<ClinicDayHours> createClinicDayHoursCollection(int clinicId){
        Collection<ClinicDayHours> clinicDayHoursCollection = new ArrayList<>();

        for(int i = 0; i < DAYS_OF_WEEK; i++){
            if(days[i])
                clinicDayHoursCollection.add(new ClinicDayHours(i,clinicId,openHours[i],closeHours[i]));
        }

        return clinicDayHoursCollection;
    }

    public void setDayHour(final int dayOfWeek, final Time openTime, final Time closeTime) {
        if(validInput(dayOfWeek,openTime,closeTime)) {
            this.days[dayOfWeek] = true;
            this.openHours[dayOfWeek] = openTime;
            this.closeHours[dayOfWeek] = closeTime;
        }
    }

    public static boolean validInput(int dayOfWeek, Time openTime, Time closeTime) {
        if (openTime == null || closeTime == null) {
            return false;
        }
        switch (dayOfWeek) {
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

    public Integer[] getDaysAsIntArray(){
        List<Integer> days = new ArrayList<Integer>();
        for(int i = 0; i < getDays().length; i++){
            if(getDays()[i])
                days.add(i);
        }
        return days.toArray(new Integer[0]);
    }

    public Time[] getCloseHours() {
        return closeHours;
    }

    public String[] getCloseHoursAsString(){
        return timeToStringArray(getCloseHours());
    }

    public Time[] getOpenHours() {
        return openHours;
    }

    public String[] getOpenHoursAsString(){

        return timeToStringArray(getOpenHours());
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

    public void setDaysHours(Set<Integer> daysSet, String[] openHours, String[] closeHours) {
        if(validInput(daysSet,openHours,closeHours)) {
            this.setDays(daysSet);
            this.setOpenHours(openHours);
            this.setCloseHours(closeHours);
        }
    }

    private boolean validInput(Set<Integer> daysSet, String[] openHours, String[] closeHours) {
        boolean ret = true;
        for(Integer i: daysSet) {
            switch (i) {
                case SUNDAY:
                case MONDAY:
                case TUESDAY:
                case WEDNESDAY:
                case THURSDAY:
                case FRIDAY:
                case SATURDAY:
                    break;
                default:
                    ret = false;
            }

            if(openHours[i] == null || !HOUR_PATTERN.matcher(openHours[i]+":00").matches()) {
                ret = false;
            }

            if(closeHours[i] == null || !HOUR_PATTERN.matcher(closeHours[i]+":00").matches()) {
                ret = false;
            }
        }

        return ret;
    }

    private void setOpenHours(String[] hours){
        for(int i = 0; i < DAYS_OF_WEEK; i++){
            if(!hours[i].equals("") && hours[i] != null)
                openHours[i] = Time.valueOf(hours[i]+":00");
        }
    }

    private void setCloseHours(String[] hours){
        for(int i = 0; i < DAYS_OF_WEEK; i++){
            if(!hours[i].equals("") && hours[i] != null)
                closeHours[i] = Time.valueOf(hours[i]+":00");
        }
    }

    private void setDays(Set<Integer> daysSet){
        for(Integer i : daysSet)
            days[i] = true;
    }

    public static int getDaysOfWeek() {
        return DAYS_OF_WEEK;
    }
}
