package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.webapp.dto.annotations.ArrayAsString;
import ar.edu.itba.paw.webapp.dto.annotations.BooleanArrayAsString;
import ar.edu.itba.paw.webapp.dto.annotations.TimeArrayAsString;
import ar.edu.itba.paw.webapp.dto.annotations.TimeIntervals;

import javax.ws.rs.QueryParam;
import java.time.LocalTime;

@TimeIntervals(message = "ClinicHoursAvailabilityDto.TimeIntervalsAreValid")
public class ClinicHoursAvailabilityDto {

    // Constants
    private static final int MAX_SIZE = 7;

    // Variables
    @QueryParam("days")
    @ArrayAsString(max=MAX_SIZE,message = "ClinicHoursAvailabilityDto.openDays.ArrayAsStringIsValid")
    @BooleanArrayAsString(message = "ClinicHoursAvailabilityDto.openDays.BooleanArrayAsStringIsValid")
    String days;

    @QueryParam("from-time")
    @ArrayAsString(max=MAX_SIZE,message = "ClinicHoursAvailabilityDto.fromTime.ArrayAsStringIsValid")
    @TimeArrayAsString(message = "ClinicHoursAvailabilityDto.fromTime.TimeArrayAsStringIsValid")
    String fromTime;

    @QueryParam("to-time")
    @ArrayAsString(max=MAX_SIZE,message = "ClinicHoursAvailabilityDto.toTime.ArrayAsStringIsValid")
    @TimeArrayAsString(message = "ClinicHoursAvailabilityDto.toTime.TimeArrayAsStringIsValid")
    String toTime;

    // Constructors
    public ClinicHoursAvailabilityDto() {
    }

    // Getters&Setters


    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    // etc.
    public boolean[] getOpenDays(){

        if(this.days ==null || this.days.trim().length()==0)
            return null;

        String[] openDays = this.days.split(",");
        boolean[] ret = new boolean[MAX_SIZE];
        for(int i = 0; i<openDays.length; i++){
            ret[i] = isTrue(openDays[i]);
        }
        return ret;
    }

    public LocalTime[] getFromTimeAsLocalTime(){
        return getTime(this.fromTime,"00:00");
    }

    public LocalTime[] getToTimeAsLocalTime(){
        return getTime(this.toTime,"23:59");
    }

    private LocalTime[] getTime(String time,String defaultValue){

        LocalTime[] ret = new LocalTime[MAX_SIZE];
        if(time==null || time.trim().length()==0){
            for(int i = 0; i<MAX_SIZE; i++)
                ret[i] = LocalTime.parse(defaultValue);
            return ret;
        }

        String[] timeArray = time.split(",");
        for(int i = 0; i<MAX_SIZE; i++){
            if(i>=timeArray.length || timeArray[i].equals(""))
                ret[i] = LocalTime.parse(defaultValue);
            else{
                try {
                    ret[i] = LocalTime.parse(timeArray[i]);
                }catch (Exception e){
                    ret[i] = LocalTime.parse(defaultValue);
                }
            }
        }
        return ret;
    }

    private boolean isTrue(String string){

        if(string == null || string.trim().length()==0)
            return false;

        String s = string.trim().toLowerCase();
        return s.equals("true") || s.equals("1");
    }

    public ClinicHours getClinicHours(){

        boolean[] openDays = getOpenDays();
        LocalTime[] fromTime = getFromTimeAsLocalTime();
        LocalTime[] toTime = getToTimeAsLocalTime();

        if(openDays == null)
            return null;

        ClinicHours clinicHours = new ClinicHours();
        for(int i =0; i<MAX_SIZE;i++){
            if(openDays[i]){
                clinicHours.setDayHour(i,fromTime[i],toTime[i]);
            }
        }

        return clinicHours;
    }
}
