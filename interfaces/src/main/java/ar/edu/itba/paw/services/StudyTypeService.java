package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface StudyTypeService {

    Collection<StudyType> getAll();

    Optional<StudyType> findById(int id);

    Optional<StudyType> findByName(String name);

    StudyType register(String name);

}
