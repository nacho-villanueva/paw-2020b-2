package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Result;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

public interface ResultDao {

    public Optional<Result> findById(int id);
    public Collection<Result> findByOrderId(long id);

    Result register(long order_id, byte[] result_data, byte[] identification, Date date, String responsible_name, String responsible_licence_number);
}
