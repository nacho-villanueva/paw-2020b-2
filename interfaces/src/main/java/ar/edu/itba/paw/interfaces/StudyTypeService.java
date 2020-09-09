package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface StudyTypeService {

    Collection<StudyType> getAllStudyTypes();

    Optional<StudyType> findById(int id);
}
