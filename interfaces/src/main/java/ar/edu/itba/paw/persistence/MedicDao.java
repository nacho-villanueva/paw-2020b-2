package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.util.Collection;
import java.util.Optional;

public interface MedicDao {

    Optional<Medic> findByUserId(int user_id);

    Collection<Medic> getAll();

    Collection<Medic> getAllUnverified();

    Medic register(User user, String name, String email, String telephone, String identification_type, byte[] identification, String licence_number, Collection<MedicalField> known_fields);

    Medic updateMedicInfo(int medic_id, String name, String email, String telephone, String identification_type, byte[] identification, String licence_number, Collection<MedicalField> known_fields, boolean verified);

    boolean knowsField(int medic_id, int field_id);

    MedicalField registerFieldToMedic(int medic_id, MedicalField medicalField);
}
