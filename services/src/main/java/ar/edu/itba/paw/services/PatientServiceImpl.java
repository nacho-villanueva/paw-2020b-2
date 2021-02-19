package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MedicPlan;
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

    @Autowired
    private MedicPlanService medicPlanService;

    @Override
    public Optional<Patient> findByUserId(int userId) {
        return dao.findByUserId(userId);
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
    public Patient register(User user, String name, MedicPlan medicPlan, String medicPlanNumber) {
        Patient patient = dao.register(user,name,medicPlan,medicPlanNumber);
        userService.updateRole(user,User.PATIENT_ROLE_ID);
        return patient;
    }

    @Override
    public Patient updatePatientInfo(User user, String name, MedicPlan medicPlan, String medicPlanNumber) {
        Optional<Patient> patient = dao.findByUserId(user.getId());
        return patient.map(value -> dao.updatePatientInfo(value, name, medicPlan, medicPlanNumber)).orElse(null);
    }

    @Override
    public Patient updateMedicPlan(Patient patient, MedicPlan medicPlan, String medicPlanNumber) {
        return dao.updateMedicPlan(patient,medicPlan,medicPlanNumber);
    }
}
