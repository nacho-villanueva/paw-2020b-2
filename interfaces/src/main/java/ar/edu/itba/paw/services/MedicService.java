package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;
import ar.edu.itba.paw.model.User;

import java.util.Collection;
import java.util.Optional;

public interface MedicService {

    Collection<Medic> getAll();

    Collection<Medic> getAllUnverified();

    Medic register(User user, String name, String telephone, String identification_type, byte[] identification, String licence_number, Collection<MedicalField> known_fields);

    Optional<Medic> findByUserId(int user_id);

    MedicalField registerFieldToMedic(int medic_id, MedicalField medicalField);
}
