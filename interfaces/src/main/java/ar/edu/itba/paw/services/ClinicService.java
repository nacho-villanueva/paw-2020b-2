package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ClinicService {

    Collection<Clinic> getAll(int page, int pageSize);

    long getAllCount();

    int getAllLastPage(int pageSize);

    Optional<Clinic> findByUserId(int userId);

    Collection<Clinic> getAllUnverified(int page, int pageSize);

    long getAllUnverifiedCount();

    int getAllUnverifiedLastPage(int pageSize);

    Clinic register(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours);

    Clinic updateClinicInfo(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours, boolean verified);

    boolean hasStudy(int clinicId, int studyTypeId);

    StudyType registerStudyToClinic(int clinicId, StudyType studyType);

    Collection<Clinic> getByStudyTypeId(int studyTypeId, int page, int pageSize);

    long getByStudyTypeIdCount(int studyTypeId);

    int getByStudyTypeIdLastPage(int studyTypeId, int pageSize);

    //If parameters are null, search will ignore those values
    Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String studyName, int page, int pageSize);

    long searchClinicsByCount(String clinicName, ClinicHours hours, String acceptedPlan, String studyName);

    int searchClinicsByLastPage(String clinicName, ClinicHours hours, String acceptedPlan, String studyName, int pageSize);
}
