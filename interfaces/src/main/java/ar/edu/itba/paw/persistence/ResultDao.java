package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Result;

import java.util.Collection;

public interface ResultDao {

    public Result findById(long id);
    public Collection<Result> findByOrderId(long id);
}
