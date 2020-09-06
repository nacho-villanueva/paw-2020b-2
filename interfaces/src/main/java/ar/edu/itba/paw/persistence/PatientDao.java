package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;

public interface PatientDao {

    public Patient findById(long id);
}
