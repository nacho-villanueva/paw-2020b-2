package ar.edu.itba.paw.webapp.dto;


import ar.edu.itba.paw.models.ClinicDayHours;
import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.webapp.dto.constraintGroups.ClinicPost;
import ar.edu.itba.paw.webapp.dto.constraintGroups.ClinicPut;
import ar.edu.itba.paw.webapp.dto.validators.DaysAreValid;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClinicPostAndPutDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.clinic.v1";

    // Variables
    @NotBlank(message="ClinicPostAndPutDto.name.NotBlank", groups = {ClinicPost.class})
    private String name;

    @NotBlank(message="ClinicPostAndPutDto.telephone.NotBlank", groups = {ClinicPost.class})
    @Pattern(regexp = "\\+?[0-9]*", message="ClinicPostAndPutDto.telephone.Pattern", groups = {ClinicPost.class, ClinicPut.class})
    private String telephone;

    @Valid
    @NotEmpty(message="ClinicPostAndPutDto.availableStudies.NotEmpty", groups = {ClinicPost.class})
    private Collection<StudyTypeDto> availableStudies;

    @Valid
    private Collection<MedicPlanDto> acceptedPlans;

    @Valid
    @DaysAreValid(message="ClinicPostAndPutDto.hours.DaysAreValid", groups = {ClinicPost.class, ClinicPut.class})
    private Collection<ClinicDayHoursDto> hours;


    // Constructors
    public ClinicPostAndPutDto() {
        // Use factory method
    }

    // Getters&Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Collection<StudyTypeDto> getAvailableStudies() {
        return availableStudies;
    }

    public void setAvailableStudies(Collection<StudyTypeDto> availableStudies) {
        this.availableStudies = availableStudies;
    }

    public Collection<MedicPlanDto> getAcceptedPlans() {
        return acceptedPlans;
    }

    public void setAcceptedPlans(Collection<MedicPlanDto> acceptedPlans) {
        this.acceptedPlans = acceptedPlans;
    }

    public Collection<ClinicDayHoursDto> getHours() {
        return hours;
    }

    public void setHours(Collection<ClinicDayHoursDto> hours) {
        this.hours = hours;
    }


    // Equals&HashCode

    // etc.
    public ClinicHours getClinicHours(){

        if(this.hours==null)
            return null;

        Collection<ClinicDayHours> clinicDayHoursCollection = this.hours.stream()
                .map(h -> (new ClinicDayHours(h.getDay(),h.getOpenTime(),h.getCloseTime())))
                .collect(Collectors.toList());
        return new ClinicHours(clinicDayHoursCollection);
    }

    public Collection<StudyType> getStudiesCollection(){

        if(this.availableStudies==null)
            return null;

        return this.availableStudies.stream().map(st -> new StudyType(st.getName())).collect(Collectors.toList());
    }

    public Set<String> getMedicPlansCollection(){

        if(this.acceptedPlans==null)
            return null;

        return this.acceptedPlans.stream().map(p -> p.plan).collect(Collectors.toSet());
    }
}
