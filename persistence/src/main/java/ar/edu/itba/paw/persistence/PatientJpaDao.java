package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class PatientJpaDao implements PatientDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Patient> findByUserId(final int user_id) {
        return Optional.ofNullable(em.find(Patient.class,user_id));
    }

    @Override
    public Optional<Patient> findByEmail(final String email) {
        final TypedQuery<Patient> query = em.createQuery("SELECT p FROM Patient as p WHERE p.user.email = :email",Patient.class);
        query.setParameter("email",email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Patient register(final User user, final String name) {
        User userRef = em.getReference(User.class,user.getId());
        final Patient patient = new Patient(userRef,name);
        em.persist(patient);
        return patient;
    }

    @Override
    public Patient register(final User user, final String name, final String medic_plan, final String medic_plan_number) {
        User userRef = em.getReference(User.class,user.getId());
        final Patient patient = new Patient(userRef,name,medic_plan,medic_plan_number);
        em.persist(patient);
        return patient;
    }

    @Override
    public Patient updatePatientInfo(final Patient patient, final String name, final String medic_plan, final String medic_plan_number) {
        em.detach(patient);
        patient.setName(name);
        patient.setMedic_plan(medic_plan);
        patient.setMedic_plan_number(medic_plan_number);
        em.merge(patient);
        return patient;
    }

    @Override
    public Patient updateMedicPlan(final Patient patient, final String medic_plan, final String medic_plan_number) {
        em.detach(patient);
        patient.setMedic_plan(medic_plan);
        patient.setMedic_plan_number(medic_plan_number);
        em.merge(patient);
        return patient;
    }
}
