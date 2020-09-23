package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;
import ar.edu.itba.paw.model.User;

import java.util.Collection;
import java.util.Optional;

public interface MedicDao {

    Optional<Medic> findByUserId(int user_id);

    Collection<Medic> getAll();

    Collection<Medic> getAllUnverified();

    Medic register(User user, String name, String email, String telephone, String identification_type, byte[] identification, String licence_number, boolean verified, Collection<MedicalField> known_fields);

    MedicalField registerFieldToMedic(int medic_id, MedicalField medicalField);
}
