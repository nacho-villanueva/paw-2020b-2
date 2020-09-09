package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MedicService;
import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.persistence.MedicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MedicServiceImpl implements MedicService {

    @Autowired
    MedicDao medicDao;

    @Override
    public Collection<Medic> getAllMedics() {
        return medicDao.getAll();
    }
}
