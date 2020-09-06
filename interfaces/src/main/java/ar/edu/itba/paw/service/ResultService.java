package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Result;

import java.util.Collection;

public interface ResultService {
    public Result findById(long id);
    public Collection<Result> findByOrderId(long id);
}
