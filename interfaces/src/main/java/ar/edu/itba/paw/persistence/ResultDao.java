package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Result;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

public interface ResultDao {

    public Optional<Result> findById(long id);
    public Collection<Result> findByOrderId(long id);

    Result register(long orderId, String resultDataType, byte[] resultData, String identificationType, byte[] identification, Date date, String responsibleName, String responsibleLicenceNumber);
}
