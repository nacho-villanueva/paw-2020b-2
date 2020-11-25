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

    @Autowired
    private MedicalFieldService medicalFieldService;

    @Override
    public Collection<Medic> getAll() {
        return medicDao.getAll();
    }

    @Override
    public Collection<Medic> getAllUnverified() {
        return medicDao.getAllUnverified();
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
}
