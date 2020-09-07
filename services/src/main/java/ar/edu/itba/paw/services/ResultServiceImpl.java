package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Result;
import ar.edu.itba.paw.persistence.ResultDao;
import ar.edu.itba.paw.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultDao resultDao;

    @Override
    public Result findById(long id) {
        return resultDao.findById(id);
    }

    @Override
    public Collection<Result> findByOrderId(long id) {
        return resultDao.findByOrderId(id);
    }
}