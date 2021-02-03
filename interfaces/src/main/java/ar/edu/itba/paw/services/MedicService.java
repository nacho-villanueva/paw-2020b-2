package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.Optional;

public interface MedicService {

    //TODO: deprecated, remove usages when possible
    Collection<Medic> getAll();

    Collection<Medic> getAll(int page);

    Collection<Medic> getAll(int page, int pageSize);

    long getAllCount();

    long getAllLastPage();

    long getAllLastPage(int pageSize);

    //TODO: deprecated, remove usages when possible
    Collection<Medic> getAllUnverified();

    Collection<Medic> getAllUnverified(int page);

    Collection<Medic> getAllUnverified(int page, int pageSize);

    long getAllUnverifiedCount();

    long getAllUnverifiedLastPage();

    long getAllUnverifiedLastPage(int pageSize);

    Medic register(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields);

    Optional<Medic> findByUserId(int userId);

    MedicalField registerFieldToMedic(int medicId, MedicalField medicalField);

    Medic updateMedicInfo(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields, boolean verified);

    boolean knowsField(int medicId, int fieldId);

    int getPageCount(int perPage);
}
