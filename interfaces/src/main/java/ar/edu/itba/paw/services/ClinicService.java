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

    Clinic updateClinicInfo(User user, String name, String telephone, Collection<StudyType> available_studies, Set<String> medic_plans, ClinicHours hours, boolean verified);

    boolean hasStudy(int clinic_id, int studyType_id);

    StudyType registerStudyToClinic(int clinic_id, StudyType studyType);

    Collection<Clinic> getByStudyTypeId(int studyType_id);

    //If parameters are null, search will ignore those values
    Collection<Clinic> searchClinicsBy(String clinic_name, ClinicHours hours, String accepted_plan, String study_name);
}
