package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Order;

import java.util.Optional;

public interface OrderService {
    public Optional<Order> findById(long id);
}
