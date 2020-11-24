package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name="clinic_hours")
@IdClass(ClinicDayHours.ClinicDayHoursPK.class)
public class ClinicDayHours {

    public static class ClinicDayHoursPK implements Serializable {
        private int clinic_id;
        private int day_of_week;

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

        public void setClinic_id(int clinic_id) {
            this.clinic_id = clinic_id;
        }

        public void setDay_of_week(int day_of_week) {
            this.day_of_week = day_of_week;
        }
    }

    @Id // to allow mapping
    private Integer clinic_id;

    @Id
    @Column(name = "day_of_week",nullable = false)
    private Integer day_of_week;

    @MapsId("clinic_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(name="open_time", nullable = false)
    private LocalTime open_time;

    @Column(name="close_time", nullable = false)
    private LocalTime close_time;

    protected ClinicDayHours() {
        //Just for hibernate
    }

    public ClinicDayHours(int day, int clinic_id, LocalTime open_time, LocalTime close_time) {
        this(day,open_time,close_time);
        this.clinic_id = clinic_id;
    }

    public ClinicDayHours(int day_of_week, LocalTime open_time, LocalTime close_time) {
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

    public LocalTime getOpen_time() {
        return open_time;
    }

    public void setOpen_time(LocalTime open_time) {
        this.open_time = open_time;
    }

    public LocalTime getClose_time() {
        return close_time;
    }

    public void setClose_time(LocalTime close_time) {
        this.close_time = close_time;
    }
}
