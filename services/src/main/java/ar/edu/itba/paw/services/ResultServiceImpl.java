package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Result;
import ar.edu.itba.paw.service.ResultService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ResultServiceImpl implements ResultService {

    @Override
    public Result findById(long id) {
        return null;
    }

    @Override
    public Collection<Result> findByOrderId(long id) {
        return null;
    }
}
