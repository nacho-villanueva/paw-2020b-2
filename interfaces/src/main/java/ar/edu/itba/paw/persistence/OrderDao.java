package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Order;

import java.util.Optional;

public interface OrderDao {

    public Optional<Order> findById(long id);
}
