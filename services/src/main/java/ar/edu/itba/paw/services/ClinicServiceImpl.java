package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.persistence.ClinicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    ClinicDao clinicDao;

    @Override
    public Collection<Clinic> getAllClinics() {
        return clinicDao.getAll();
    }
}
