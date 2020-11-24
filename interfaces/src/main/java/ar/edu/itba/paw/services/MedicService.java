package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.Optional;

public interface MedicService {

    Collection<Medic> getAll();

    Collection<Medic> getAllUnverified();

    Medic register(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields);

    Optional<Medic> findByUserId(int userId);

    MedicalField registerFieldToMedic(int medicId, MedicalField medicalField);

    Medic updateMedicInfo(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields, boolean verified);

    boolean knowsField(int medicId, int fieldId);
}
