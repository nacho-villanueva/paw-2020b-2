package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.MedicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class MedicServiceImpl implements MedicService {

    @Autowired
    private MedicDao medicDao;


    @Override
    public Collection<Medic> getAll() {
        return medicDao.getAll();
    }

    @Override
    public Collection<Medic> getAllUnverified() {
        return medicDao.getAllUnverified();
    }

    @Override
    public Medic register(User user, String name, String telephone, String identification_type, byte[] identification, String licence_number, Collection<MedicalField> known_fields) {
        return medicDao.register(user,name,telephone,identification_type,identification,licence_number,known_fields, false);
    }

    @Override
    public Optional<Medic> findByUserId(int user_id) {
        return medicDao.findByUserId(user_id);
    }

    @Override
    public MedicalField registerFieldToMedic(int medic_id, MedicalField medicalField) {
        return medicDao.registerFieldToMedic(medic_id,medicalField);
    }

    @Override
    public Medic updateMedicInfo(User user, String name, String telephone, String identification_type, byte[] identification, String licence_number, Collection<MedicalField> known_fields, boolean verified) {
        return medicDao.updateMedicInfo(user,name,telephone,identification_type,identification,licence_number,known_fields,verified);
    }

    @Override
    public boolean knowsField(int medic_id, int field_id) {
        return medicDao.knowsField(medic_id,field_id);
    }
}
