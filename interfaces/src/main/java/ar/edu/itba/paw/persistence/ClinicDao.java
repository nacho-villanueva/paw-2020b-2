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

    Collection<Clinic> getAll();

    Collection<Clinic> getAllUnverified();

    Clinic register(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours, boolean verified);

    Clinic updateClinicInfo(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours, boolean verified);

    boolean hasStudy(int clinic_id, int studyType_id);

    Collection<Clinic> getByStudyTypeId(int studyType_id);

    StudyType registerStudyToClinic(int clinic_id, StudyType studyType);

    //If parameters are null, search will ignore those values
    Collection<Clinic> searchClinicsBy(String clinic_name, ClinicHours hours, String accepted_plan, String study_name);
}
