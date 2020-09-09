package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Result;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

import java.util.Optional;

public interface ResultService {
    public Optional<Result> findById(long id);
    public Collection<Result> findByOrderId(long id);

    Result register(long order_id, String result_data_type, byte[] result_data, String identification_type, byte[] identification, Date date, String responsible_name, String responsible_licence_number);
}
