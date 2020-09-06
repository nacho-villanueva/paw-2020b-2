package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;

@Repository
public class ClinicDaoImpl implements ClinicDao {

    @Override
    public Clinic findById(long id) {
        return new Clinic("Pepitos","pepitos@studies.com","+54911231512",new ArrayList<>(Arrays.asList("Colonoscopy","MRA")));
    }
}
