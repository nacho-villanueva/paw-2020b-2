package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Result;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

public interface ResultService {
    public Optional<Result> findById(long id);
    public Collection<Result> findByOrderId(long id);

    Result register(long order_id, String result_data_type, byte[] result_data, String identificationType, byte[] identification, Date date, String responsible_name, String responsible_licenceNumber);
}
