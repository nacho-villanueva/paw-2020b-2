package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Result;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface ResultService {
    public Optional<Result> findById(long id);
    public Collection<Result> findByOrderId(long id);

    Result register(long orderId, String resultDataType, byte[] resultData, String identificationType, byte[] identification, LocalDate date, String responsibleName, String responsibleLicenceNumber);
}
