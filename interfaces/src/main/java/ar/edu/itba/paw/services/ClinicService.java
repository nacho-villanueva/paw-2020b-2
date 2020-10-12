package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.ClinicHours;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ClinicService {

    Collection<Clinic> getAll();

    Optional<Clinic> findByUserId(int user_id);

    Collection<Clinic> getAllUnverified();

    Clinic register(User user, String name, String telephone, Collection<StudyType> available_studies, Set<String> medic_plans, ClinicHours hours);

    StudyType registerStudyToClinic(int clinic_id, StudyType studyType);

    Collection<Clinic> getByStudyTypeId(int studyType_id);

}
