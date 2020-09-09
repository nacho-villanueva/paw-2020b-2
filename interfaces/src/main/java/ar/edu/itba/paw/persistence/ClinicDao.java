package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface ClinicDao {

    public Optional<Clinic> findById(int id);

    Collection<Clinic> getAll();

    Clinic register(String name, String email, String telephone, Collection<StudyType> available_studies);

    StudyType registerStudyToClinic(int clinic_id, StudyType studyType);
}
