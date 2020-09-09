package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Medic;

import java.util.Collection;
import java.util.Optional;

public interface MedicService {

    public Collection<Medic> getAllMedics();

    Optional<Medic> findById(int id);
}
