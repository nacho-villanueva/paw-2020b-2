package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;

public interface ClinicDao {

    public Clinic findById(long id);
}
