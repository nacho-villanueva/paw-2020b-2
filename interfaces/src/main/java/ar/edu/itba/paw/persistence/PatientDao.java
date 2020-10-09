package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface PatientDao {

    Optional<Patient> findByUser_id(int user_id);

    Optional<Patient> findByEmail(String email);

    Patient register(User user, String name);

    Patient register(User user, String name, String medic_plan, String medic_plan_number);

    Patient updatePatientInfo(User user, String name, String medic_plan, String medic_plan_number);

    Patient updateMedicPlan(Patient patient, String medic_plan, String medic_plan_number);
}
