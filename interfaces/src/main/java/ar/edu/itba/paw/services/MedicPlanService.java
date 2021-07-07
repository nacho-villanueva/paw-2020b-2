package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MedicPlan;

import java.util.Collection;
import java.util.Optional;

public interface MedicPlanService {

    Optional<MedicPlan> findById(int id);

    Optional<MedicPlan> findByName(String name);

    Collection<MedicPlan> getAll();

    MedicPlan register(String name);
}
