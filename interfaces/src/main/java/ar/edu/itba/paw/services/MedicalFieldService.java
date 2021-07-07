package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MedicalField;

import java.util.Collection;
import java.util.Optional;

public interface MedicalFieldService {
    Optional<MedicalField> findById(int id);

    Optional<MedicalField> findByName(String name);

    Collection<MedicalField> getAll();

    MedicalField register(String name);
}
