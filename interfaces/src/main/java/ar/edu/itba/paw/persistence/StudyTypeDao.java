package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface StudyTypeDao {
    public Optional<StudyType> findById(int id);

    public Optional<StudyType> findByName(String name);

    public Collection<StudyType> getAll();

    public Collection<StudyType> findByClinicId(int clinic_id);

    public StudyType findOrRegister(String name);

    public StudyType register(String name);
}
