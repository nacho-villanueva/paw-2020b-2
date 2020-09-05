package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Collection;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    MedicDao medicDao;

    @Autowired
    ClinicDao clinicDao;

    @Autowired
    PatientDao patientDao;

    @Autowired
    ResultDao resultDao;

    @Override
    public Order findById(long id) {
        Medic medic = medicDao.findById(id); //Todo: change id for DB response
        Clinic clinic = clinicDao.findById(id); //Todo: change id for DB response
        Patient patient = patientDao.findById(id); //Todo: change id for DB response
        Collection<Result> results = resultDao.findByOrderId(id); //Todo: change id for DB response

        return new Order(medic,Date.valueOf("2020-05-13"),clinic,"Colonoscopy","Do it X way",null,"Osde","das667213",patient,results);
    }
}
