package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao ud;

    @Autowired
    private ClinicDao cd;

    @Override
    public Optional<Order> findById(final long id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    @Override
    public Order register(final Medic medic, final LocalDate date, final Clinic clinic,
                          final String patientEmail, final String patientName,
                          final StudyType studyType, final String description,
                          final String identificationType, final byte[] identification,
                          final String insurancePlan, final String insuranceNumber) {

        final Medic medicReference = em.getReference(Medic.class, medic.getUser().getId());
        final Clinic clinicReference = em.getReference(Clinic.class, clinic.getUser().getId());
        final StudyType studyTypeReference = em.getReference(StudyType.class, studyType.getId());


        final Order order = new Order(
                medicReference,
                date,
                clinicReference,
                studyTypeReference,
                description,
                identificationType,
                identification,
                insurancePlan,
                insuranceNumber,
                patientEmail,
                patientName);

        em.persist(order);
        em.flush();
        return order;
    }

    @Override
    public Collection<Order> getAllAsClinic(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o JOIN o.clinic c WHERE c.userId = :id", Order.class);
        query.setParameter("id", user.getId());
            return query.getResultList();
    }

    @Override
    public Collection<Order> getAllAsMedic(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o JOIN o.medic m WHERE m.userId = :id", Order.class);
        query.setParameter("id", user.getId());
        return query.getResultList();
    }

    @Override
    public Collection<Order> getAllSharedAsMedic(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o, User m WHERE m.id=:userId AND m MEMBER o.sharedWith", Order.class);
        query.setParameter("userId", user.getId());
        return query.getResultList();
    }

    @Override
    public Order shareWithMedic(Order order, User user){
        final Optional<Order> maybeOrder = findById(order.getOrderId());
        if(order.getMedic().getUserId() == user.getId())
            return null;
        final Optional<User> maybeUser = ud.findById(user.getId());
        if(maybeOrder.isPresent() && maybeUser.isPresent()){
            maybeOrder.get().addToSharedWith(maybeUser.get());
            em.flush();
        }

        return maybeOrder.orElse(null);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.patientEmail = :email", Order.class);
        query.setParameter("email", user.getEmail());
        return query.getResultList();
    }

    @Override
    public Order changeOrderClinic(Order order, Clinic clinic){
        Optional<Clinic> maybeClinic = cd.findByUserId(clinic.getUserId());
        if(!maybeClinic.isPresent())
            return null;
        Optional<Order> orderDB = findById(order.getOrderId());
        if(!orderDB.isPresent())
            return null;

        orderDB.get().setClinic(maybeClinic.get());
        em.flush();
        return orderDB.get();
    }
}
