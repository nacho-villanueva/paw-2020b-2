package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;

import java.util.Optional;

public interface ClinicDao {

    public Optional<Clinic> findById(long id);
}
