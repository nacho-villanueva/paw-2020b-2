package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;

import java.util.Collection;
import java.util.Optional;

public interface MedicDao {

    public Optional<Medic> findById(long id);

    Medic register(String name, String email, String telephone, String licence_number, Collection<MedicalField> known_fields);
}
