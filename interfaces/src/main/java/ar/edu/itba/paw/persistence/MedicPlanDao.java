package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MedicPlan;

import java.util.Collection;
import java.util.Optional;

public interface MedicPlanDao {

    Optional<MedicPlan> findById(int id);

    Optional<MedicPlan> findByName(String name);

    Collection<MedicPlan> getAll();

    MedicPlan register(String name);

    MedicPlan findOrRegister(String name);
}
