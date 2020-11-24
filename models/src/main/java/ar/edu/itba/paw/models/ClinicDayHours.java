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
        private int dayOfWeek;

        public ClinicDayHoursPK() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClinicDayHoursPK that = (ClinicDayHoursPK) o;
            return clinicId == that.clinicId &&
                    dayOfWeek == that.dayOfWeek;
        }

        @Override
        public int hashCode() {

            return Objects.hash(clinicId, dayOfWeek);
        }

        public void setClinicId(int clinicId) {
            this.clinicId = clinicId;
        }

        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
    }

    @Id // to allow mapping
    @Column(name = "clinic_id",nullable = false)
    private Integer clinicId;

    @Id
    @Column(name = "day_of_week",nullable = false)
    private Integer dayOfWeek;

    @MapsId("clinic_id") //TODO: should i change this one to clinicId?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(name="open_time", nullable = false)
    private Time openTime;

    @Column(name="close_time", nullable = false)
    private Time closeTime;

    protected ClinicDayHours() {
        //Just for hibernate
    }

    public ClinicDayHours(int day, int clinicId, Time openTime, Time closeTime) {
        this(day,openTime,closeTime);
        this.clinicId = clinicId;
    }

    public ClinicDayHours(int dayOfWeek, Time openTime, Time closeTime) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    public Time getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }
}
