package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.MedicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class MedicServiceImpl implements MedicService {

    // Pagination-related constants
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_MAX_PAGE_SIZE = 20;

    @Autowired
    private MedicDao medicDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MedicalFieldService medicalFieldService;

    @Override
    public Collection<Medic> getAll() {
        return getAll(DEFAULT_PAGE);
    }

    @Override
    public Collection<Medic> getAll(int page) {
        return getAll(page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Medic> getAll(int page, int pageSize) {
        return medicDao.getAll(page,pageSize);
    }

    @Override
    public long getAllCount() {
        return medicDao.getAllCount();
    }

    @Override
    public long getAllLastPage() {
        return getAllLastPage(DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllLastPage(int pageSize) {
        return getLastPage(getAllCount(),pageSize);
    }

    @Override
    public Collection<Medic> getAllUnverified() {
        return getAllUnverified(DEFAULT_PAGE);
    }

    @Override
    public Collection<Medic> getAllUnverified(int page) {
        return getAllUnverified(page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Medic> getAllUnverified(int page, int pageSize) {
        return medicDao.getAllUnverified(page,pageSize);
    }

    @Override
    public long getAllUnverifiedCount() {
        return medicDao.getAllUnverifiedCount();
    }

    @Override
    public long getAllUnverifiedLastPage() {
        return getAllUnverifiedLastPage(DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllUnverifiedLastPage(int pageSize) {
        return getLastPage(getAllUnverifiedCount(),pageSize);
    }

    @Override
    public Medic register(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields) {
        Medic medic = medicDao.register(user,name,telephone,identificationType,identification,licenceNumber,knownFields, false);
        userService.updateRole(user,User.MEDIC_ROLE_ID);
        return medic;
    }

    @Override
    public Optional<Medic> findByUserId(int userId) {
        return medicDao.findByUserId(userId);
    }

    @Override
    public MedicalField registerFieldToMedic(int medicId, MedicalField medicalField) {
        return medicDao.registerFieldToMedic(medicId,medicalField);
    }

    @Override
    public Medic updateMedicInfo(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields, boolean verified) {
        return medicDao.updateMedicInfo(user,name,telephone,identificationType,identification,licenceNumber,knownFields,verified);
    }

    @Override
    public boolean knowsField(int medicId, int fieldId) {
        return medicDao.knowsField(medicId,fieldId);
    }

    // auxiliar functions
    private long getLastPage(final long count, final int pageSize){
        return (long) Math.ceil((double)count / pageSize);
    }
}
