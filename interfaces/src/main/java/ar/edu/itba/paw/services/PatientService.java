package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface PatientService {

    Optional<Patient> findByUserId(int userId);

    Optional<Patient> findByEmail(String email);

    Patient register(User user, String name);

    Patient register(User user, String name, String medic_plan, String medic_plan_number);

    Patient updatePatientInfo(User user, String name, String medic_plan, String medic_plan_number);

    Patient updateMedicPlan(Patient patient, String medic_plan, String medic_plan_number);
}
