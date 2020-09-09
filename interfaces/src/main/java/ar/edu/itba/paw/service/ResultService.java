package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Result;

import java.util.Collection;

import java.util.Optional;

public interface ResultService {
    public Optional<Result> findById(long id);
    public Collection<Result> findByOrderId(long id);
}
