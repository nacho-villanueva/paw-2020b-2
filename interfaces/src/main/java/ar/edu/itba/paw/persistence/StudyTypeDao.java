package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface StudyTypeDao {
    public Optional<StudyType> findById(int id);

    public Optional<StudyType> findByName(String name);

    public Collection<StudyType> getAll();

    public StudyType findOrRegister(String name);
}
