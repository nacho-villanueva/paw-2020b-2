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
    public Collection<Result> findByOrderId(long id) {
        final TypedQuery<Result> query = em.createQuery("SELECT r FROM Result r WHERE r.order.orderId = :id", Result.class);
        query.setParameter("id", id);
        return query.getResultList();
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
