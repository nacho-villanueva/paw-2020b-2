package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ClinicDayHours;
import ar.edu.itba.paw.webapp.dto.annotations.TimeIntervals;
import ar.edu.itba.paw.webapp.dto.annotations.IntegerSize;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Objects;

@TimeIntervals(message="ClinicDayHoursDto.TimeIntervalsAreValid")
public class ClinicDayHoursDto {

    // Variables
    @NotNull(message="ClinicDayHoursDto.day.NotNull")
    @IntegerSize(message="ClinicDayHoursDto.day.DayIsValid",min=0,max=6)
    private Integer day;

    @NotNull(message="ClinicDayHoursDto.openTime.NotNull")
    private LocalTime openTime;

    @NotNull(message="ClinicDayHoursDto.closeTime.NotNull")
    private LocalTime closeTime;

    public ClinicDayHoursDto() {
        // Use factory method
    }

    public ClinicDayHoursDto(ClinicDayHours clinicDayHours) {
        this.day = clinicDayHours.getDayOfWeek();
        this.openTime = clinicDayHours.getOpenTime();
        this.closeTime = clinicDayHours.getCloseTime();
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicDayHoursDto that = (ClinicDayHoursDto) o;
        return Objects.equals(day, that.day) && Objects.equals(openTime, that.openTime) && Objects.equals(closeTime, that.closeTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, openTime, closeTime);
    }
}
