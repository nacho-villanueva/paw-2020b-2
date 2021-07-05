package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.annotations.*;

import javax.validation.Valid;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@TimeIntervals(message = "OrderFilterDto.TimeIntervalsAreValid")
public class OrderFilterDto {

    // Variables
    @Valid
    @ClinicId(message = "OrderFilterDto.UserIdOnlyDto.ClinicIdIsValid")
    @QueryParam("clinic")
    private Set<Integer> clinics;

    @Valid
    @MedicId(message = "OrderFilterDto.UserIdOnlyDto.MedicIdIsValid")
    @QueryParam("medic")
    private Set<Integer> medics;

    @Valid
    @EmailCollection(message = "OrderFilterDto.patientEmails.EmailCollection")
    @QueryParam("patient-email")
    private Set<String> patientEmails;

    @QueryParam("from-date")
    private LocalDate fromDate;

    @QueryParam("to-date")
    private LocalDate toDate;

    @Valid
    @StudyTypeId(message = "OrderFilterDto.studyTypes.StudyTypeIdIsValid")
    @QueryParam("study-type")
    private Set<Integer> studyTypes;

    @DefaultValue("false")
    @QueryParam("include-shared")
    private boolean includeShared;

    // Constructors
    public OrderFilterDto() {
        // Use Factory Methods
    }

    // Getters&Setters
    public Set<Integer> getClinics() {
        return removeNullIntegers(clinics);
    }

    public void setClinics(Set<Integer> clinics) {
        this.clinics = clinics;
    }

    public Set<Integer> getMedics() {
        return removeNullIntegers(medics);
    }

    public void setMedics(Set<Integer> medics) {
        this.medics = medics;
    }

    public Set<String> getPatientEmails() {
        return removeEmptyStrings(patientEmails);
    }

    public void setPatientEmails(Set<String> patientEmails) {
        this.patientEmails = patientEmails;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Set<Integer> getStudyTypes() {
        return removeNullIntegers(studyTypes);
    }

    public void setStudyTypes(Set<Integer> studyTypes) {
        this.studyTypes = studyTypes;
    }

    public boolean isIncludeShared() {
        return includeShared;
    }

    public void setIncludeShared(boolean includeShared) {
        this.includeShared = includeShared;
    }

    // Private aux functions
    private Set<Integer> removeNullIntegers(Set<Integer> set) {
        Set<Integer> cleanSet = new HashSet<>();

        for (Integer elem : set) {
            if (elem != null)
                cleanSet.add(elem);
        }
        return cleanSet;
    }

    private Set<String> removeEmptyStrings(Set<String> set) {
        Set<String> cleanSet = new HashSet<>();

        for (String elem : set) {
            if (elem != null && !"".equals(elem.trim()))
                cleanSet.add(elem);
        }
        return cleanSet;
    }
}
