package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.ClinicHours;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ClinicDao {

    Optional<Clinic> findByUserId(int user_id);

    Collection<Clinic> getAll();

    Collection<Clinic> getAllUnverified();

    Clinic register(User user, String name, String telephone, Collection<StudyType> available_studies, Set<String> medic_plans, ClinicHours hours, boolean verified);

    Clinic updateClinicInfo(User user, String name, String telephone, Collection<StudyType> available_studies, Set<String> medic_plans, ClinicHours hours, boolean verified);

    boolean hasStudy(int clinic_id, int studyType_id);

    Collection<Clinic> getByStudyTypeId(int studyType_id);

    StudyType registerStudyToClinic(int clinic_id, StudyType studyType);
}
