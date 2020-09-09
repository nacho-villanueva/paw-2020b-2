package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;

import java.util.Optional;

public interface PatientDao {

    public Optional<Patient> findById(int id);

    public Optional<Patient> findByEmail(String email);

    public Patient findOrRegister(String email, String name);

    public Patient register(String email, String name);
}
