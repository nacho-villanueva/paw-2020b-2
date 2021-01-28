package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MedicalField;

import java.util.Collection;
import java.util.Optional;

public interface MedicalFieldDao {

    Optional<MedicalField> findById(int id);

    Optional<MedicalField> findByName(String name);

    Collection<MedicalField> getAll();

    MedicalField register(String name);

    MedicalField findOrRegister(String name);
}
