package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface PatientDao {

    Optional<Patient> findByUserId(int userId);

    Optional<Patient> findByEmail(String email);

    Patient register(User user, String name);

    Patient register(User user, String name, String medicPlan, String medicPlanNumber);

    Patient updatePatientInfo(Patient patient, String name, String medicPlan, String medicPlanNumber);

    Patient updateMedicPlan(Patient patient, String medicPlan, String medicPlanNumber);
}
