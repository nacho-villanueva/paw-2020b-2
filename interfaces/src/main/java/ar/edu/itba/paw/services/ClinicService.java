package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Clinic;

import java.util.Collection;
import java.util.Optional;

public interface ClinicService {

    Collection<Clinic> getAllClinics();

    Collection<Clinic> getByStudyTypeId(int studyType_id);

    Optional<Clinic> findById(int id);
}
