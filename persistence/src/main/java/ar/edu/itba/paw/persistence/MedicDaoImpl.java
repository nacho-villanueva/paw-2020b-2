package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;

@Repository
public class MedicDaoImpl implements MedicDao {

    @Override
    public Medic findById(long id) {
        return new Medic("Gus Jhonson","gus@jhonson.com","+549113125512","A4-56DF21623", new ArrayList<>(Arrays.asList("Field1","Field2")));
    }
}
