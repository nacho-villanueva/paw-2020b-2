package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.Result;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
public class ResultJpaDao implements ResultDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Result> findById(long id) {
        return Optional.ofNullable(em.find(Result.class, id));
    }

    @Override
    public Collection<Result> findByOrderId(long id) {
        final TypedQuery<Result> query = em.createQuery("SELECT r FROM Result r WHERE r.order.order_id = :id", Result.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public Result register(long order_id, String result_data_type, byte[] result_data, String identificationType,
                           byte[] identification, Date date, String responsible_name, String responsible_licence_number) {

        Order orderRef = em.getReference(Order.class,order_id);
        Result result = new Result(
                orderRef,
                date,
                responsible_name,
                responsible_licence_number,
                identificationType,
                identification,
                result_data_type,
                result_data);

        em.persist(result);
        em.flush();
        return result;
    }
}
