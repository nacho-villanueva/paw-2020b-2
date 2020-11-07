package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MedicalField;

import java.util.Collection;
import java.util.Optional;

public interface MedicalFieldDao {

    public Optional<MedicalField> findById(int id);

    public Optional<MedicalField> findByName(String name);

    public Collection<MedicalField> getAll();

    public MedicalField findOrRegister(String name);
}
