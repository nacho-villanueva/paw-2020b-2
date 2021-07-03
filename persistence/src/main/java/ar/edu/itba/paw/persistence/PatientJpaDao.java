package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MedicPlan;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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

    @Autowired
    private MedicPlanDao medicPlanDao;

    @Override
    public Optional<Patient> findByUserId(final int userId) {
        return Optional.ofNullable(em.find(Patient.class,userId));
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
        em.flush();
        return patient;
    }

    @Override
    public Patient register(final User user, final String name, final MedicPlan medicPlan, final String medicPlanNumber) {
        //Getting user ref
        User userRef = em.getReference(User.class,user.getId());

        //Getting plan ref
        MedicPlan planRef = getPlanRef(medicPlan);

        final Patient patient = new Patient(userRef,name,planRef,medicPlanNumber);
        em.persist(patient);
        em.flush();
        return patient;
    }

    @Override
    public Patient updatePatientInfo(final Patient patient, final String name, final MedicPlan medicPlan, final String medicPlanNumber) {
        Optional<Patient> patientDB = findByUserId(patient.getUser().getId());
        patientDB.ifPresent(patient1 -> {
            patient1.setName(name);
            patient1.setMedicPlan(getPlanRef(medicPlan));
            patient1.setMedicPlanNumber(medicPlanNumber);
            em.flush();
        });
        return patientDB.orElse(null);
    }

    @Override
    public Patient updateMedicPlan(final Patient patient, final MedicPlan medicPlan, final String medicPlanNumber) {
        Optional<Patient> patientDB = findByUserId(patient.getUser().getId());
        patientDB.ifPresent(patient1 -> {
            patient1.setMedicPlan(getPlanRef(medicPlan));
            patient1.setMedicPlanNumber(medicPlanNumber);
            em.flush();
        });
        return patientDB.orElse(null);
    }

    private MedicPlan getPlanRef(MedicPlan medicPlan) {
        MedicPlan planRef;
        if(medicPlan.getId() != null) {
            planRef = em.getReference(MedicPlan.class, medicPlan.getId());
            if(planRef != null) {
                return planRef;
            }
        }

        //If plan does not exists we try and create it then add its reference that we know it wont fail (in theory)
        MedicPlan newPlan = medicPlanDao.findOrRegister(medicPlan.getName());
        return em.getReference(MedicPlan.class, newPlan.getId());
    }
}
