package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import java.util.Collection;
import java.util.Optional;

public interface MedicDao {

    Optional<Medic> findByUserId(int userId);

    Collection<Medic> getAll();

    Collection<Medic> getAllUnverified();

    Medic register(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields, boolean verified);

    Medic updateMedicInfo(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields, boolean verified);

    boolean knowsField(int medic_id, int field_id);

    MedicalField registerFieldToMedic(int medic_id, MedicalField medicalField);
}
