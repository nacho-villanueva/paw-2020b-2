package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao ud;

    @Override
    public Optional<Order> findById(final long id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    @Override
    public Order register(final Medic medic, final Date date, final Clinic clinic,
                          final String patient_email, final String patient_name,
                          final StudyType studyType, final String description,
                          final String identification_type, final byte[] identification,
                          final String insurance_plan, final String insurance_number) {

        final Medic medicReference = em.getReference(Medic.class, medic.getUser().getId());
        final Clinic clinicReference = em.getReference(Clinic.class, clinic.getUser().getId());
        final StudyType studyTypeReference = em.getReference(StudyType.class, studyType.getId());


        final Order order = new Order(
                medicReference,
                date,
                clinicReference,
                studyTypeReference,
                description,
                identification_type,
                identification,
                insurance_plan,
                insurance_number,
                patient_email,
                patient_name);

        em.persist(order);
        em.flush();
        return order;
    }

    @Override
    public Collection<Order> getAllAsClinic(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o JOIN o.clinic c WHERE c.user_id = :id", Order.class);
        query.setParameter("id", user.getId());
            return query.getResultList();
    }

    @Override
    public Collection<Order> getAllAsMedic(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o JOIN o.medic m WHERE m.user_id = :id", Order.class);
        query.setParameter("id", user.getId());
        return query.getResultList();
    }

    @Override
    public Collection<Order> getAllSharedAsMedic(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o, User m WHERE m.id=:userId AND m MEMBER o.shared_with", Order.class);
        query.setParameter("userId", user.getId());
        return query.getResultList();
    }

    @Override
    public Order shareWithMedic(Order order, User user){
        final Optional<Order> maybeOrder = findById(order.getOrder_id());
        if(order.getMedic().getUser_id() == user.getId())
            return null;
        final Optional<User> maybeUser = ud.findById(user.getId());
        if(maybeOrder.isPresent() && maybeUser.isPresent()){
            maybeOrder.get().addToShared_with(maybeUser.get());
            em.flush();
        }

        return maybeOrder.orElse(null);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.patient_email = :email", Order.class);
        query.setParameter("email", user.getEmail());
        return query.getResultList();
    }

    @Override
    public Collection<Order> getAllAsPatientOfType(String email, StudyType type){
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.patient_email = :email AND o.study = :type", Order.class);
        query.setParameter("email", email);
        query.setParameter("type", type);
        return query.getResultList();
    }
}
