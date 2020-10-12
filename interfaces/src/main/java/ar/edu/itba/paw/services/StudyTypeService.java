package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface StudyTypeService {

    Collection<StudyType> getAll();

    public Collection<StudyType> findByClinicId(int clinic_id);

    public Optional<StudyType> findById(int id);

    public Optional<StudyType> findByName(String name);

    public StudyType findOrRegister(String name);

}
