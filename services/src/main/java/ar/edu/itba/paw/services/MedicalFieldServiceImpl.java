package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.persistence.MedicalFieldDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class MedicalFieldServiceImpl implements MedicalFieldService {

    @Autowired
    private MedicalFieldDao dao;

    @Override
    public Optional<MedicalField> findById(int id) {
        return dao.findById(id);
    }

    @Override
    public Optional<MedicalField> findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public Collection<MedicalField> getAll() {
        return dao.getAll();
    }

    @Override
    public Collection<MedicalField> findByMedicId(int medic_id) {
        return dao.findByMedicId(medic_id);
    }

    @Override
    public MedicalField findOrRegister(String name) {
        return dao.findOrRegister(name);
    }
}
