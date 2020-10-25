package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.PatientDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao dao;

    @Autowired
    private UserService userService;

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
        Patient patient = dao.register(user,name);
        userService.updateRole(user,User.PATIENT_ROLE_ID);
        return patient;
    }

    @Override
    public Patient register(User user, String name, String medic_plan, String medic_plan_number) {
        Patient patient = dao.register(user,name,medic_plan,medic_plan_number);
        userService.updateRole(user,User.PATIENT_ROLE_ID);
        return patient;
    }

    @Override
    public Patient updatePatientInfo(User user, String name, String medic_plan, String medic_plan_number) {
        Optional<Patient> patient = dao.findByUserId(user.getId());
        return patient.map(value -> dao.updatePatientInfo(value, name, medic_plan, medic_plan_number)).orElse(null);
    }

    @Override
    public Patient updateMedicPlan(Patient patient, String medic_plan, String medic_plan_number) {
        return dao.updateMedicPlan(patient,medic_plan,medic_plan_number);
    }
}
