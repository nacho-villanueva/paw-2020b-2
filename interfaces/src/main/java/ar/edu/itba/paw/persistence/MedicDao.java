package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;

public interface MedicDao {

    public Medic findById(long id);
}
