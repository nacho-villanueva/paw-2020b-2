package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface StudyTypeDao {

    Optional<StudyType> findById(int id);

    Optional<StudyType> findByName(String name);

    Collection<StudyType> getAll();

    StudyType register(String name);

    StudyType findOrRegister(String name);
}
