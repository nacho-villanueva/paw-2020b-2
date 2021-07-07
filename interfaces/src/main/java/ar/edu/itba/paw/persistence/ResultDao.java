package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Result;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface ResultDao {

    Optional<Result> findById(long id);

    Collection<Result> findByOrderId(long id, int page, int pageSize);

    long findByOrderIdCount(long id);

    Result register(long orderId, String resultDataType, byte[] resultData, String identificationType, byte[] identification, LocalDate date, String responsibleName, String responsibleLicenceNumber);
}
