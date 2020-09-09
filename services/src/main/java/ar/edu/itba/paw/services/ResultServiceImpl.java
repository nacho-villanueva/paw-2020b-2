package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Result;
import ar.edu.itba.paw.persistence.ResultDao;
import ar.edu.itba.paw.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultDao resultDao;

    @Override
    public Optional<Result> findById(long id) {
        return resultDao.findById(id);
    }

    @Override
    public Collection<Result> findByOrderId(long id) {
        return resultDao.findByOrderId(id);
    }

    @Override
    public Result register(long order_id, String result_data_type, byte[] result_data, String identification_type, byte[] identification, Date date, String responsible_name, String responsible_licence_number) {
        return resultDao.register(order_id,result_data_type,result_data,identification_type,identification,date,responsible_name,responsible_licence_number);
    }
}

