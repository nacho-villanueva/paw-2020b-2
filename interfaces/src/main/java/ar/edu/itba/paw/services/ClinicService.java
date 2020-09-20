package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;

import java.util.Collection;
import java.util.Optional;

public interface ClinicService {

    Collection<Clinic> getAll();

    Optional<Clinic> findByUserId(int user_id);

    Collection<Clinic> getAllUnverified();

    Clinic register(User user, String name, String email, String telephone, boolean verified, Collection<StudyType> available_studies);

    StudyType registerStudyToClinic(int clinic_id, StudyType studyType);

}
