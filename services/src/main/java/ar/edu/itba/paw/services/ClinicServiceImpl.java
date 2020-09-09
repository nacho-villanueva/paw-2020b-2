package ar.edu.itba.paw.services;

import ar.edu.itba.paw.service.ClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.persistence.ClinicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    ClinicDao clinicDao;

    @Override
    public Collection<Clinic> getAllClinics() {
        return clinicDao.getAll();
    }

    @Override
    public Optional<Clinic> findById(int id){
        return clinicDao.findById(id);
    }
}