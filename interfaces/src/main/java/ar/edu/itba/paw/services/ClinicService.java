package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Clinic;

import java.util.Collection;
import java.util.Optional;

public interface ClinicService {

    Collection<Clinic> getAllClinics();

    Optional<Clinic> findById(int id);
}
