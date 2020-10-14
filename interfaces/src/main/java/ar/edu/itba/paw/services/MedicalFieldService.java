package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.MedicalField;

import java.util.Collection;
import java.util.Optional;

public interface MedicalFieldService {
    public Optional<MedicalField> findById(int id);

    public Optional<MedicalField> findByName(String name);

    public Collection<MedicalField> getAll();

    public Collection<MedicalField> findByMedicId(int medic_id);

    public MedicalField findOrRegister(String name);
}
