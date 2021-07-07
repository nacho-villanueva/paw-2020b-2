package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.Result;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
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
    public Collection<Result> findByOrderId(long id, int page, int pageSize) {

        String queryString = "SELECT r FROM Result r " +
                "WHERE r.order.orderId = :id " +
                "ORDER BY r.id";

        final TypedQuery<Result> query = em.createQuery(queryString, Result.class);
        query.setParameter("id", id);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long findByOrderIdCount(long id) {

        String queryString = "SELECT COUNT(r) FROM Result r " +
                "WHERE r.order.orderId = :id";

        final TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Result register(long orderId, String resultDataType, byte[] resultData, String identificationType,
                           byte[] identification, LocalDate date, String responsibleName, String responsibleLicenceNumber) {

        Order orderRef = em.getReference(Order.class,orderId);
        Result result = new Result(
                orderRef,
                date,
                responsibleName,
                responsibleLicenceNumber,
                identificationType,
                identification,
                resultDataType,
                resultData);

        em.persist(result);
        em.flush();
        return result;
    }
}
