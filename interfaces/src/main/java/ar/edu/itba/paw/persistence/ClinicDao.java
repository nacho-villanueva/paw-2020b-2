package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ClinicDao {

    Optional<Clinic> findByUserId(int userId);

    Collection<Clinic> getAll(int page, int pageSize);

    long getAllCount();

    Collection<Clinic> getAllUnverified(int page, int pageSize);

    long getAllUnverifiedCount();

    Clinic register(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours, boolean verified);

    Clinic updateClinicInfo(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours, boolean verified);

    boolean hasStudy(int clinicId, int studyTypeId);

    Collection<Clinic> getByStudyTypeId(int studyTypeId, int page, int pageSize);

    long getByStudyTypeIdCount(int studyTypeId);

    StudyType registerStudyToClinic(int clinicId, StudyType studyType);

    //If parameters are null, search will ignore those values
    Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String studyName, int page, int pageSize);

    long searchClinicsByCount(String clinicName, ClinicHours hours, String acceptedPlan, String studyName);
}
