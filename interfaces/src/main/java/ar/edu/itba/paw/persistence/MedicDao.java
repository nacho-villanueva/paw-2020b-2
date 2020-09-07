package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;

import java.util.Optional;

public interface MedicDao {

    public Optional<Medic> findById(long id);
}
