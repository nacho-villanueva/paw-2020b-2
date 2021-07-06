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

    @Autowired
    private MedicDao medicDao;

    @Autowired
    private UserService userService;

    @Override
    public Collection<Medic> getAll(int page, int pageSize) {
        return medicDao.getAll(page,pageSize);
    }

    @Override
    public long getAllCount() {
        return medicDao.getAllCount();
    }

    @Override
    public int getAllLastPage(int pageSize) {
        return getLastPage(getAllCount(),pageSize);
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
    public int getAllUnverifiedLastPage(int pageSize) {
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
    public Medic updateMedicInfo(User user, String name, String telephone, String identificationType, byte[] identification, String licenceNumber, Collection<MedicalField> knownFields) {
        return medicDao.updateMedicInfo(user,name,telephone,identificationType,identification,licenceNumber,knownFields);
    }

    @Override
    public void verifyMedic(int medicId) {
        medicDao.verifyMedic(medicId);
    }

    @Override
    public boolean knowsField(int medicId, int fieldId) {
        return medicDao.knowsField(medicId,fieldId);
    }

    // auxiliar functions
    private int getLastPage(final long count, final int pageSize){
        return (int) Math.ceil((double)count / pageSize);
    }
}
