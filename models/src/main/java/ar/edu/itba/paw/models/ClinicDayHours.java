package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name="clinic_hours")
@IdClass(ClinicDayHours.ClinicDayHoursPK.class)
public class ClinicDayHours {

    /* default */static class ClinicDayHoursPK implements Serializable {
        public int clinic_id;
        public int day_of_week;

        public ClinicDayHoursPK() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClinicDayHoursPK that = (ClinicDayHoursPK) o;
            return clinic_id == that.clinic_id &&
                    day_of_week == that.day_of_week;
        }

        @Override
        public int hashCode() {

            return Objects.hash(clinic_id, day_of_week);
        }
    }

    @Id // to allow mapping
    private int clinic_id;

    @Id
    @Column(name = "day_of_week",nullable = false)
    private int day_of_week;

    @MapsId("clinic_id")
    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(name="open_time")
    private Time open_time;

    @Column(name="close_time")
    private Time close_time;

    protected ClinicDayHours() {
        //Just for hibernate
    }

    public ClinicDayHours(int day, Clinic clinic, Time open_time, Time close_time) {
        this.day_of_week = day;
        this.clinic = clinic;
        this.open_time = open_time;
        this.close_time = close_time;
    }

    public ClinicDayHours(int day_of_week, Time open_time, Time close_time) {
        this.day_of_week = day_of_week;
        this.open_time = open_time;
        this.close_time = close_time;
    }

    public int getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(int clinic_id) {
        this.clinic_id = clinic_id;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
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
