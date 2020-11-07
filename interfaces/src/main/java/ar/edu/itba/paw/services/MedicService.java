package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.Optional;

public interface MedicService {

    Collection<Medic> getAll();

    Collection<Medic> getAllUnverified();

    Medic register(User user, String name, String telephone, String identification_type, byte[] identification, String licence_number, Collection<MedicalField> known_fields);

    Optional<Medic> findByUserId(int user_id);

    MedicalField registerFieldToMedic(int medic_id, MedicalField medicalField);

    Medic updateMedicInfo(User user, String name, String telephone, String identification_type, byte[] identification, String licence_number, Collection<MedicalField> known_fields, boolean verified);

    boolean knowsField(int medic_id, int field_id);
}
