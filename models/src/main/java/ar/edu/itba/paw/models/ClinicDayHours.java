package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name="clinic_hours")
@IdClass(ClinicDayHours.ClinicDayHoursPK.class)
public class ClinicDayHours {

    public static class ClinicDayHoursPK implements Serializable {
        private int clinicId;
        private int day_of_week;

        public ClinicDayHoursPK() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClinicDayHoursPK that = (ClinicDayHoursPK) o;
            return clinicId == that.clinicId &&
                    day_of_week == that.day_of_week;
        }

        @Override
        public int hashCode() {

            return Objects.hash(clinicId, day_of_week);
        }

        public void setClinicId(int clinicId) {
            this.clinicId = clinicId;
        }

        public void setDay_of_week(int day_of_week) {
            this.day_of_week = day_of_week;
        }
    }

    @Id // to allow mapping
    @Column(name = "clinic_id",nullable = false)
    private Integer clinicId;

    @Id
    @Column(name = "day_of_week",nullable = false)
    private Integer day_of_week;

    @MapsId("clinic_id") //TODO: should i change this one to clinicId?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(name="open_time", nullable = false)
    private Time open_time;

    @Column(name="close_time", nullable = false)
    private Time close_time;

    protected ClinicDayHours() {
        //Just for hibernate
    }

    public ClinicDayHours(int day, int clinicId, Time open_time, Time close_time) {
        this(day,open_time,close_time);
        this.clinicId = clinicId;
    }

    public ClinicDayHours(int day_of_week, Time open_time, Time close_time) {
        this.day_of_week = day_of_week;
        this.open_time = open_time;
        this.close_time = close_time;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
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
