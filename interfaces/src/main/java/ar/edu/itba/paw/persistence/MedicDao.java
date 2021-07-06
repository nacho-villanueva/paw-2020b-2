package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import java.util.Collection;
import java.util.Optional;

public interface MedicDao {

    Optional<Medic> findByUserId(int userId);

    Collection<Medic> getAll(int page, int pageSize);

    long getAllCount();

    Collection<Medic> getAllUnverified(int page, int pageSize);

    long getAllUnverifiedCount();

    Medic register(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields, boolean verified);

    Medic updateMedicInfo(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields);

    void verifyMedic(int medicId);

    boolean knowsField(int medicId, int fieldId);

    MedicalField registerFieldToMedic(int medicId, MedicalField medicalField);
}
