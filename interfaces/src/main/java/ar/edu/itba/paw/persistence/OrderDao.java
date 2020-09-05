package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Order;

public interface OrderDao {

    public Order findById(long id);
}
