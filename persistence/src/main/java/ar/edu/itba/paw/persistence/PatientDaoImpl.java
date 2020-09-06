package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDaoImpl implements PatientDao {

    @Override
    public Patient findById(long id) {
        return new Patient("patient@email.com");
    }
}
