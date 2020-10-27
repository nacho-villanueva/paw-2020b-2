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

    @Override
    public Optional<Order> findById(final long id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    @Override
    public Order register(final Medic medic, final Date date, final Clinic clinic,
                          final String patient_name, final String patient_email,
                          final StudyType studyType, final String description,
                          final String identification_type, final byte[] identification,
                          final String insurance_plan, final String insurance_number) {

        final Order order = new Order(
                medic,
                date,
                clinic,
                studyType,
                description,
                identification_type,
                identification,
                insurance_plan,
                insurance_number,
                patient_name,
                patient_email);

        em.persist(order);
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
    public Collection<Order> getAllAsPatient(User user) {
        final TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.patient_email = :email", Order.class);
        query.setParameter("email", user.getEmail());
        return query.getResultList();
    }
}
