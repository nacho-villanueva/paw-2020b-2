package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Result;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface ResultService {
    Optional<Result> findById(long id);

    //TODO: deprecated, remove usages when possible
    Collection<Result> findByOrderId(long id);

    Collection<Result> findByOrderId(long id, int page);

    Collection<Result> findByOrderId(long id, int page, int pageSize);

    long findByOrderIdCount(long id);

    long findByOrderIdLastPage(long id);

    long findByOrderIdLastPage(long id, int pageSize);

    Result register(long orderId, String resultDataType, byte[] resultData, String identificationType, byte[] identification, LocalDate date, String responsibleName, String responsibleLicenceNumber);
}
