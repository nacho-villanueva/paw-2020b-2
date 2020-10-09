package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;

import java.util.Collection;
import java.util.Optional;

public interface ClinicDao {

    Optional<Clinic> findByUserId(int user_id);

    Collection<Clinic> getAll();

    Collection<Clinic> getAllUnverified();

    Clinic register(User user, String name, String email, String telephone, Collection<StudyType> available_studies);

    Clinic updateClinicInfo(int clinic_id, String name, String email, String telephone, Collection<StudyType> available_studies, boolean verified);

    boolean hasStudy(int clinic_id, int studyType_id);

    Collection<Clinic> getByStudyTypeId(int studyType_id);

    StudyType registerStudyToClinic(int clinic_id, StudyType studyType);
}
