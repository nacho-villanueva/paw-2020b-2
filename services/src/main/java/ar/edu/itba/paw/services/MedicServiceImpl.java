package ar.edu.itba.paw.services;

import ar.edu.itba.paw.service.MedicService;
import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.persistence.MedicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class MedicServiceImpl implements MedicService {

    @Autowired
    MedicDao medicDao;

    @Override
    public Collection<Medic> getAllMedics() {
        return medicDao.getAll();
    }

    @Override
    public Optional<Medic> findById(int id){
        return medicDao.findById(id);
    }
}
