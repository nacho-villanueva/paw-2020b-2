package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Study;

import java.util.Collection;
import java.util.Optional;

public interface ClinicDao {

    public Optional<Clinic> findById(long id);

    Clinic register(String name, String email, String telephone, Collection<Study> available_studies);
}
