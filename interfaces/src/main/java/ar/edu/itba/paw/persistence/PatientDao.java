package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;

import java.util.Optional;

public interface PatientDao {

    public Optional<Patient> findById(long id);

    public Patient register(String email, String name);
}
