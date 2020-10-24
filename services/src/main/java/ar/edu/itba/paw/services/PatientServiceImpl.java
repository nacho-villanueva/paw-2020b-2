package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao dao;

    @Override
    public Optional<Patient> findByUser_id(int user_id) {
        return dao.findByUserId(user_id);
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public Patient register(User user, String name) {
        return dao.register(user,name);
    }

    @Override
    public Patient register(User user, String name, String medic_plan, String medic_plan_number) {
        return dao.register(user,name,medic_plan,medic_plan_number);
    }

    @Override
    public Patient updatePatientInfo(User user, String name, String medic_plan, String medic_plan_number) {
        return dao.updatePatientInfo(user,name,medic_plan,medic_plan_number);
    }

    @Override
    public Patient updateMedicPlan(Patient patient, String medic_plan, String medic_plan_number) {
        return dao.updateMedicPlan(patient,medic_plan,medic_plan_number);
    }
}
